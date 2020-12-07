## **为何使用消息系统**

**异步处理、服务解耦、流量控制**

**解耦**
在项目启动之初来预测将来项目会碰到什么需求，是极其困难的。消息系统在处理过程中间插入了一个隐含的、基于数据的接口层，两边的处理过程都要实现这一接口。这允许你独立的扩展或修改两边的处理过程，只要确保它们遵守同样的接口约束。

**冗余**
有些情况下，处理数据的过程会失败。除非数据被持久化，否则将造成丢失。消息队列把数据进行持久化直到它们已经被完全处理，通过这一方式规避了数据丢失风险。许多消息队列所采用的”插入-获取-删除”范式中，在把一个消息从队列中删除之前，需要你的处理系统明确的指出该消息已经被处理完毕，从而确保你的数据被安全的保存直到你使用完毕。

**扩展性**
因为消息队列解耦了你的处理过程，所以增大消息入队和处理的频率是很容易的，只要另外增加处理过程即可。不需要改变代码、不需要调节参数。扩展就像调大电力按钮一样简单。

**灵活性 & 峰值处理能力**
在访问量剧增的情况下，应用仍然需要继续发挥作用，但是这样的突发流量并不常见；如果为以能处理这类峰值访问为标准来投入资源随时待命无疑是巨大的浪费。使用消息队列能够使关键组件顶住突发的访问压力，而不会因为突发的超负荷的请求而完全崩溃。

**可恢复性**
系统的一部分组件失效时，不会影响到整个系统。消息队列降低了进程间的耦合度，所以即使一个处理消息的进程挂掉，加入队列中的消息仍然可以在系统恢复后被处理。

**顺序保证**
在大多使用场景下，数据处理的顺序都很重要。大部分消息队列本来就是排序的，并且能保证数据会按照特定的顺序来处理。Kafka保证一个Partition内的消息的有序性。

**缓冲**
在任何重要的系统中，都会有需要不同的处理时间的元素。例如，加载一张图片比应用过滤器花费更少的时间。消息队列通过一个缓冲层来帮助任务最高效率的执行———写入队列的处理会尽可能的快速。该缓冲有助于控制和优化数据流经过系统的速度。

**异步通信**
很多时候，用户不想也不需要立即处理消息。消息队列提供了异步处理机制，允许用户把一个消息放入队列，但并不立即处理它。想向队列中放入多少消息就放多少，然后在需要的时候再去处理它们。

# Kafka

1. 设计理念

   1.1 kafka与其它消息系统对比

   1.2 kafka特性

2. topic

3. partitions
   3.1 副本复制 

4. broker

5. Producers
   5.1 producer负载均衡
   5.2 producer异步发送

6. consumer
   6.1 Consumer Offset Tracking
   6.2 Consumers and Consumer Groups 

7. offset

8. 消息交付语义
   8.1 Exactly-once

9. 拉取系统

10. kafka的存储
   10.1 Kafka高效文件存储设计特点
   10.2 在partition中如何通过offset查找message
   10.3 那么怎么知道何时读完本条消息，否则就读到下一条消息的内容了
   10.4 Message持久化和缓存
   10.5 满足长时间保存消息
   10.6 Kafka删除策略

11. kafka 性能
    11.1 零拷贝：sendfile
    11.2 reactor网络线程模型
    11.3 小IO问题，批量发送
    11.4 日志压缩（点对点压缩）
    11.5 总结 Kafka性能好在什么地方？
    11.6 分区是不是越多越好呢？

12. 可靠性（高可用）
    12.1 ack确认机制
    12.2 生产者可靠性级别
    12.3 leader Rebalance
    12.4 controller Rebalance
    12.5 Consumer Rebalance

13. 事务

14. kafka用zookeeper实现的服务类型


## 设计理念

1、高吞吐量来支持高容量的事件流处理
2、针对实时性场景。支持从离线系统加载数据
3、低延迟的消息系统
4、关于消息被处理的状态是在consumer端维护，而不是由kafka server端维护。
5、分布式，producer、broker和consumer都分布于多台机器上。

## Topic

一个 Topic 可以认为是一类消息,每个 topic 将被分成多 个 partition(区),每个 partition 在存储层面是 append log 文件。任 何发布到此 partition 的消息都会被直接追加到 log 文件的尾部,每 条消息在文件中的位置称为 offset(偏移量),offset 为一个 long 型数字,它是唯一标记一条消息。kafka 并没有提供其他额外的索引 机制来存储 offset,因为在 kafka 中几乎不允许对消息进行 “随机 读写”。

kafka 中,**即使消息被消费,消息仍然不会被立即删除。日志文 件将会根据 broker 中的配置要求 ,保留一定的时间之后删除** ;比如 log 文件保留 2 天,那么两天后,文件会被清除,无论其中的消息是否 被消费。kafka 通过这种简单的手段,来释放磁盘空间,以及减少消息 消费之后对文件内容改动的磁盘 IO 开支。

## partitions

partitions 的设计目的有多个 。**最根本原因是 kafka 基于文件 存储。通过分区partition,可以将日志内容分散到多个 server 上,来避免文件 尺寸达到单机磁盘的上限 ,每个 partiton 都会被当前 server(kafka 实例)保存;可以将一个 topic 切分多任意多个 partitions 来保存消 息。此外越多的 partitions 意味着可以容纳更多的 consumer,有效 提升并发消费的能力（吞吐量）。**

Kafka的producer和consumer都可以**多线程地并行**操作，而每个线程处理的是一个分区的数据。因此**分区实际上是调优Kafka并行度的最小单元**。对于producer而言，它实际上是用**多个线程并发地向不同分区所在的broker发起Socket连接**同时给这些分区发送消息（IO多路复用，reactor模型）；而consumer，同一个消费组内的所有**consumer线程都被指定topic的某一个分区**进行消费。

一个 Topic 的多个 partitions,被分布在 kafka 集群中的多 个 server 上;每个 server(kafka 实例)负责 partitions 中消息的读写 操作;此外 kafka 还可以配置 partitions 需要**备份的个数(replicas)**, 每个partition 将会被备份到多台机器上,以提高可用性。

