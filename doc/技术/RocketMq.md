RocketMQ被阿里广泛应用在订单、交易、充值、流计算、消息推送、日志流式处理、binglog分发等场景，已然是阿里核心系统的“扛把子”。

拿最最重要的**数据可靠性**举例，RocketMQ支持**异步实时刷盘、同步刷盘、同步复制、异步复制**，而则Kafka使用异步刷盘方式，异步复制、同步复制。RocketMQ的同步刷盘在单机可靠性上比Kafka更高，不会因为操作系统Crash，导致数据丢失。

kafka同步Replication理论上性能低于RocketMQ的同步Replication，原因是Kafka的数据以分区为单位组织，意味着一个Kafka实例上会有几百个数据分区。而RocketMQ一个实例上只有一个数据分区，可以充分利用IO组Commit机制，有更好的IO性能，降低性能损耗。kafka 是分区内partition 顺序读写。rocketMq是topic磁盘顺序读写。

不仅如此，在消息投递实时性、严格的消息顺序、定时消息等核心功能方面，RocketMQ均比Kafka更加出色。非常适合**应对更丰富的业务场景**，包括涉及异步解耦、顺序消息、分布式事务消息（**交易系统、支付红包**等）等大厂核心场景！

- Producer：就是消息生产者，可以集群部署。它会先和 NameServer 集群中的随机一台建立长连接，得知当前要发送的 Topic 存在哪台 Broker Master上，然后再与其建立长连接，支持多种负载平衡模式发送消息。
- Consumer：消息消费者，也可以集群部署。它也会先和 NameServer 集群中的随机一台建立长连接，得知当前要消息的 Topic 存在哪台 Broker Master、Slave上，然后它们建立长连接，支持集群消费和广播消费消息。
- Broker：主要负责消息的存储、查询消费，支持主从部署，一个 Master 可以对应多个 Slave，Master 支持读写，Slave 只支持读。Broker 会向集群中的每一台 NameServer 注册自己的路由信息。
- NameServer：是一个很简单的 Topic 路由注册中心，支持 Broker 的动态注册和发现，保存 Topic 和 Borker 之间的关系。通常也是集群部署，但是各 NameServer 之间不会互相通信， 各 NameServer 都有完整的路由信息，即无状态。它的特点就是轻量级，无状态。角色类似于 Zookeeper 的情况，从上面描述知道其主要的两个功能就是：Broker 管理、路由信息管理。

## 流程

先启动 NameServer 集群，各 NameServer 之间无任何数据交互，Broker 启动之后会向所有 NameServer 定期（每 30s）发送心跳包，包括：IP、Port、TopicInfo，NameServer 会定期扫描 Broker 存活列表，如果超过 120s 没有心跳则移除此 Broker 相关信息，代表下线。

这样每个 NameServer 就知道集群所有 Broker 的相关信息，此时 Producer 上线从 NameServer 就可以得知它要发送的某 Topic 消息在哪个 Broker 上，和对应的 Broker （Master 角色的）建立长连接，发送消息。

Consumer 上线也可以从 NameServer  得知它所要接收的 Topic 是哪个 Broker ，和对应的 Master、Slave 建立连接，接收消息。

简单的工作流程如上所述，相信大家对整体数据流转已经有点印象了，我们再来看看每个部分的详细情况。

## Producer

Producer 无非就是消息生产者，那首先它得知道消息要发往哪个 Broker ，于是每 30s 会从某台 NameServer 获取 Topic 和 Broker 的映射关系存在本地内存中，如果发现新的 Broker 就会和其建立长连接，每 30s 会发送心跳至 Broker 维护连接。

并且会轮询当前可以发送的 Broker 来发送消息，达到负载均衡的目的，在同步发送情况下如果发送失败会默认重投两次（retryTimesWhenSendFailed = 2），并且不会选择上次失败的 broker，会向其他 broker 投递。

在异步发送失败的情况下也会重试，默认也是两次 （retryTimesWhenSendAsyncFailed = 2），但是仅在同一个 Broker 上重试。

## 小结一下

Producer 每 30s 会向 NameSrv 拉取路由信息更新本地路由表，有新的 Broker 就和其建立长连接，每隔 30s 发送心跳给 Broker 。