基于 replicated(partition 的备份) 方案,那么就意味着需要对多个备份进行调度 ; 每个 partition 都有一个 server 为”leader”;**leader 负责所有的读 写操作,如果 leader 失效,那么将会有其他 follower 来接管(成为新的leader);follower 只是单调的和 leader 跟进,同步消息即可。**由 此可见作为 leader 的 server 承载了**全部的请求压力**,**有多少个 partitions 就意味着有多少个 “leader”**,kafka 会将”l**eader”均衡的分散在每个实例上**,来确保整体的性能稳定。

##### 消息partition内有序

发送到 partitions 中的消息将会按照它接收的顺序追加到日志 中。对于消费者而言,它们消费消息的顺序和日志中消息顺序一致。如果 Topic 的”replication factor”为 N,那么允许 N-1 个 kafka 实例失效。**（就是最低要有一个partitions存活，也就是partition可以没有备份）**

### 副本复制 

1）一个partition的复制个数（replication factor）包括这个partition的leader本身。

2）所有对partition的读和写都通过leader。

3）Followers通过pull获取leader上log（message和offset）

4）如果一个follower挂掉、卡住或者同步太慢，leader会把这个follower从”in sync replicas“（ISR）列表中删除。

5）当所有的”in sync replicas“的follower把一个消息写入到自己的log中时，这个消息才被认为是”committed“的。

6）如果针对某个partition的所有复制节点都挂了，Kafka选择最先复活的那个节点作为leader（这个节点不一定在ISR里）。

## broker

已发布的消息保存在一组服务器中，它们被称为代理（Broker）或Kafka集群。

与其它消息系统不同，Kafka broker是无状态的。这意味着消费者必须维护已消费的状态信息。这些信息由消费者自己维护，broker完全不管（有offset managerbroker管理）。

- 从Broker删除消息变得很棘手，因为Broker并不知道消费者是否已经使用了该消息。Kafka创新性地解决了这个问题，它将一个简单的基于时间的SLA应用于保留策略。当消息在代理中超过一定时间后，将会被自动删除。
- 这种创新设计有很大的好处，消费者可以故意倒回到老的偏移量再次消费数据。这违反了队列的常见约定，但被证明是许多消费者的基本特征。

## Producers

Producer 将消息发布到指定的 Topic 中,同时 Producer 也能决 定将此消息归属于哪个 partition;比如基于”round-robin”方式或者 通过其他的一些算法等。

- Producer发送消息到Topic时，分配partition的算法如下：

>1. **如果指定了一个partition，那么直接使用指定的partition**
>2. **如果没有指定partition，但是指定了key，那么会根据key进行哈希，分配到对应的partition中**
>3. **如果partition和key都没指定，会使用round-robin算法进行分配**

### **producer负载均衡**

producer根据用户指定的算法（**默认路由规则：hash(key)%numPartitions**），将消息发送到指定的partition
存在多个partiiton，每个partition有自己的replica，每个replica分布在不同的Broker节点上
多个partition需要选取出lead partition，**lead partition负责读写**，并由zookeeper负责fail over
通过zookeeper管理broker与consumer的动态加入与离开

### **producer异步发送：**

异步非阻塞操作是一个可扩展消息系统的基本操作，kafka当然也提供这样一个操作（producer.type=async)。producer 可以在内存中缓存要发送的消息，然后等到触发时间或者缓存内容达到配置好的buffer的大小，就会批量发送消息。由于产生消息的机器一般都是异构的，产 生数据的速度是不同的，这种异步缓存机制会对broker产生统一的通信量，会更好的提高网络利用率和更高的吞吐量。

若broker宕机，buffer 丢失，segment不完整，启动server时首先会检查segment 完整性

## Consumers

每个 consumer 属于一个 consumer group;反过来说,每个 group 中可以有多个 consumer。发送到 Topic 的消息,只会被订阅此 Topic 的每个 group 中的**一个 consumer 消费**

Kafka保证**同一Consumer Group中只有一个Consumer会消费某条消息**，实际上，Kafka保证的是稳定状态下每一个**Consumer实例只会消费某一个或多个特定Partition的数据**，而某个Partition的数据只会被某一个特定的Consumer实例所消费。也就是说Kafka对消息的分配是**以Partition为单位分配的**，而非以每一条消息作为分配单元。这样设计的劣势是无法保证同一个Consumer Group里的Consumer**均匀消费数据**，优势是每个**Consumer不用都跟大量的Broker通信，减少通信开销**，同时也降低了分配难度，实现也更简单。另外，**因为同一个Partition里的数据是有序的，这种设计可以保证每个Partition里的数据可以被有序消费。**
　　如果某Consumer Group中Consumer（每个Consumer只创建1个MessageStream）数量少于Partition数量，则至少有一个Consumer会消费多个Partition的数据，如果Consumer的数量与Partition数量相同，则正好一个Consumer消费一个Partition的数据。而如果Consumer的数量多于Partition的数量时，会有部分Consumer无法消费该Topic下任何一条消息。

**Consumer Offset Tracking**

1）High-level consumer记录每个partition所消费的maximum offset，并定期commit到offset manager（broker）。

2）Simple （Low Level） consumer需要手动管理offset。现在的Simple consumer Java API只支持commit offset到zookeeper

##### Low Level consumer原因

- 同一条消息读多次
- 只读取某个Topic的部分Partition
- 管理事务，从而确保每条消息被处理一次，且仅被处理一次

　　与Consumer Group相比，Low Level Consumer要求用户做大量的额外工作。

- 必须在应用程序中跟踪offset，从而确定下一条应该消费哪条消息
- 应用程序需要通过程序获知每个Partition的Leader是谁
- 必须处理Leader的变化

　　使用Low Level Consumer的一般流程如下

- 查找到一个“活着”的Broker，并且找出每个Partition的Leader
- 找出每个Partition的Follower
- 定义好请求，该请求应该能描述应用程序需要哪些数据
- Fetch数据
- 识别Leader的变化，并对之作出必要的响应

**Consumers and Consumer Groups**

1）consumer注册到zookeeper

2）属于同一个group的consumer（group id一样）平均分配partition，每个partition只会被一个consumer消费。

3）当broker或同一个group的其他consumer的状态发生变化的时候，consumer rebalance就会发生。

## offeset

对于 consumer 而言,它需要保存消费消息的 offset（后来移动到 **broker 名字为*consumer_offsets* 的topic中保存**）,对于 offset的保存和使用 ,由 consumer 来控制 ;当 consumer 正常消费消息 时,offset 将会”线性”的向前驱动,即消息将依次顺序被消费 。事实 上 consumer 可以使用任意顺序消费消息,它只需要将 offset 重置为 任意值。(offset 将会保存在 zookeeper 中,参见下文)

__老版本的消费位移信息是存储的zookeeper 中的， 但是zookeeper 并不适合频繁的写入查询操作，所以在新版本的中消费位移信息存放在了__consumer_offsets内置topic中，consumer_offsets这个用于存储offset的topic是由kafka服务器默认自动创建的默认50个分区，通过消费组名字获取存储offset的分区`Math.abs("consumer_offsets_group".hashCode()) % 50`。是kafka内置的topic，禁止删除。可以在zookeeper中删除__consumer_offsets分区不会被创建，它会在消费者开始消费数据的时候被创建。

## 

#### **消息交付语义：**

- **最多一次**(at most once)：读完消息先commit再处理消息。这种模式下，如果Consumer在commit后还没来得及处理消息就crash了，下次重新开始工作后就无法读到刚刚已提交而未处理的消息，这就对应于At most once
- **至少一次**(at least once)：读完消息先处理再commit。这种模式下，如果在处理完消息之后commit之前Consumer crash了，下次重新开始工作时还会处理刚刚未commit的消息，实际上该消息已经被处理过了。这就对应于At least once
- **恰好一次**(exactly)：幂等一次 ，这正是我们想要的。

Kafka默认保证at-least-once delivery，容许用户实现at-most-once语义，exactly-once的实现取决于目的存储系统，kafka提供了读取offset，实现也没有问题。

### Exactly-once 语意

分布式系统中最难解决的两个问题是：

1. 消息顺序保证（Guaranteed order of messages）。
2. 消息的精确一次投递（Exactly-once delivery）。

**幂等：partition内部的exactly-once顺序语义**

幂等操作，是指可以执行多次，而不会产生与仅执行一次不同结果的操作，Producer的send操作现在是幂等的。在任何导致producer重试的情况下，相同的消息，如果被producer发送多次，也只会被写入Kafka一次。要打开此功能，并让所有partition获得exactly-once delivery、无数据丢失和in-order语义，需要修改broker的配置：**enable.idempotence = true**。

**实现**

1. 它的工作方式类似于TCP：发送到Kafka的每批消息将包含一个序列号，该序列号用于重复数据的删除。与TCP不同，TCP只能在transient in-memory中提供保证。**序列号将被持久化存储topic中，因此即使leader replica失败，接管的任何其他broker也将能感知到消息是否重复。**这种机制的开销相当低：它只是在每批消息中添加了几个额外字段

2. 事务实现：**跨partition的原子性写操作**。Kafka现在支持使用新事务API原子性的对跨partition进行写操作，该API允许producer发送批量消息到多个partition。该功能同样支持在同一个事务中提交消费者offsets。因此真正意义上实现了end-to-end的exactly-once delivery语义。**Kafka topic partition内部的消息可能是事务完整提交后的消息，也可能是事务执行过程中的部分消息**。

3. **Exactly-once 流处理**：配置 `processing.guarantee=exactly_once`

### **拉取系统**

作为一个messaging system，Kafka遵循了传统的方式，由producer向broker push消息并由consumer从broker pull消息。consumer拉取partition中的消息都使用到zero cory技术

由于kafka broker会持久化数据，broker没有内存压力，因此，consumer非常适合采取pull的方式消费数据，具有以下几点好处：

consumer pull的优点：
* 简化kafka设计
* consumer自己控制消息的读取速度和数量。
* consumer根据自身情况自主选择消费模式，例如批量，重复消费，从尾端开始消费等

3)consumer pull的缺点：

* 如果broker没有数据，则可能要pull多次忙等待，Kafka可以配置consumer long pull一直等到有数据。

## **kafka的存储**

1）依赖文件系统，持久化到本地
2) 数据持久化到log

Topic在逻辑上可以被认为是一个queue，每条消费都必须指定它的Topic。为了使得Kafka的吞吐率可以线性提高，**物理上把Topic分成一个或多个Partition**，每个**Partition在物理上对应一个文件夹**，该文件夹下存储这个Partition的所有**消息和索引文件**

发布者发到某个topic的消息会被均匀的分布到多个partition上（或根据用户指定的路由规则进行分布），broker收到发布消息往对应partition的最后一个segment上添加该消息。当某个segment上的消息条数达到**配置值**或消息**发布时间超过阈值时**，segment上的消息会被flush到磁盘，**只有flush到磁盘上的消息订阅者才能订阅到**

segment达到一定的大小后将不会再往该segment写数据，broker会创建新的segment。

https://img-blog.csdn.net/20161007013635914 

- 多个segment(段)：每个partition(目录)相当于一个**巨型文件被 平均分配到多个大小相等segment(段)数据文件中**（默认大小500M）。但每个段segment file**消息数量不一定相等**，这种特性方便old segment file快速被删除。
- 顺序读写：每个**partition只需要支持顺序读写就行了**，segment文件生命周期由服务端配置参数决定。
  这样做的好处就是能快速删除无用文件，有效提高磁盘利用率。
- 索引和文件：segment file组成：由2大部分组成，分别为**index file（存储的是key-value格式的，key代表在.log中按顺序开始第条消息，value代表该消息的位置偏移，.index中不是对每条消息都做记录，它是每隔一些消息记录一次）和data file**，此2个文件一一对应，成对出现，后缀”.index”和“.log”分别表示为segment索引文件、数据文件.
- 名字为当前最大的offset ：**segment文件命名规则**：partition全局的第一个segment从0开始，后续每个segment文件名为**上一个**全局partition的**最大offset(偏移message数**，也就是说新的**segment的名字为当前partition中最大的offse**t)。数值最大为64位long大小(64字节的offset)，19位数字字符长度，没有数字用0填充。

### Kafka高效文件存储设计特点

- Kafka把topic中一个parition大文件分成多个小文件段，通过多个小文件段，就容易定期清除或删除已经消费完文件，减少磁盘占用。
- 通过索引信息可以快速定位message和确定response的最大大小。
- 通过index元数据全部映射到memory，可以避免segment file的IO磁盘操作。
- 通过索引文件稀疏存储，可以大幅降低index文件元数据占用空间大小。

### **在partition中如何通过offset查找message**

例如读取offset=368776的message