不要在生产环境开启 autoCreateTopicEnable。

Producer 会通过重试和延迟机制提升消息发送的高可用。

# Broker

- Remoting 远程模块，处理客户请求。
- Client Manager 管理客户端，维护订阅的主题。
- Store Service 提供消息存储查询服务。
- HA Serivce，主从同步高可用。
- Index Serivce，通过指定key 建立索引，便于查询。

## Broker 的存储

RocketMQ 存储用的是本地文件存储系统，效率高也可靠。

主要涉及到三种类型的文件，分别是 CommitLog、ConsumeQueue、IndexFile。

RocketMQ 的**所有主题的消息都存在** CommitLog 中，单个 CommitLog 默认 1G，并且文件名以起始偏移量命名，固定 20 位，不足则前面补 0，比如 00000000000000000000 代表了第一个文件，第二个文件名就是 00000000001073741824，表明起始偏移量为 1073741824，以这样的方式命名用偏移量就能找到对应的文件。

**所有消息都是顺序写入的**，超过文件大小则开启下一个文件。

### ConsumeQueue

ConsumeQueue 消息消费队列，可以认为是 **CommitLog 中消息的索引**，因为 CommitLog 是糅合了所有主题的消息，所以通过索引才能更加高效的查找消息。

ConsumeQueue **存储的条目是固定大小**，只会存储 8 字节的 commitlog 物理偏移量，4 字节的消息长度和 8 字节 Tag 的哈希值，固定 20 字节。

在实际存储中，**ConsumeQueue 对应的是一个Topic 下的某个 Queue**，每个文件约 5.72M，由 30w 条数据组成。

消费者是先从 ConsumeQueue 来得到消息真实的物理地址，然后再去 CommitLog 获取消息。

### IndexFile

IndexFile 就是索引文件，是额外提供查找消息的手段，不影响主流程。

通过 **Key 或者时间区间来查询对应的消**息，文件名以创建时间戳命名，固定的单个 IndexFile 文件大小约为400M，一个 IndexFile 存储 2000W个索引。

![rocketMq](/Users/yangyibo/Documents/技能点/整理知识点图/技术/rocketmq/rocketMq.png)

消息到了先存储到 Commitlog，然后会有一个 ReputMessageService 线程接近实时地将消息转发给消息消费队列文件与索引文件，也就是说是异步生成的。

## 消息刷盘机制

RocketMQ 提供消息**同步刷盘**和**异步刷盘**两个选择，关于刷盘我们都知道效率比较低，单纯存入内存中的话效率是最高的，但是可靠性不高，影响消息可靠性的情况大致有以下几种：

1. Broker 被暴力关闭，比如 kill -9
2. Broker 挂了
3. 操作系统挂了
4. 机器断电
5. 机器坏了，开不了机
6. 磁盘坏了

如果都是 **1-4 的情况，同步刷盘肯定没问题**，异步的话就有可能丢失部分消息，**5 和 6就得依靠副本机制了**，如果同步双写肯定是稳的，但是性能太差，如果异步则有可能丢失部分消息。

所以需要看场景来使用同步、异步刷盘和副本双写机制。

## 页缓存与内存映射

Commitlog 是混合存储的，所以所有消息的**写入就是顺序写入**，对文件的**顺序写入和内存的写入速度基本上没什么差别**。

并且 RocketMQ 的文件都利用了**内存映射即 Mmap**，将**程序虚拟页面直接映射到页缓存上**，无需内核态再往用户态的拷贝。

![mmap](/Users/yangyibo/Documents/技能点/整理知识点图/技术/rocketmq/mmap.png)

页缓存其实就是操作系统对文件的缓存，用来加速文件的读写，也就是说**对文件的写入先写到页缓存**中，操作系统会**不定期刷盘**（时间不可控），对**文件的读会先加载到页缓存**中，并且**根据局部性原理还会预读临近块的内容**（预读）。

其实也是**因为使用内存映射机制**，所以 RocketMQ 的文件存储都**使用定长结构**来存储，方便一次将整个文件映射至内存中。

## 文件预分配和文件预热

而内存映射也只是做了映射，只有当真正读取页面的时候产生缺页中断，才会将数据真正加载到内存中，所以 RocketMQ 做了一些优化，防止运行时的性能抖动。