* 第一步查找segment file：其中00000000000000000000.index表示最开始的文件，起始偏移量(offset)为0.第二个文件 00000000000000368769.index的消息量起始偏移量为368770 = 368769 + 1.同样，第三个文件00000000000000737337.index的起始偏移量为737338=737337 + 1，其他后续文件依次类推，以起始偏移量命名并排序这些文件，只要根据offset **二分查找**文件列表，就可以快速定位到具体文件。所以当offset=368776时定位到00000000000000368769.index|log
* 第二步：通过segment file查找到message通过第一步定位到segment file，当offset=368776时，依次定位到00000000000000368769.index索引中的[6,1407]定位到 00000000000000368769.log文件1407的位置（由于index并不是对每条消息都做记录的，是每隔一些消息记录一次）。顺序查找直到 offset=368776为止。

segment index file采取稀疏索引存储方式，它减少索引文件大小，通过map可以直接内存操作，稀疏索引为数据文件的每个对应message设置一个元数据指针,它 比稠密索引节省了更多的存储空间，但查找起来需要消耗更多的时间。

### 那么怎么知道何时读完本条消息，否则就读到下一条消息的内容了？

这个就需要联系到消息的物理结构了，消息都具有固定的物理结构，包括：offset（8 Bytes）、消息体的大小（4 Bytes）、crc32（4 Bytes）、magic（1 Byte）、attributes（1 Byte）、key length（4 Bytes）、key（K Bytes）、payload(N Bytes)等等字段，可以确定一条消息的大小，即读取到哪里截止。

### **Message持久化和缓存**

（持久化就是文件系统＋页缓存）

Kafka是依赖文件系统来存储和缓存消息的，（但是大家都觉得磁盘是比较慢的）。磁盘不同用法会造成速度上的巨大差别。一个**67200rpm SATA磁盘 线性写可达到300M/s，但是如果是随机写，只有50k/s**并且，**kafka是运行在JVM上的，JVM两个特性：**

- object 的内存开销是非常大的，**经常是要存储数据的两倍（或者更高）**
- Java的内存回收机制随着堆内存的数据的增加变得频繁。

作为这些因素的结果，使用 **文件系统 **和依赖于 **页缓存** 比维持一个内存的存储或者其他的结构（BTree）有优势------我们至少通过**自动访问所有的空闲内存使得可用的缓存加倍**，而且可能通过**存储一个紧凑的字节结构而不是单独的对象使得可用的缓存又增加一倍的大小**。

我们**不是把数据尽量多的维持在内存中**并只有当需要的时候在将数据刷到文件系统，我们是反其道而行之。 **所有的数据不用进行任何的刷数据的调用就立刻被写入到文件系统的一个持久化的日志中记录** 。事实上这只是意味着转移到了内核的 **页缓存** 中，OS将在之后将它刷出。接着我们添加一个配置驱动器刷数据策略来允许系统的用户控制数据被刷入物理磁盘的频率（每多少消息或者每多少秒）来设置一个在临界磁盘崩溃时数据量的一个限制。

#### **满足长时间保存消息：**

一般消息系统**持久化数据结构是用BTree**，使得在消息系统中支持一个广泛的各种各样的事务性的和非事务性的语义。但是BTree的开销还是比较高的：**B树操作的复杂度是O(log N)**，这个开销貌似是固定的。但是对磁盘操作却不是这样的，因为需要考虑磁盘寻道的开销。此外，为满足事务性语义，BTree还要考虑row-lock，无疑这样的开销是非常大的。

直观上一个持久化的队列可以进行**简单读写和添加数据到文件**。尽管不能支持B数的丰富语义，但是他的优势是：快！O(1)并且读写不相互阻塞。这样还有个好处，可以长时间存

### Kafka删除策略

1）N天前的删除。

2）保留最近的MGB数据。

## **kafka 性能**

（总结：通过sendfile（零拷贝技术）点对点压缩 保证网络性能）

**sendfile：将数据从页缓存发送到socket**，零拷贝在java中 则是通过java.nio.channels.FileChannel中的transferTo方法来实现的

通常有两种原因造成**效率低下**： 太多的网络请求，过多的字节拷贝。
为提供效率，kafka的API围绕 “message set”概念构建，这种方式是天然的将消息分组。这样可以允许一次请求 **一组** 消息，并且分摊了网络往返的开销。

Lazily desialized ：**MessageSet** 实现本身是一个封装了字节 **数组** 或者文件的API。

被broker维护的message的记录本身只是个被写入磁盘的message sets的目录。维护**字节数组或者文件**对网络传输是非常方便的，现代的unix操作系统提供了一个非常高效的方法**将数据从页缓存发送到socket**------ *sendfile*，java通过FileChannel.transferTo.api提供对这个系统调用的访问。

通常的数据从file传输到socket的路径有：

1、操作系统**从磁盘读取文件到内核空间**的pagecache。

2、应用程序从**内核空间读取数据到用户空间**的缓存。

3、应用程序将**数据写回内核空间的socket buffer**。

4、操作系统将**socket buffer的数据拷贝到NIC buffer**，数据从NIC被发送到网络。

这样效率显然很低,因为里面涉及 4 次拷贝,2 次系统调用。使用 sendfile 就可以避免返 些重复的拷贝操作,即 **OS 直接将数据从页面缓存发送到网络中**,其中叧需最后一步中的将 数据**拷贝到 NIC** 的缓冲区。

kafka使用了zero copy技术： 数据**只被拷贝到pagecache一次**，每一次consumer请求都会重用，这就要求限制连接到服务器的consumer的数量。

**写message**

- 消息从java堆转入Page Cache(即物理内存)。
- 由异步线程刷盘,消息从pagacache刷入磁盘。

**读message**

  - 消息直接从Page Cache(数据在虚拟内存)转入socket发送出去。
  - 当从Page Cache没有找到相应数据时，此时会产生磁盘IO,从磁
    盘Load消息到Page Cache,然后直接从socket发出去

kafka中**partition leader到follower的消息同步**和**consumer拉取partition中的消息都使用到zero cory**。Cousumer从broker获取数据时直接使用了FileChannel.transferTo()，直接在内核态进行的channel到channel的数据传输。

https://mp.weixin.qq.com/s/ck6yO0xPPyfggdUri_w91g

### **reactor网络线程模型**

![Kafka网络](/Users/yangyibo/Documents/技能点/整理知识点图/技术/Kafka网络.png)

整个网络通信模块基于**Java NIO开发，并采用Reactor模式**，其中包含1个**Acceptor**负责接受客户请求，N个**Processor**负责读写数据，M个**Handler**处理业务逻辑。

首先客户端发送请求全部会先发送给一个Acceptor，broker里面会存在3个线程（默认是3个），这3个线程都是叫做processor，Acceptor不会对客户端的请求做任何的处理，直接封装成一个个socketChannel发送给这些**processor形成一个队列**（socketChannel的队列），发送的方式是轮询，就是先给第一个processor发送，然后再给第二个，第三个，然后又回到第一个。**消费者线程去消费这些socketChannel时，会获取一个个request请求**，这些request请求中就会伴随着数据。

线程池里面默认有8个线程（**KafkaRequestHandler**），这些线程是用来**处理request**的，解析请求，如果request是写请求，就写到磁盘里。读的话返回结果。同时它还包含一个respondQueue，用来存放KafkaRequestHandler处理完Request后返还给客户端的Response。processor会从response中读取响应数据，然后再返回给客户端。这就是Kafka的网络三层架构。

所以如果我们需要对kafka进行增强调优，增加**processor并增加线程池里面的处理线程**，就可以达到效果。request和response那一块部分其实就是起到了一个缓存的效果，是考虑到processor们生成请求太快，线程数不够不能及时处理的问题。所以这就是一个**加强版的reactor网络线程模型**。

##### 小IO问题，批量发送

使用”message set“组合消息。server使用”chunks of messages“写到log。高效的压缩需要将多个消息一起压缩而不是对单个消息单独压缩。

#### **日志压缩（点对点压缩）：**

点对点压缩：producer端：定期的对数据进行压缩，然后发送给服务端。服务端以压缩的形式存储数据，只有当consumer请求数据时进行解压。

1）针对一个topic的partition，压缩使得Kafka至少知道每个key对应的最后一个值。

2）压缩不会重排序消息。

3）消息的offset是不会变的。

4）消息的offset是顺序的。

Kafka支持 GZIP 和 Snappy 压缩协议，

### 总结 Kafka性能好在什么地方？：

* 顺序写

  操作系统每次从磁盘读写数据的时候，需要先**寻址**，也就是先要找到数据在磁盘上的物理位置，然后再进行数据读写，如果是**机械硬盘，寻址就需要较长的时间**（10ms）。

  kafka的设计中，数据其实是存储在磁盘上面，一般来说，会把数据存储在内存上面性能才会好。但是kafka用的是顺序写，追加数据是追加到末尾，磁盘顺序写的性能极高，在磁盘个数一定，转数达到一定的情况下，基本和内存速度一致

  随机写的话是在文件的某个位置修改数据，性能会较低。

* 零拷贝

* reactor网络线程模型
### 分区是不是越多越好呢？

不是，原因如下：

* 分区多造成，缓存多：producer有个参数batch.size，默认是16KB。它会为每个分区缓存消息，producer有个参数batch.size，默认是16KB。它会为每个分区缓存消息，一旦满了就打包将消息批量发出。分区越多需要的缓存越多
* reactor 网络模型，分区越多，consumer 所需要的线程池越大。线程越多，线程切换开销越大
* **文件句柄的开销**：每个分区有属于自己的一个目录有两个文件base_offset.log和base_offset.index。Kafak 的controller和ReplicaManager会为每个broker都保存这两个文件句柄(file handler)。很明显，如果分区数越多，所需要保持打开状态的文件句柄数也就越多，最终可能会突破你的ulimit -n的限制。
* 高可用 选举开销：分区越多，leader选举开销变大

所以需要合理设置分区数  ，通过测试寻找合适的吞吐量 假设总的目标吞吐量是Tt，那么分区数 = Tt / max(Tp, Tc)

Tp表示producer的吞吐量。测试producer通常是很容易的，因为它的逻辑非常简单，就是直接发送消息到Kafka就好了。Tc表示consumer的吞吐量。测试Tc通常与应用的关系更大， 因为Tc的值取决于你拿到消息之后执行什么操作，因此Tc的测试通常也要麻烦一些。

## 集群内部可靠性保证

###**ack确认机制**

#### AR

在Kafka中维护了一个AR列表，包括所有的分区的副本。AR又分为ISR和OSR。
  **AR = ISR + OSR。**
  AR、ISR、OSR、LEO、HW这些信息都被保存在Zookeeper中。

##### 1．ISR
**ISR中的副本都要同步leader**中的数据，只有都同步完成了数据才认为是成功提交了，成功提交之后才能供外界访问。在这个同步的过程中，数据即使已经写入也不能被外界访问，这个过程是通过**LEO-HW**机制来实现的。

##### 2．**OSR**

  OSR内的副本是否同步了leader的数据，不影响数据的提交，**OSR内的follower尽力**的去同步leader，可能数据版本会落后。最开始所有的副本都在ISR中，在kafka工作的过程中，如果**某个副本同步速度**慢于**replica.lag.time.max.ms**指定的阈值，则**被踢出ISR存入OSR**，如果后续**速度恢复可以回到ISR**中。

##### 3．LEO
  **LogEndOffset**：**分区的最新的数据的offset**，当**数据写入leader后**，LEO就立即执行该最新数据。相当于最新数据标识位。

##### 4．**HW**

  **HighWatermark**：只有写入的数据**被同步到所有的ISR中的副本后，数据才认为已提交**，HW更新到该位置，**HW之前的数据才可以被消费者访问**，保证没有同步完成的数据不会被消费者访问到。相当于所有副本同步数据标识位。

  在leader宕机后，**只能从ISR列表中选取新的leader，无论ISR中哪个副本被选为新的leader，它都知道HW之前的数据**，可以保证在切换了leader后，消费者可以继续看到HW之前已经提交的数据。

  所以**LEO代表已经写入的最新数据位置**，而**HW表示已经同步完成的数据**，只有HW之前的数据才能被外界访问。

##### 5．**HW截断机制**

 如果leader宕机，选出了新的leader，而新的leader并不能保证已经完全同步了之前leader的所有数据，**只能保证HW之前的数据是同步过的**，此时**所有的follower都要将数据截断到HW**的位置，再和新的leader同步数据，保证数据一致。