### 文件预分配

CommitLog 的大小默认是1G，当超过大小限制的时候需要准备新的文件，而 RocketMQ 就起了一个后台线程 AllocateMappedFileService，不断的处理 AllocateRequest，AllocateRequest 其实就是预分配的请求，会提前准备好下一个文件的分配，防止在消息写入的过程中分配文件，产生抖动。

### 文件预热

有一个 warmMappedFile 方法，它会把当前映射的文件，每一页遍历，写入一个0字节，然后再调用mlock 和 madvise(MADV_WILLNEED)。

mlock：可以**将进程使用的部分或者全部的地址空间锁定在物理内存**中，防止其被交换到 swap 空间。

madvise：给操作系统建议，说这文件在不久的将来要访问的，因此，提前读几页可能是个好主意。

## 小结一下

CommitLog 采用混合型存储，也就是**所有 Topic 都存在一起**，顺序追加写入，文件名用起始偏移量命名。

消息先写入 CommitLog 再**通过后台线程分发到 ConsumerQueue 和 IndexFile** 中。

消费者**先读取 ConsumerQueue 得到真正消息的物理地址**，然后访问 CommitLog 得到真正的消息。

利用了 mmap 机制减少一次拷贝，利用文件预分配和文件预热提高性能。

提供同步和异步刷盘，根据场景选择合适的机制。

**顺序写盘**，整体来看是**顺序读盘**，并且使用了 mmap，不是真正的零拷贝。又因为页缓存的不确定性和 mmap 惰性加载(访问时缺页中断才会真正加载数据)，用了文件预先分配和文件预热即每页写入一个0字节，然后再调用`mlock` 和 `madvise(MADV_WILLNEED)`。

## Broker 的 HA

从 Broker 会和**主 Broker** 建立长连接，然后获取主 Broker commitlog 最大偏移量，开始向主 Broker 拉取消息，主 Broker 会返回一定数量的消息，循环进行，达到主从数据同步。

消费者消费消息会先请求主 Broker ，如果主 Broker 觉得现在压力有点大，则会返回从 Broker 拉取消息的建议，然后消费者就去从服务器拉取消息。

## Consumer

消费有两种模式，分别是**广播模式和集群模式**。

广播模式：一个分组下的每个消费者都会**消费完整**的Topic 消息。

集群模式：一个分组下的消费者**瓜分**消费Topic 消息。

一般我们用的都是集群模式。