当宕机的leader恢复，发现新的leader中的数据和自己持有的数据不一致，此时**宕机的leader会将自己的数据截断到宕机之前的hw位置**，然后同步新leader的数据。宕机的leader活过来也像follower一样同步数据，来保证数据的一致性。

注意：这只能保证副本之间的数据一致性，并不能保证数据不丢失或者不重复。

6. **LSO**(Last Stable Offset): 对未完成的事务而言，LSO 的值等于**事务中第一条消息的位置**(firstUnstableOffset)，对已完成的事务而言，它的值同 HW 相同

7. **LW**(Low Watermark): 低水位, 代表 AR(分区中的所有副本)集合中最小的 logStartOffset 值

### **生产者可靠性级别**

kafka为生产者提供了如下的三种可靠性级别，通过不同策略保证不同的可靠性保障。
  其实此策略配置的就是leader将成功接收消息信息响应给客户端的时机。 通过request.required.acks参数配置：

1. acks = 0 ；生产者不停向leader发送数据，而不需要leader反馈成功消息。这种模式效率最高，可靠性最低。可能在发送过程中丢失数据，也可能在leader宕机时丢失数据。

2. acks = 1 ；生产者发送数据给leader，leader收到数据后发**送成功信**息，生产者收到后认为发送数据成功，如果一直收不到成功消息，则生产者认为发送数据失败会自动重发数据。当leader宕机时，可能丢失数据。

3. acks = -1；生产者发送数据给leader，**leader收到数据后要等到ISR列表中的所有副本都同步数据完成后**，**才向生产者发送成功消息，如果一只收不到成功消息**，则认为发送数据失败会自动重发数据。这种模式下可靠性很高，但是**当ISR列表中只剩下leader时，当leader宕机让然有可能丢数据。**

    

   此时可以配置min.insync.replicas指定要求**ISR**中至少要有**指定数量的副本**，默认该值为1，需要改为大于等于2的值。这样当生产者发送数据给leader但是发现ISR中只有leader自己时，会收到**异常**表明数据写入失败，此时**无法写入数据**，保证了数据绝对不丢。

   

   虽然不丢但是可能会产生冗余数据，如果在follower同步完成后，broker发送ack之前，leader发生故障，导致没有返回ack给Producer，由于失败重试机制，又会给新选举出来的leader发送数据，造成数据重复。。例如生产者发送数据给leader，leader同步数据给ISR中的follower，同步到一半leader宕机，此时选出新的leader，可能具有部分此次提交的数据，而生产者收到失败消息**重发**数据，新的leader接受数据则数据重复了。

   

## **leader**

leader 是一个partition 标记为 leader信息是存在zookeeper 中。

### leader 选举

Follower都在Zookeeper上设置一个Watch，一旦Leader宕机，其对应的ephemeral znode会自动删除，此时所有Follower都尝试创建该节点，而创建成功者（Zookeeper保证只有一个能创建成功）即是新的Leader，其它Replica即为Follower。

但是该方法会有3个问题： 　　

- split-brain 这是由Zookeeper的特性引起的，虽然Zookeeper能保证所有Watch按顺序触发，但并不能保证同一时刻所有Replica“看”到的状态是一样的，这就可能造成不同Replica的响应不一致
- herd effect 如果宕机的那个Broker上的Partition比较多，会造成多个Watch被触发，造成集群内大量的调整
- Zookeeper负载过重 每个Replica都要为此在Zookeeper上注册一个Watch，当集群规模增加到几千个Partition时Zookeeper负载会过重。

Kafka 0.8.* 的Leader Election方案解决了上述问题，它**在所有broker中选出一个controller**，所有**Partition的Leader选举都由controller决定**。controller会将Leader的改变直接通过**RPC**的方式（比Zookeeper Queue的方式更高效）通知需为此作出响应的Broker。同时controller也负责增删Topic以及Replica的重新分配。

当leader宕机时**会选择ISR中的一个follower成为新的leader**（独占锁选举），如果ISR中的所有副本都宕机，怎么办？

  **策略1**：必须等待ISR列表中的副本活过来才选择其成为leader继续工作。可靠性有保证，但是可用性低，只有最后挂了leader活过来kafka才能恢复

 **策略2：**选择任何一个活过来的副本，成为leader继续工作，此follower可能不在ISR中。可靠性没有保证，任何一个副本活过来就可以继续工作，但是有可能存在数据不一致的情况。

### leader Epoch 策略

为了解决HW可能造成的数据丢失和数据不一致问题，Kafka引入了Leader Epoch机制，在每个副本日志目录下都有一个**leader-epoch-checkpoint**文件，用于保存Leader Epoch信息，其内容示例如下：

```
0 0
1 300
2 500
```

上面每一行为一个Leader Epoch，分为**两部分**，前者**Epoch**，表示**Leader版本号**，是一个单调递增的正整数，每当Leader变更时，都会加1，后者StartOffset，为每一代Leader写入的第一条消息的位移。例如第0代Leader写的第一条消息位移为0，而第1代Leader写的第一条消息位移为300，也意味着第0代Leader在写了0-299号消息后挂了，重新选出了新的Leader。下面我们看下Leader Epoch如何工作：

1. **当副本成为Leader时**：

   当收到生产者发来的第一条消息时，会将新的 **epoch** 和当前 **LEO** 添加到 leader-epoch-checkpoint 文件中。

2. **当副本成为Follower时**：

3. 1. 向Leader 发送 LeaderEpochRequest请求，请求内容中含有**Follower当前本地的最新Epoch**；

   2. Leader将返回给Follower的响应中含有一个 **LastOffset**和 当前的 **Epoch**，其取值规则为：

   3. 1. 若FollowerLastEpoch = LeaderLastEpoch，则取Leader LEO；
      2. 否则，取大于FollowerLastEpoch的第一个Leader Epoch中的StartOffset。

   4. Follower 在拿到返回的LastOffset后，若 LastOffset < 本地 LEO，将截断丢弃 大于 LastOffset 的日志；

   5. Follower开始正常工作，发送Fetch请求；

#### LeaderEpoch replicas =1 时 数据丢失

A作为Leader，A已写入m0、m1两条消息，且HW为2，而B作为Follower，只有消息m0，且HW为1，A、B同时宕机。B重启，被选为Leader，将写入新的LeaderEpoch（1, 1）。B开始工作，收到消息m2时。这是A重启，将作为Follower将发送LeaderEpochRequert（FollowerLastEpoch=0），B返回大于FollowerLastEpoch的第一个LeaderEpoch的StartOffset，即1，小于当前LEO值，所以将发生日志截断，并发送Fetch请求，同步消息m2，避免了消息不一致问题。但是数据m2还是丢失了。***\*这种情况的发送的\**根本原因在于min.insync.replicas的值设置为1**，即没有任何其他副本同步的情况下，就认为m2消息为已提交状态。

LeaderEpoch不能解决min.insync.replicas为1带来的数据丢失问题，但是可以解决其所带来的数据不一致问题。而我们之前所说能解决的数据丢失问题，是指**消息已经成功同步到Follower上**，但因HW未及时更新引起的数据丢失问题。

####  LeaderEpoch 解决宕机导致数据不一致问题

epoch 策略是为了保证 follower 宕机期间 进行了一次或多次 leader 选举情况的下的数据一致性，

A作为Leader，A已写入m0、m1两条消息，且HW为2，而B作为Follower，只有消息m0，且HW为1，A、B同时宕机。B重启，被选为Leader，将写入新的LeaderEpoch（1, 1）。B开始工作，收到消息m2时。这是A重启，将作为Follower将发送LeaderEpochRequert（FollowerLastEpoch=0），B返回大于FollowerLastEpoch的第一个LeaderEpoch的StartOffset，即1，小于当前LEO值，所以将发生日志截断，并发送Fetch请求，同步消息m2，避免了消息不一致问题。



详情：https://mp.weixin.qq.com/s/yIPIABpAzaHJvGoJ6pv0kg

参考：https://mp.weixin.qq.com/s/qpDAMtxRmRytusRlN1vCXw 

参考：http://www.jasongj.com/2015/04/24/KafkaColumn2/

参考：http://www.jasongj.com/2015/06/08/KafkaColumn3/

## controller

在大数据分布式文件系统里面，95%的都是主从式的架构，个别是对等式的架构，比如ElasticSearch。

kafka也是主从式的架构，主节点就叫controller，其余的为从节点，controller是需要和zookeeper进行配合管理整个kafka集群。

###   broker failover过程简介 （Controller）

1. **Controller在Zookeeper注册Watch**，一旦有Broker宕机（这是用宕机代表任何让系统认为其die的情景，包括但不限于机器断电，网络不可用，GC导致的Stop The World，进程crash等），其在Zookeeper对应的znode会自动被删除，Zookeeper会fire Controller注册的watch，Controller读取最新的幸存的Broker

2. Controller决定set_p，该集合**包含了宕机的所有Broker上的所有Partition**

3. 对set_p中的每一个Partition
   　　3.1 从`/brokers/topics/[topic]/partitions/[partition]/state`读取**该Partition当前的ISR**
      　　3.2 决定该Partition的**新Leader**。如果当前**ISR中有至少一个Replica还幸存**，则选择其中一个作为新Leader，新的ISR则包含当前ISR中所有幸存的Replica。否则选择**该Partition中任意一个幸存的Replica作为新的Leader**以及ISR（该场景下可能会有潜在的数据丢失）。如果**该Partition的所有Replica都宕机了**，则将**新的Leader设置为-1**。
      　　　3.3 将新的Leader，ISR和新的`leader_epoch`及`controller_epoch`写入`/brokers/topics/[topic]/partitions/[partition]/state`。注意，该操作只有其version在3.1至3.3的过程中无变化时才会执行，否则跳转到3.1
     
4. 直接通过RPC向set_p相关的Broker发送LeaderAndISRRequest命令。Controller可以在一个RPC操作中发送多个命令从而提高效率。

#### 创建/删除Topic

1. Controller在Zookeeper的`/brokers/topics`节点上注册Watch，一旦某个Topic被创建或删除，则Controller会通过Watch得到新创建/删除的Topic的Partition/Replica分配。
 2. 对于删除Topic操作，Topic工具会将该Topic名字存于`/admin/delete_topics`。若`delete.topic.enable`为true，则Controller注册在`/admin/delete_topics`上的Watch被fire，Controller通过回调向对应的Broker发送StopReplicaRequest；若为false则Controller不会在`/admin/delete_topics`上注册Watch，也就不会对该事件作出反应，此时Topic操作只被记录而不会被执行。
 3. 对于创建Topic操作，Controller从`/brokers/ids`读取当前所有可用的Broker列表，对于set_p中的每一个Partition：
        　　3.1 从分配给该Partition的所有Replica（称为AR）中任选一个可用的Broker作为新的Leader，并将AR设置为新的ISR（因为该Topic是新创建的，所以AR中所有的Replica都没有数据，可认为它们都是同步的，也即都在ISR中，任意一个Replica都可作为Leader）
        　　3.2 将新的Leader和ISR写入`/brokers/topics/[topic]/partitions/[partition]`
4. 直接通过RPC向相关的Broker发送LeaderAndISRRequest。

### Controller Failover