而消费者消费消息又分为**推和拉**模式，详细看我这篇文章[消息队列推拉模式](https://mp.weixin.qq.com/s?__biz=Mzg2MjEyNTk1Ng==&mid=2247485324&idx=1&sn=ce900c8438936ab6f3ed21105e099236&scene=21#wechat_redirect)，分别从源码级别分析了 RokcetMQ 和 Kafka 的消息推拉，以及推拉模式的优缺点。

## Consumer 端的负载均衡机制

Consumer 会定期的获取 Topic 下的队列数，然后再去查找订阅了该 Topic 的同一消费组的所有消费者信息，默认的分配策略是类似分页排序分配。

将队列排好序，然后消费者排好序，比如队列有 9 个，消费者有 3 个，那消费者-1 消费队列 0、1、2 的消息，消费者-2 消费队列 3、4、5，以此类推。

所以如果负载太大，那么就加队列，加消费者，通过负载均衡机制就可以感知到重平衡，均匀负载。

## Consumer 消息消费的重试

难免会遇到消息消费失败的情况，所以需要提供消费失败的重试，而一般的消费失败要么就是消息结构有误，要么就是一些暂时无法处理的状态，所以立即重试不太合适。

**RocketMQ 会给每个消费组都设置一个重试队列**，Topic 是 `%RETRY%+consumerGroup`，并且设定了很多重试级别来延迟重试的时间。

为了利用 RocketMQ 的延时队列功能，重试的消息会先保存在 Topic 名称为“SCHEDULE_TOPIC_XXXX”的延迟队列，在消息的扩展字段里面会存储原来所属的 Topic 信息。

delay 一段时间后再恢复到重试队列中，然后 Consumer 就会消费这个重试队列主题，得到之前的消息。

如果超过一定的重试次数都消费失败，则会移入到死信队列，即 Topic `%DLQ%" + ConsumerGroup` 中，存储死信队列即认为消费成功，因为实在没辙了，暂时放过。

然后我们可以通过人工来处理死信队列的这些消息。

## 消息的全局顺序和局部顺序

**全局顺序就是消除一切并发，一个 Topic 一个队列**，Producer 和 Consuemr 的并发都为一。

**局部顺序其实就是指某个队列顺序，多队列之间还是能并行的**。

可以通过 MessageQueueSelector 指定 Producer 某个业务只发这一个队列，然后 Comsuer 通过MessageListenerOrderly 接受消息，其实就是加锁消费。

在 Broker 会有一个 mqLockTable ，顺序消息在创建拉取消息任务的时候需要在 Broker 锁定该消息队列，之后加锁成功的才能消费。

而严格的顺序消息其实很难，假设现在都好好的，如果有个 Broker 宕机了，然后发生了重平衡，队列对应的消费者实例就变了，就会有可能会出现乱序的情况，如果要保持严格顺序，那此时就只能让整个集群不可用了。

## 一些注意点

1、**订阅消息是以 ConsumerGroup 为单位存储的**，所以ConsumerGroup 中的每个 Consumer 需要有相同的订阅。

因为订阅消息是随着心跳上传的，**如果一个 ConsumerGroup 中 Consumer 订阅信息不一样，那么就会出现互相覆盖的情况**。

比如消费者 A 订阅 Topic a，消费者 B 订阅 Topic b，此时消费者 A 去 Broker 拿消息，然后 B 的心跳包发出了，Broker 更新了，然后接到 A 的请求，一脸懵逼，没这订阅关系啊。

2、RocketMQ 主从读写分离

**从只能读，不能写**，并且只有当前客户端读的 offset 和 当前 Broker 已接受的最大 offset 超过限制的物理内存大小时候才会去从读，所以正常情况下从分担不了流量

3、**单单加机器提升不了消费速度，队列的数量也需要跟上。**

4、之前提到的，不要允许自动创建主题

## 推拉模式

一般而言我们在谈论**推拉模式的时候指的是 Comsumer 和 Broker 之间的交互**。

默认的认为 Producer 与 Broker 之间就是推的方式，即 Producer 将消息推送给 Broker，而不是 Broker 主动去拉取消息。

### 推模式

推模式指的是消息从 Broker 推向 Consumer，即 Consumer 被动的接收消息，由 Broker 来主导消息的发送。

**推模式有什么好处？**

**消息实时性高**， Broker 接受完消息之后可以立马推送给 Consumer。

**对于消费者使用来说更简单**，简单啊就等着，反正有消息来了就会推过来。

**推模式有什么缺点？**

**推送速率难以适应消费速率**推模式的目标就是以最快的速度推送消息，当生产者往 Broker 发送消息的速率大于消费者消费消息的速率时，随着时间的增长消费者那边可能就“爆仓”了。

并且不同的消费者的消费速率还不一样，身为 Broker 很难平衡每个消费者的推送速率。如果要实现自适应的推送速率那就需要Broker 维护每个消费者的状态进行推送速率的变更

## 拉模式

拉模式指的是 Consumer 主动向 Broker 请求拉取消息，即 Broker 被动的发送消息给 Consumer。

**拉模式有什么好处？**

拉模式主动权就在消费者身上了，**消费者可以根据自身的情况来发起拉取消息的请求**。假设当前消费者觉得自己消费不过来了，它可以根据一定的策略停止拉取，或者间隔拉取都行。

**拉模式下 Broker 就相对轻松了**，它只管存生产者发来的消息，至于消费的时候自然由消费者主动发起，来一个请求就给它消息呗，从哪开始拿消息，拿多少消费者都告诉它，它就是一个没有感情的工具人，消费者要是没来取也不关它的事。

**拉模式可以更合适的进行消息的批量发送**，基于推模式可以来一个消息就推送，也可以缓存一些消息之后再推送，但是**推送的时候其实不知道消费者到底一次性能处理多少消息**。而**拉模式就更加合理，它可以参考消费者请求的信息来决定缓存多少消息之后批量发送**。

**拉模式有什么缺点？**

**消息延迟**，毕竟是消费者去拉取消息只能不断地拉取，但是又不能很频繁地请求，太频繁了就变成消费者在攻击 Broker 了。因此需要降低请求的频率，比如隔个 2 秒请求一次，你看着消息就很有可能延迟 2 秒了。

**消息忙请求**，忙请求就是比如消息隔了几个小时才有，那么在几个小时之内消费者的请求都是无效的，在做无用功。

## 那到底是推还是拉

RocketMQ 和 Kafka 都选择了拉模式，当然业界也有基于推模式的消息队列如 ActiveMQ。

个人觉得拉模式更加的合适，因为现在的消息队列都有**持久化消息**的需求，也就是说本身它就有个存储功能，它的使命就是接受消息，保存好消息使得消费者可以消费消息即可。

消费者各种各样，身为 Broker 不应该有依赖于消费者的倾向，我已经为你保存好消息了，你要就来拿好了。

一般而言 Broker 不会成为瓶颈，因为消费端有业务消耗比较慢，但是 Broker 毕竟是一个中心点，能轻量就尽量轻量。

RocketMQ 和 Kafka 都选择了拉模式，对于拉模式的缺点它们进行了优化，减轻了拉模式的缺点。

## 长轮询

RocketMQ 和 Kafka 都是利用“长轮询”来实现拉模式

## RocketMQ 中的长轮询

RocketMQ 中的 PushConsumer 其实是披着推模式外衣的拉模式的方法，**只是看起来像推模式而已**。

因为 RocketMQ 在被背后偷偷的帮我们去 Broker 请求数据了。

后台会有个 RebalanceService 线程，这个线程会根据 topic 的队列数量和当前消费组的消费者个数做负载均衡，每个队列产生的 pullRequest 放入阻塞队列 pullRequestQueue 中。然后又有个 PullMessageService 线程**不断的从阻塞队列 pullRequestQueue 中获取 pullRequest**，然后通过网络请求 broker，这样实现的准实时拉取消息。

![ROcketMq拉](/Users/yangyibo/Documents/技能点/整理知识点图/技术/rocketmq/ROcketMq拉.png)

大致就是每隔5秒主动去拉取消息，然后如果有新消息了，也会通知到消费者去拉取。

## Kafka 中的长轮询

Kafka 在拉请求中有参数，可以使得消费者请求在 “长轮询” 中阻塞等待。

简单的说就是消费者去 Broker 拉消息，定义了一个超时时间，也就是说消费者去请求消息，如果有的话马上返回消息，如果没有的话消费者等着直到超时，然后再次发起拉消息请求。

并且 Broker 也得配合，如果消费者请求过来，有消息肯定马上返回，没有消息那就建立一个延迟操作，等条件满足了再返回

![kafka拉](/Users/yangyibo/Documents/技能点/整理知识点图/技术/rocketmq/kafka拉.png)

## 小结

可以看到 RocketMQ  和 Kafka 都是采用“长轮询”的机制，具体的做法都是通过消费者等待消息，当有消息的时候 Broker 会直接返回消息，如果没有消息都会采取延迟处理的策略，并且为了保证消息的及时性，在对应队列或者分区有新消息到来的时候都会提醒消息来了，及时返回消息。

一句话说就是消费者和 Broker 相互配合，拉取消息请求不满足条件的时候 hold 住，避免了多次频繁的拉取动作，当消息一到就提醒返回。



[进阶必看的 RocketMQ ，就这篇了][https://mp.weixin.qq.com/s/j_ZUJMywyy5tbFlBCCbLLg]

[Kafka和RocketMQ底层存储之那些你不知道的事][https://mp.weixin.qq.com/s?__biz=MzkxNTE3NjQ3MA==&mid=2247485747&idx=1&sn=b3aa0bf544ef7c716dfc6d25495c780a&source=41#wechat_redirect]

[消息队列之推还是拉，RocketMQ 和 Kafka是如何做的？][https://mp.weixin.qq.com/s?__biz=MzkxNTE3NjQ3MA==&mid=2247485735&idx=1&sn=41ceea4e4f0430c3a5d3cc076e2d7870&source=41#wechat_redirect]

## NameServer 如何保证一致性