Controller也需要Failover。每个Broker都会在Controller Path (`/controller`)上注册一个Watch。当前Controller失败时，对应的Controller Path会自动消失（因为它是Ephemeral Node），此时该Watch被fire，所有“活”着的Broker都会去竞选成为新的Controller（创建新的Controller Path），但是只会有一个竞选成功（这点由Zookeeper保证）。竞选成功者即为新的Leader，竞选失败者则重新在新的Controller Path上注册Watch。因为[Zookeeper的Watch是一次性的，被fire一次之后即失效](http://zookeeper.apache.org/doc/trunk/zookeeperProgrammers.html#ch_zkWatches)，所以需要重新注册。

###  Consumer Rebalance

对于一个Consumer Group，可能随时都有Consumer加入或者退出这个Consumer Group，Consumer列表的变化势必会引起partition的重新分配。这个为Consumer分配partition的过程就被称为Consumer Rebalance。

 ### 发生 rebalance 的时机

   1. 组成员个数发生变化。例如有新的 `consumer` 实例加入该消费组或者离开组。
   2. 订阅的 `Topic` 个数发生变化。
   3. 订阅 `Topic` 的分区数发生变化。

- **Kafka提供了两种分配策略：Range和RoundRobin**。

  ### Range策略

  range策略的具体步骤如下：

  1. 对一个topic中的partition进行排序
  2. 对消费者按字典进行排序
  3. 然后遍历排序后的partition的方式分配给消费者

  举个例子，比如有两个消费者C0和C1，两个topic(t0,t1)，每个topic有三个分区p(0-2)，

  那么采用Range策略，分配出的结果为：

  - C0: [t0p0, t0p1, t1p0, t1p1]
  - C1: [t0p2, t1p2]

  ### RoundRobin策略

  RoundRobin策略和Range策略类型，唯一的区别就是Range策略分配partition时，是按照topic逐次划分的。而RoundRobin策略则是将所有topic的所有分区一起排序，然后遍历partition分配给消费者。

  因此，采用RoundRobin策略，分配出的结果为：

  - C0: [t0p0, t0p2, t1p1]
  - C1: [t0p1, t1p0, t1p2]

  ### Group Coordinator

  `Group Coordinator` 是一个服务，每个 `Broker`在启动的时候都会启动一个该服务。`Group Coordinator` 的作用是用来存储 **`Group` 的相关 `Meta` 信息**，并将对应 `Partition` 的 **`Offset` 信息记录到 `Kafka` 内置`Topic(__consumer_offsets)` 中**`Kafka` 在 0.9 之前是基于 `Zookeeper` 来存储 `Partition` 的 `Offset` 信息 `(consumers/{group}/offsets/{topic}/{partition})`，因为 `Zookeeper` 并不适用于频繁的写操作，所以在 0.9 之后通过内置 `Topic` 的方式来记录对应 `Partition` 的 `Offset`。

  

  Group Coordinator是负责管理Consumer Group的组件。当一个Consumer希望加入某一个Consumer Group时，它会发送一个请求给Group Coordinator。Group Coordinator负责维护一个Consumer Group中所有的Consumer列表，随着Consumer的加入和退出，Coordinator也会随之更新这个列表。

  **第一个加入Consumer Group的Consumer被称为leader。**

  一旦Consumer Group中的成员发生变化，例如有新的Consumer加入，那么就需要为其分配partition；或者有Consumer退出，那么就需要将其负责消费的partition分配给组内其他成员。因此Consumer Group中的成员发生变化， Group Coordinator就负责发起Consumer Rebalance活动。
  

	

值得注意的是，真正的**Consumer Rebalance行为是由Consumer Group Leader执行的**。Group Leader首先向Coordinator获取Group中的**Consumer成员列表**，然后根据Rebalance策略，将partition分配给Consumer Group中的成员，再将分配结果告知Coordinator。最后，Coordinator将partition分配结果通知给每一个Consumer。**在Consumer Rebalance的过程中，所有的Consumer都不允许消费消息。**
  ### 总结

  1. **Consumer Groups 用于多个Consumer并行消费消息。为了防止两个消费者重复消费一条消息，Kafka不允许同一个Consumer Group中的两个Consumer读取同一个partition。**
  2. **Group Coordinator 用于维护Consumer Group信息。**
  3. **Consumer Rebalance 是为Consumer Group中的Consumer分配partition的过程。一旦一个Consumer Group中的成员发生变化，就会触发Rebalance行为。**
  4. **Group leader 是第一个加入Consumer Group的Consumer，它负责Consumer Rebalance的执行。**
  5. Consumer Rebalance策略主要有Range和Round Robin。

## 事务

Kafka 的事务基本上是配合其幂等机制来实现 **Exactly Once** 语义的。

```java
producer.initTransactions();
try {
    producer.beginTransaction();
    producer.send(record1);
    producer.send(record2);
    producer.commitTransaction();
} catch(ProducerFencedException e) {
    producer.close();
} catch(KafkaException e) {
    producer.abortTransaction();
}
```



Kafka 的事务有事务协调者角色，事务协调者其实就是 Broker 的一部分。

1. 在开始事务的时候，生产者会向事务协调者发起请求表示事务开启，事务协调者会将这个消息记录到特殊的日志-事务日志中，然后生产者再发送真正想要发送的消息，Kafka 会像对待正常消息一样处理这些事务消息，由消费端来过滤这个消息。消费端，有两种策略去读取事务写入的消息，通过"**isolation.level**"来进行配置：

   1. `read_committed`：可以同时读取事务执行过程中的部分写入数据和已经完整提交的事务写入数据；
   2. `read_uncommitted`：完全不等待事务提交，按照offsets order去读取消息，也就是兼容0.11.x版本前Kafka的语义；

   我们必须通过配置consumer端的配置`isolation.level`，来正确使用事务API，通过使用 new Producer API并且对一些unique ID设置`transaction.id`（该配置属于producer端），该unique ID用于提供事务状态的连续性。

2. 然后发送完毕之后生产者会向事务协调者发送提交或者回滚请求，由事务协调者来进行两阶段提交，如果是提交那么会先执行**预提交**，即把**事务的状态置为预提交然后写入事务日志**，然后再向所有事务有关的分区写入一条类似事务结束的消息，这样消费端消费到这个消息的时候就知道事务好了，可以把消息放出来了。

3. 最后协调者会向事务日志中再记一条事务结束信息，至此 Kafka 事务就完成了



## Kafka中副本机制的设计和原理

https://mp.weixin.qq.com/s/yIPIABpAzaHJvGoJ6pv0kg

### **kafka用zookeeper实现的服务类型。**

**1，配置管理**

Topic的配置之所以能动态更新就是基于zookeeper做了一个动态全局配置管理。

**2，负载均衡**

基于zookeeper的消费者，实现了该特性，动态的感知分区变动，将负载使用既定策略分不到消费者身上。

**3，命名服务**

Broker将advertised.port和advertised.host.name，这两个配置发布到zookeeper上的zookeeper的节点上/brokers/ids/BrokerId(broker.id),这个是供生产者，消费者，其它Broker跟其建立连接用的。

**4，分布式通知**

比如分区增加，topic变动，Broker上线下线等均是基于zookeeper来实现的分布式通知。

**5，集群管理和master选举**

我们可以在通过命令行，对kafka集群上的topic partition分布，进行迁移管理，也可以对partition leader选举进行干预。

Master选举，要说有也是违反常规，常规的master选举，是基于临时顺序节点来实现的，序列号最小的作为master。而kafka的**Controller的选举是基于临时节点来**实现的，临时节点创建成功的成为Controller，更像一个**独占锁服务**。

**6，分布式锁**

独占锁，用于Controller的选举。