**zookeeper在分布式集群的作用**

**1，数据发布与订阅（配置中心）**

发布与订阅模型，即所谓的配置中心，顾名思义就是讲发布者将数据发布到zk节点上，共订阅者动态获取数据，实现配置的集中式管理和动态更新。例如，全局的配置信息，服务服务框架的地址列表就非常适合使用。

1. 分布式环境下，配置文件管理和同步是一个常见问题

2. - 一个集群中，所有节点的配置信息是一致的，比如 Hadoop 集群、集群中的数据库配置信息等全局配置
   - 对配置文件修改后，希望能够快速同步到各个节点上。

3. 配置管理可交由 ZooKeeper 实现

4. - 可将配置信息写入 ZooKeeper 上的一个 Znode
   - 各个节点监听这个 Znode
   - 一旦 Znode 中的数据被修改，ZooKeeper 将通知各个节点

**2，统一命名服务(Naming Service)**

在分布式环境中，经常需要对**服务**进行统一命名，假如有一个服务部署了**2两个副**本，直接调用具体的服务肯定有些不合适，因为我们并不清楚哪个服务可以更快的处理我们的请求，这时候我们可以将这三个服务进行统一命名，然后其内部再去负载。这样就可以调用最优的那个服务了。例如Dubbo

常见的是发布者将自己的地址列表写到zookeeper的节点上，然后订阅者可以从固定名称的节点获取地址列表，链接到发布者进行相关通讯。

**3，分布式通知/协调**

这个利用的是zookeeper的watcher注册和异步通知机制，能够很好的实现分布式环境中不同系统间的通知与协调，实现对数据变更的实时处理。

**4，统一集群管理与Master选举**

集群管理，比如在线率，节点上线下线通知这些。Master选举可以使用临时顺序节点来实现。

1. 分布式环境中，**实时掌握**每个节点的状态是必要的，比如我们要知道集群中各机器状态、收集各个机器的运行时状态数据、服务器动态上下线等。

2. 交由 ZooKeeper 实现的方式

3. - 可将节点信息写入 ZooKeeper 上的一个 Znode
   - 监听这个 Znode 可获取它的实时状态变化
   - 典型应用：HBase 中 Master 状态监控和选举。

4. **Master选举**

   在分布式环境中，相同的业务应用分布在不同的机器上，有些业务逻辑（例如一些耗时的计算，网络I/O处理），往往只需要让**整个集群中的某一台机器**进行执行，其余机器可以共享这个结果，这样可以大大减少重复劳动，提高性能，于是这个master选举便是这种场景下的碰到的主要问题。

   利用 **Zookeeper 的强一致性**，能够**很好的保证在分布式高并发情况下节点的创建一定是全局唯一的**，即：同时有多个客户端请求创建 `/currentMaster` 节点，最终一定只有一个客户端请求能够创建成功。Zookeeper 通过这种节点唯一的特性，可以创建一个 Master 节点，其他客户端 Watcher 监控当前 Master 是否存活，一旦 Master 挂了，其他机器再创建这样的一个 Master 节点，用来重新选举。

**5，负载均衡**
 即软件负载均衡。最典型的是消息中间件的生产、消费者负载均衡。

**6，分布式锁**

分布式锁，这个主要得益于zookeeper数据的强一致性，利用的是**临时节点。锁服务分为两类，一个是独占锁，另一个是控制时序**。

独占，是指所有的客户端都来获取这把锁，最终只能有一个获取到。用的是临时节点。

控制时序，所有来获取锁的客户端，都会被安排得到锁，只不过要有个顺序。实际上是某个节点下的临时顺序子节点来实现的。

**7，分布式队列**

一种是FIFO，这个就是使用临时顺序节点实现的，和分布式锁服务控制时序一样。

第二种是等待队列的成员聚齐之后的才同意按序执行。实际上，是在队列的节点里首先创建一个/queue/num节点，并且赋值队列的大小。这样我们可以通过监控队列节点子节点的变动来感知队列是否已满或者条件已经满足执行的需要。这种，应用场景是有条件执行的任务，条件齐备了之后任务才能执行。



zookeeper的节点类型：持久节点（PERSISTENT），临时节点（EPHEMERAL）

持久顺序节点（PERSISTENT_SEQUENTIAL），临时顺序节点（EPHEMERAL_SEQUENTIAL）

Zkclient对zookeeper的listener实现总共有四种：IZkStateListener(监听会话状态，是否进行了超时重连等)，IZkDataListener(监听节点数据的变动)，IZkChildListener(监听子节点的变动)，IZkConnection (监听链接)。

**IZkStateListener**

主要作用是**会话超时**的监控，需要在处理函数里重新注册临时节点。主要方法两个:

handleStateChanged,zookeeper的链接状态改变的时候调用

handleNewSession,与zookeeper的会话超时，导致断开并新连接建立的时候会调用。需要在此方法中实现临时节点的注册。在kafka中主要有以下四个实现:

* **ZKSessionExpireListener**：所属对象为每个消费者，会话超时，会导致消费者进行再平衡
* **SessionExpirationListener**：所属对象Broker的Controller对象，会话超时会导致Crontroller再选举
* **ZkSessionExpireListener**：所属对象是给ZookeeperTopicEventWatcher对象，会话超时事件触发后都会重新将ZkTopicEventListener和"/brokers/topics"的目进行绑定。ZkTopicEventListener负责会监控该目录下的子节点，也即topic的增删。会在创建带topic过滤器的流的时候用到

# **认识zookeeper**

Zookeeper是Apache开源的一个分布式框架，它主要为分布式应用提供协调服务。

ZooKeeper 是用于维护配置信息，命名，提供分布式同步和提供组服务的集中式服务。所有这些类型的服务都以某种形式被分布式应用程序使用。主要**负责存储和管理大家都关心的数据**，一旦这些数据的状态发生变化，Zookeeper就会通知那些注册在Zookeeper上的服务。简单来讲就是**zookeeper=文件系统+通知机制**

###  设计目标

- **简单的数据结构** ：Zookeeper 使得分布式程序能够通过一个共享的树形结构的名字空间来进行相互协调，即Zookeeper 服务器内存中的数据模型由一系列被称为`ZNode`的数据节点组成，**Zookeeper 将全量的数据存储在内存中，以此来提高服务器吞吐、减少延迟的目的**。（但是内存限制了能够存储的容量不太大，此限制也是保持 znode 中存储的数据量较小的进一步原因）
- **可以构建集群** ：Zookeeper 集群通常由一组机器构成，组成 Zookeeper 集群的每台机器都会在内存中维护当前服务器状态，并且每台机器之间都相互通信。
- **顺序访问** ：对于来自客户端的每个更新请求，Zookeeper 都会**分配一个全局唯一的递增编号**，这个编号反映了**所有事务操作**的先后顺序。
- **高性能** ：Zookeeper 和 Redis 一样全量数据存储在内存中，100% 读请求压测 QPS 12-13W

## ZooKeeper 语义保证

1. **最终一致性：**client不论连接到哪个Server，展示给它都是同一个视图，这是zookeeper最重要的性能。
2. **可靠性：**具有简单、健壮、良好的性能，如果消息m被到一台服务器接受，那么它将被所有的服务器接受。
3. **实时性：**Zookeeper保证客户端将在一个时间间隔范围内获得服务器的更新信息，或者服务器失效的信息。但由于网络延时等原因，Zookeeper不能保证两个客户端能同时得到刚更新的数据，如果需要最新数据，应该在读数据之前调用sync()接口。
4. **等待无关（wait-free）：**慢的或者失效的client不得干预快速的client的请求，使得每个client都能有效的等待。
5. **原子性：**更新只能成功或者失败，没有中间状态。
6. **顺序性：**包括全局有序和偏序两种：全局有序是指如果在一台服务器上消息a在消息b前发布，则在所有Server上消息a都将在消息b前被发布；偏序是指如果一个消息b在消息a后被同一个发送者发布，a必将排在b前面。

## ZooKeeper工作机制/原理

ZooKeeper 从设计模式角度来理解：就是一个基于**观察者模式**设计的**分布式服务管理**框架，它负责**存储和管理大家都关心的数据**，然后**接受观察者的注**册，一旦这些数据的状态发生变化，ZK 就将**负责通知已经在 ZK 上注册的那些观察者做出相应的反**应，从而**实现集群中类似 Master/Slave 管理模**式。

Zookeeper的核心是**原子广播**，这个机制保证了各个Server之间的同步。实现这个机制的协议叫做**Zab协议**（ZooKeeper Atomic Broadcast protocol）。Zab协议有两种模式，它们分别是**恢复模式（Recovery选主）**和**广播模式（Broadcast同步）**。

当服务启动或者在领导者崩溃后，**Zab就进入了恢复模式**，当领导者被选举出来，且大多数Server完成了和leader的状态同步以后，恢复模式就结束了。**状态同步保证了leader和Server具有相同的系统状态**。

为了保证**事务的顺序一致性**，zookeeper采用了**递增的事务id号**（zxid）来标识事务。所有的**提议（proposal**）都在被提出的时候加上了zxid。

实现中zxid是一个**64位的数**字，它**高32位是epoch用来标识leader关系是否改变**，每次一个leader被选出来，它都会有一个**新的epoch**，标识当前属于那个leader的统治时期。低32位用于递增计数。

## Zookeeper的节点

Zookeeper的数据结构与**Unix文件系统**很类似，整体上可以看作是**一棵树**，与Unix文件系统不同的是**Zookeeper的每个节点都可以存放数据**，**每个节点称作一个ZNode**，默认存储**`1MB`的数据，每个ZNode都可以通过其路径唯一标识**。

![zookeeper-node](/Users/yangyibo/Documents/技能点/整理知识点图/技术/zookeeper-node.png)

#### ZNode特点：

1. 每个子目录项如NameService都被称作为znode，这个**znode是被它所在的路径唯一标识**，如Server1这个znode的标识为/NameService/Server1。
2. znode可以有子节点目录，并且每个znode可以存储数据，默认存**储`1MB`的数据**。注意EPHEMERAL（**临时的）类型的目录节点不能有子节点**目录。
3. znode是有版本的（version），每个**znode中存储的数据可以有多个版本**，也就是一个访问路径中可以**存储多份数据，version号自动增加。**  
4. znode可以被**监控**，包括这个目录节点中存储的数据的修改，子节点目录的变化等，**一旦变化可以通知设置监控的客户端**，这个是Zookeeper的核心特性，Zookeeper的很多功能都是基于这个特性实现的。
5. ZXID：每次**对Zookeeper的状态的改变都会产生一个zxid**（ZooKeeper Transaction Id），**zxid是全局有序的**，如果zxid1小于zxid2，则zxid1在zxid2之前发生。

### ZNode类型（可组合，比如Sequence-Persistent节点）

- **持久化目录节点 Persistent**：客户端与Zookeeper**断开连接后，该节点依旧存在**，一旦被创建，便不会意外丢失，即使服务器全部重启也依然存在。每个 Persist 节点即可包含数据，也可包含子节点。
- **临时目录节点 Ephemeral**：客户端与Zookeeper**断开连接后，该节点被删除**，（在创建它的客户端与服务器间的 Session 结束时自动被删除。服务器重启会导致 Session 结束，因此 Ephemeral 类型的 znode 此时也会自动删除。
- **Sequence 顺序节点节点：** ZooKeeper还允许用户为每个节点添加一个特殊的属性,SEQUENTIAL 也被称为顺序节点，例如“**Ephemeral-Sequence** 和  **Persistent-Sequence**”，**创建出的节点名在指定的名称之后带有10位10进制数的序号**。多个客户端创建同一名称的节点时，都能创建成功，只是序号不同。
- **Non-sequence 节点：** **多个客户**端同时创建同一 Non-sequence 节点时，**只有一个可创建成功，其它匀失败**。并且创建出的**节点名称与创建时指定的节点名完全一样**。

说明：创建ZNode时**设置顺序标识**，ZNode名称后会附加一个值，**顺序号是一个单调递增的计数器，由父节点维护**。

### stat结构体，

Zookeeper 的每个 ZNode 上都会存储数据，Zookeeper 都会为其维护一个叫作 **Stat** 的数据结构，其中包含数据更改、ACL更改的版本号、时间戳等。

- **czxid**-创建节点的事务 **zxid（时间戳）**：每次修改 ZooKeeper 状态都会收到一个 **zxid 形式的时间戳，也就是 ZooKeeper 事务 ID**。事务 ID 是 **ZooKeeper 中所有修改总的次序**。每个修改都有唯一的 zxid，如果 zxid1 小于 zxid2，那么 zxid1 在 zxid2 之前发生。
- **mzxid**：znode **最后更新**的事务 zxid
- **pZxid**：znode 最后更新的**子节点 zxid**
- **ctime** ：znode **被创建的**毫秒数(从 1970 年开始)
- **mtime**：znode **最后修改**的毫秒数(从 1970 年开始)
- **cversion**：znode 子节点**变化号**，znode **子节点修改次数**
- **data**version：znode **数据变化号**
- **acl**Version：znode **访问控制列表**的变化号
- **ephemeral**Owner：如果是临时节点，这个是 znode **拥有者的 session id**。如果不是临时节点则是 0
- **dataLength**：znode 的**数据长度**
- **numChildren**：znode **子节点数量**

#### 会话（Session）

Session 指的是 ZooKeeper 服务器与客户端会话。

**在 ZooKeeper 中，一个客户端连接是指客户端和服务器之间的一个 TCP 长连接**。客户端启动的时候，首先会与服务器建立一个 TCP 连接，从第一次连接建立开始，客户端会话的生命周期也开始了。通过这个连接，客户端能够**通过心跳检测**与服务器保持**有效**的会话，也能够向 Zookeeper 服务器**发送请求并接受响应**，同时还能够通过**该连接接收来自服务器的 Watch 事件**通知。

Session 作为会话实体，用来代表客户端会话，其包括 4 个属性：

- **SessionID**，用来全局唯一识别会话；
- **TimeOut**，会话超时事件。客户端在创造 Session 实例的时候，会设置一个会话超时的时间。当由于服务器压力太大、网络故障或是客户端主动断开连接等各种原因导致客户端连接断开时，只要在 sessionTimeout 规定的时间内能够重新连接上集群中任意一台服务器，那么之前创建的会话仍然有效；
- **TickTime**，下次会话超时时间点；
- **isClosing**，当服务端如果检测到会话超时失效了，会通过设置这个属性将会话关闭。

Client和Zookeeper集群建立连接，整个session状态变化如图所示：

![zookeeper-Session](/Users/yangyibo/Documents/技能点/整理知识点图/技术/zookeeper-Session.png)

如果Client因为**Timeout**和Zookeeper Server失去连接，client处在**CONNECTING**（**图节点2**）状态，会自动尝试再去连接Server，如果在session有效期内再次成功连接到某个Server，则回到CONNECTED（**图节点3**）状态。

**注意：**如果因为网络状态不好，client和Server失去联系，**client会停留在当前状态，会尝试主动再次连接Zookeeper Server**。client**不能宣称自己的session expired**，**session expired是由Zookeeper Server**来决定的，**client可以选择自己主动关闭session**。

#### 权限控制 ACL

Zookeeper 采用 ACL（Access Control Lists）策略来进行权限控制，类似于 UNIX 文件系统的权限控制。Zookeeper 定义了如下 5 种权限：

- **CREATE**: 创建子节点的权限
- **READ**: 获取节点数据和子节点列表的权限
- **WRITE**: 更新节点数据的权限
- **DELETE**: 删除子节点的权限
- **ADMIN**: 设置节点ACL的权限

其中尤其需要注意的是，CREATE 和 DELETE 这两种权限都是**针对子节点**的权限控制。

## 事件监听（Watcher）

Zookeeper watch是一种监听通知机制。Zookeeper**所有的读操作**getData(), getChildren()和 exists()都可以设置监视(watch)，监视事件可以理解为**一次性**的触发器

Watch的三个关键点：

**One-time trigger（一次性触发）**

Zookeeper 允许用户在指定节点上注册一些 **Watcher**，当 Znode 发生变化时，将**触发并删除**一个 watch。监视事件可以理解为**一次性的触发**器。当 watch 被触发时**客户端会收到一个数据包**，指示 znode 已经被修改。如果客户端和 ZooKeeper 服务器之间的连接中断，客户端将收到本地通知。**该机制是 Zookeeper 实现分布式协调服务的重要特性**

3.6.0中的新增功能：客户端还可以在 znode 上设置永久性的递归监视，这些监视在触发时不会删除，并且会以递归方式触发已注册 znode 以及所有子 znode 的更改。

**Sent to the client（发送至客户端）**

监视事件是通过 socket **异步发送至监视者**的，Zookeeper 本身提供了**保序性**(ordering guarantee)：即**客户端只有首先看到了监视事件**后，才会**感知到它所设置监视的 znode 发生了变化**。网络延迟或者其他因素可能导致不同的客户端在不同的时刻感知某一监视事件，但是**不同的客户端所看到的一切具有一致的顺序**。

**The data for which the watch was set（被设置 watch 的数据）**

Zookeeper 维护了两条监视链表：**数据监视**和**子节点监视**(data watches and child watches) **getData() 和exists()设置数据监视**，**getChildren()设置子节点监视**。

znode 节点本身具有不同的改变方式，可能改变的是znode也可能改变的是子节点。**`getData()` 和 `exists()` 设置数据监视，`getChildren()` 设置子节点监视**。或者，你也可以想象 Zookeeper 设置的不同监视返回不同的数据，**`getData()` 和 `exists()`返回 znode 节点的相关信息**，而 `getChildren()` 返回**子节点列表**。

因此， `setData()` 会触发设置在某一节点上所设置的数据监视(假定数据设置成功)，而一次成功**的 `create()` 操**作则会触发当前节点上所设置的**数据监视**以及**父节点的子节点**监视。一次成功**的 `delete()` 操作**将会触发**当前节点的数据监视和子节点监视事件，同时也会触**发该节点**父节点的 `child watch`**。

**exists()丢失监听**

Zookeeper 中的**监视是轻量级的，容易设置、维护和分发**。当**客户端与 Zookeeper 服务器端失去联系时**，客户端并不会收到监视事件的通知，只有当**客户端重新连接**后，若在必要的情况下，以前注册的监视会重新被注册并触发，对于开发人员来说这通常是透明的。只有一种情况会导致监视事件的丢失，即：通过 **`exists()`** 设置了某个 znode 节点的监视，但是如果某个客户端在此 znode **节点被创建和删除的时间间隔内与 zookeeper 服务器失去了联系**，该客户端即使稍后重新连接 zookeepe r服务器后也得不到事件通知。

### 监听器的原理

Watcher 机制包括三个角色：客户端线程、客户端的 WatchManager 以及 ZooKeeper 服务器。Watcher 机制就是这三个角色之间的交互，整个过程分为注册、存储和通知三个步骤：

1. 客户端向 ZooKeeper 服务器注册一个 Watcher 监听；
2. 把这个监听信息存储到客户端的 WatchManager 中；
3. 当 ZooKeeper 中的节点发生变化时，会通知客户端，客户端会调用相应 Watcher 对象中的回调方法。

过程如下：

![zookeeper监听](/Users/yangyibo/Documents/技能点/整理知识点图/技术/zookeeper监听.png)

1. 客户端创建一个Main()线程
2. 在Main()线程中**创建两个线程**，一个负责**网络连接通信（connect），一个负责监听（listener）**
3. 通过connect线程将注册的监听事件发送给Zookeeper
4. 将注册的**监听事件添加到Zookeeper的注册监听器列表中**
5. Zookeeper**监听到有数据或路径发生变化时，把这条消息发送给Listener线程**
6. **Listener线程内部调用process()方法**

## 一致性

Zookeeper是一个**高效的、可扩展的**服务，read和write操作都被设计为快速的，read比write操作更快。

* **顺序一致性（Sequential Consistency）：**从**一个客户端**来的更新请求会被**顺序执行**。

* **原子性（Atomicity）：**更新要么成功要么失败，没有部分成功的情况。

* **唯一的系统镜像（Single System Image）：**无论客户端连接到哪个Server，看到**系统镜像是一致**的。

* **可靠性（Reliability）：**更新一旦有效，**持续有效**，直到被覆盖。

* **时间线（Timeliness）：**保证在**一定的时间内**各个客户端看到的**系统信息是一致**的。

### ZAB协议

Zab协议 的全称是 **Zookeeper Atomic Broadcast** （Zookeeper原子广播）。**Zookeeper 是通过 Zab 协议来保证分布式事务的最终一致性**。实现了**主从模式**的系统架构来**保持集群中各个副本之间的数据一致性**。

根据ZAB协议，所有的**写操作都必须通过Leader**完成，Leader写入**本地日志后**再复制到所有的**Follower**节点。

一旦Leader节点无法工作，ZAB协议能够自动从Follower节点中重新选出一个合适的替代者，即新的Leader，该过程即为领导选举。该领导选举过程，是ZAB协议中最为重要和复杂的过程。

### ZAB协议处理请求

Zookeeper Server接收到一次request，如果本身是follower服务器，则会转发给leader，Leader执行请求并通过Transaction的形式广播这次执行。

### 两段提交协议（a two-phase commit）决定 Transaction是否commit执行？

- Leader给所有的follower发送一个PROPOSAL消息。
- 一个follower接收到这次PROPOSAL消息，写到磁盘，发送给leader一个ACK消息，告知已经收到。
- 当Leader收到法定人数（quorum，即半数以上）的follower的ACK时候，发送commit消息执行。

### Zab协议保证：

- 如果leader以T1和T2的顺序广播，那么所有的Server必须先执行T1，再执行T2。

- 如果任意一个Server以T1、T2的顺序commit执行，其他所有的Server也必须以T1、T2的顺序执行。

  - 在新的leader广播Transaction之前，先前Leader commit的Transaction都会先执行。
  - 在任意时刻，都不会有2个Server同时有法定人数（quorum）的支持者。
    这里的quorum是一半以上的Server数目，确切的说是有投票权力的Server（不包括Observer）。

  

“两段提交协议”最大的问题是如果Leader发送了PROPOSAL消息后crash或暂时失去连接，会导致整个集群处在一种不确定的状态（follower不知道该放弃这次提交还是执行提交）。Zookeeper这时会选出新的leader，请求处理也会移到新的leader上，不同的leader由不同的epoch标识。切换Leader时，需要解决下面两个问题：

ZAB协议保证了在Leader选举的过程中，已经被Commit的数据不会丢失，未被Commit的数据对客户端不可见。

### Commit过的数据不丢失

Leader在保存消息后进行广播，收到超过一半follower的ack 确认，此时消息**已经被commint 客户端可见**此消息，但是仍有部分 follower 没有同步到此消息。 此时leader 宕机。则新Leader必须保证这个事务也必须commit。

选举出新的 leader 之后，各个follower 会将自己最大的zxid 发送给 新的leader 。此时新的 leader 会将Follower的zxid与自身zxid之间 的所有被Commit过的消息同步给Follower。此时zxid 小于 新leader 的 follower会根据广播，写入和ack确认。此时便保证了已经被Commit的数据不会丢失。

### 未Commit过的消息对客户端不可见

Leader在保存消息后进行广播，收到少于一半follower的ack 确认。此时**消息没有被commint 客户端不可见**此消息。但是有部分 follower 已经同步到此消息了。 此时leader 宕机。则新Leader必须必须丢弃这个proposal。



###  写操作

#### leader写
通过Leader进行写操作，主要分为五步：

1. 客户端向Leader发起写请求
2. Leader将写请求以Proposal的形式发给所有Follower并等待ACK
3. Follower收到Leader的Proposal后返回ACK
4. Leader得到过半数的ACK（Leader对自己默认有一个ACK）后向所有的Follower和Observer发送Commmit
5. Leader将处理结果返回给客户端

这里要注意

- Leader并不需要得到Observer的ACK，即Observer无投票权

- Leader不需要得到所有Follower的ACK，只要收到过半的ACK即可，同时Leader本身对自己有一个ACK。上图中有4个Follower，只需其中两个返回ACK即可，因为(2+1) / (4+1) > 1/2

- Observer虽然无投票权，但仍须同步Leader的数据从而在处理读请求时可以返回尽可能新的数据

#### 写Follower/Observer

- Follower/Observer均可接受写请求，但不能直接处理，而需要将写请求转发给Leader处理

- 除了多了一步请求转发，其它流程与直接写Leader无任何区别

### 读操作

Leader/Follower/Observer都可直接处理读请求，从本地内存中读取数据并返回给客户端即可。由于处理读请求不需要服务器之间的交互，Follower/Observer越多，整体可处理的读请求量越大，也即读性能越好。  

### 版本控制

有了 Watcher 机制，就可以实现分布式协调/通知了，假设有这样的场景，两个客户端同时对 B接点 进行写入操作，这两个客户端就会存在竞争关系，通常需要对 B 进行**加锁**操作，ZK 通过 version 版本号来控制实现**乐观锁**中的“**写入校验**”机制。

ZNode 会维护一个叫作 **Stat** 的数据结构，**Stat** 中记录了这个 **ZNode 的三个数据版本**，分别是 **version**（当前ZNode的版本）、**cversion**（当前ZNode 子节点的版本）和 **aversion**（当前ZNode的ACL版本）。

## Zookeeper集群

Zookeeper集群虽然没有指定Master和Slave。但是，在Zookeeper工作时，会通过**内部选举机制产生一个Leader节点，其他节点为Follower或者是Observer**。

![zookeeper集群](/Users/yangyibo/Documents/技能点/整理知识点图/技术/zookeeper集群.png)

### zookeeper 角色

Zookeeper集群是一个基于主从复制的高可用集群，每个服务器承担如下三种角色中的一种

* **Leader：** 一个Zookeeper集群同一时间只会有一个实际工作的Leader，它会发起并维护与各Follwer及Observer间的心跳。所有的写操作必须要通过Leader完成再由Leader将写操作广播给其它服务器。
* **Follower：** 一个Zookeeper集群可能同时存在多个Follower，它会响应Leader的心跳。Follower可直接处理并返回客户端的读请求，同时会将写请求转发给Leader处理，并且负责在Leader处理写请求时对请求进行投票。
* **Observer：** 角色与Follower类似，但是无投票权

#### **Leader主要功能：**

1. 恢复数据；

2. 维持与follower的心跳，接收follower请求并判断follower的请求消息类型；

3. follower的消息类型主要有PING消息（**follower的心跳信息**）、REQUEST消息（**follower发送的提议信息，包括写请求及同步请求**）、ACK消息（**follower的对提议的回复，超过半数的follower通过，则commit该提议；**）、REVALIDATE消息（**REVALIDATE消息是用来延长SESSION有效时间**），根据不同的消息类型，进行不同的处理。   

#### Follower主要功能

1. 向Leader发送请求（PING消息、REQUEST消息、ACK消息、REVALIDATE消息）；

2. 接收Leader消息并进行处理；

3. 接收Client的请求，如果为写请求，发送给Leader进行投票；

4. 返回Client结果。

##### Follower循环处理如下来自Leader的消息：

1. PING消息：心跳消息
2. PROPOSAL消息：Leader发起的提案，要求Follower投票
3. COMMIT消息：服务器端最新一次提案的信息
4. UPTODATE消息：表明同步完成
5. REVALIDATE消息：根据Leader的REVALIDATE结果，关闭待revalidate的session还是允许其接受消息
6. SYNC消息：返回SYNC结果到客户端，这个消息最初由客户端发起，用来强制得到最新的更新

### server 的状态

- LOOKING：寻找Leader状态,当前Server不知道leader是谁
- LEADING：领导者状态，表明当前服务器角色是 Leader
- FOLLOWING：跟随者状态，leader已经选举出来 ,当前Server与leader同步。
- OBSERVING：观察者状态，observer的行为在大多数情况下与follower完全一致，但是他们不参加选举和投票，而仅仅接受(observing)选举和投票的结果

被声明为**Observer**的节点，**不参与选举过程**，也不参与写操作的**”过半写成功**“策略。

**过半写成功策略**：Leader节点接收到写请求后，这个Leader会**将写请求广播给各个server**，各个server会将该写请求加入待写队列，并向Leader发送成功信息，当**Leader收到一半以上的成功**消息后，说明**该写操作可以执行**。Leader会向各个server发送提交消息，各个server收到消息后开始写。

**Follower和Observer只提供数据的读操作**，当他们**接收的写请求时，会将该请求转发给Leader节**点（只有leader进行写操作）。

**集群中只要有半数以上的节点存活，Zookeeper集群就能正常服务**。因此Zookeeper集群适合安装**奇数台**机器。

## 选举机制

当leader崩溃或者leader失去大多数的follower，这时候zk进入恢复模式，恢复模式需要重新选举出一个新的leader，让所有的Server都恢复到一个正确的状态。

Zk的**选举算法**有两种：一种是基于**basic paxos**实现的，另外一种是基于**fast paxos**算法实现的。

Paxos是一个**共识（consensus）算法**，用于解决分布式共识问题。其目的是在一个分布式系统中如何就**某一个值（proposal）** 达成一致。

系统默认的选举算法为fast paxos。

### 选票数据结构

每个服务器在进行领导选举时，会发送如下关键信息

- **logicClock** 每个服务器会维护一个自增的整数，名为logicClock，它表示这是该服务器发起的**第多少轮投票**
- **state** 当前服务器的状态
- **self_id** 当前服务器的myid
- **self_zxid** 当**前服务器**上所保存的数据的最大zxid
- **vote_id** 被推举的服务器的myid
- **vote_zxid** 被**推举的服务器**上所**保存的数据的最大zxid**

### 投票流程 fast paxos

**自增选举轮次**：Zookeeper规定所有有效的投票都必须在同一轮次中。每个服务器在开始新一轮投票时，会先对自己维护的logicClock进行自增操作。

**初始化选票：**每个服务器在**广播**自己的选票前，会将自己的投票箱**清空**。该投票箱记录了所收到的选票。例：服务器2投票给服务器3，服务器3投票给服务器1，服务器1投票给自己，则服务器1的投票箱为(2, 3), (3, 1), (1, 1)。票箱中只会记录**每一投票者的最后一票**，如投票者更新自己的选票，则其它服务器收到该**新选票后会在自己票箱中更新该服务器的选票**。

**发送初始化选票：** 每个服务器最开始都是通过广播把票**投给自己**。

**接收外部投票：** 服务器会尝试**从其它服务器获取投票**，并**记入自己的投票箱**内。如果无法获取任何外部投票，则会确认自己是否与集群中其它服务器保持着有效连接。如果是，则再次发送自己的投票；如果否，则马上与之建立连接。

**判断选举轮次：**收到外部投票后，首先会根据投票信息中所包含的**logicClock（第几轮投票）**来进行不同处理

- 外部投票的logicClock**大于**自己的logicClock。说明该服务器的选举轮次落后于其它服务器的选举轮次，**立即清空自己的投票箱并将自己的logicClock更新为收到的logicClock**，然后再对比自己之前的投票与收到的投票以确定是否需要变更自己的投票，最终再次将自己的投票广播出去。
- 外部投票的logicClock**小于**自己的logicClock。当前服务器直接忽略收到的该投票，继续处理其他投票。
- 外部投票的logickClock与自己的相等。当时进行**选票PK**。

**选票PK：** 选票PK是基于(self_id, self_zxid)与(vote_id, **vote_zxid**)的对比

- 若logicClock一致，则**对比二者的vote_zxid（选择大的）**，若外部投票的vote_zxid比较大，则将自己的票中的vote_zxid与vote_myid更新为收到的票中的vote_zxid与vote_myid并**广播出去**，另外将收到的票及自己更新后的票放入自己的票箱。如果票箱内已存在(self_myid, self_zxid)相同的选票，则直接覆盖
- 若二者vote_zxid一致，**则比较二者的vote_myid（选择大的）**，若外部投票的vote_myid比较大，则将自己的票中的vote_myid更新为收到的票中的vote_myid并广播出去，另外将收到的票及自己更新后的票放入自己的票箱

**统计选票：**如果已经确定有**过半服务器**认可了自己的投票（可能是更新后的投票，所以zookeeper部署奇数台），则终止投票。否则继续接收其它服务器的投票。

**更新服务器状态：**投票终止后，服务器开始更新自身状态。若过半的票投给了自己，则将自己的服务器状态更新为LEADING，否则将自己的状态更新为FOLLOWING

### 集群启动领导选举

集群有三个服务器1，2，3。当集群刚启动时，所有服务器的 logicClock都为1，zxid都为0。

(1)**初始投票给自己：**各服务器初始化后，都投票给**自己**，并将自己的一票存入自己的票箱。(1, 1, 0)第一位数代表投出该选票的服务器的logicClock，第二位数代表被推荐的服务器的myid，第三位代表被推荐的服务器的最大的zxid。由于该步骤中所有选票都投给自己，所以第二位的myid即是自己的myid，第三位的zxid即是自己的zxid。此时各服务器自己的票箱中只有自己投给自己的一票。

(2)**更新选票：**服务器收到外部投票后，进行选票PK，相应更新自己的选票并**广播出去**，并将合适的选票存入自己的票箱。

服务器1收到服务器2的选票（1, 2, 0）和服务器3的选票（1, 3, 0）后，由于所有的logicClock都相等，所有的zxid都相等，因此选择myid最大的更新自己的选票为（1, 3, 0），并将自己的票箱全部清空，再将服务器3的选票与自己的选票存入自己的票箱，接着将自己更新后的选票广播出去。此时服务器1票箱内的选票为(1, 3)，(3, 3)。

同理，服务器2收到服务器3的选票后也将自己的选票更新为（1, 3, 0）并存入票箱然后广播。此时服务器2票箱内的选票为(2, 3)，(3, ,3)。

服务器3根据上述规则，无须更新选票，自身的票箱内选票仍为（3, 3）。服务器1与服务器2更新后的选票广播出去后，由于三个服务器最新选票都相同，最后三者的票箱内都包含三张投给服务器3的选票。

（3）**根据选票确定角色：**根据上述选票，三个服务器一致认为此时服务器3应该是Leader。因此服务器1和2都更改自身为FOLLOWING状态，而服务器3更改自身为LEADING状态。之后Leader发起并维护与Follower间的心跳。

#### 当Follower重启

Follower重启，或者发生网络分区（脑裂）后找不到Leader，会进入**LOOKING**状态并发起新的一轮投票。当服务器3收到服务器1的投票后，将自己的**LEADING**状态以及选票返回给服务器1。服务器2收到服务器1的投票后，将自己的状态FOLLOWING及选票返回给服务器1。此时服务器1**知道服务器3是Leader**，并且通过服务器2与服务器3的选票可以确定服务器3确实得到了超过**半数**的选票。因此服务器1更改自己为FOLLOWING状态。

#### 当Leader重启

**（1）Follower发起新投票：**Leader（服务器3）宕机后，Follower（服务器1和2）发现Leader不工作了，因此进入**LOOKING**状态并发起新的一轮投票，并且都将票投给自己。

**（2）选票PK和广播：**服务器1和2根据外部投票确定是否要更新自身的选票。这里有两种情况

- 服务器1和2的**zxid（最大数据）**相同。例如在服务器3宕机前服务器1与2完全与之同步。此时选票的更新主要取决于**myid（服务器id）**的大小，投票给大的
- 服务器1和2的zxid不同。在旧Leader宕机之前，其所主导的写操作，只需过半服务器确认即可，而不需所有服务器确认。所以服务器1和2可能一个与旧Leader同步（即zxid与之相同）另一个不同步（即zxid比之小）。此时选票的更新主要取决于谁的**zxid**较大

**（3）选出新Leader：**经过上一步选票PK后，服务器1与服务器2均将选票投给服务器1，因此服务器2成为Follower，而服务器1成为新的Leader并维护与服务器2的心跳。

**（4）旧Leader恢复后称为FOLLOWING：**旧的Leader服务器3恢复后，进入**LOOKING**状态并发起新一轮领导选举，并将选票投给自己。此时服务器1会将自己的LEADING状态及选票（3, 1, zxid）返回给服务器3，而服务器2将自己的FOLLOWING状态及选票（3, 1, zxid）返回给服务器3。此时，服务器3了解到Leader为服务器1，且根据选票了解到服务器1确实得到过半服务器的选票，因此自己进入FOLLOWING状态。

#### **basic paxos** 算法

1. 选举线程由当前Server发起选举的线程担任，其主要功能是对投票结果进行统计，并选出推荐的Server；

2. 选举线程首先向所有Server发起一次询问（包括自己）；

3. 选举线程收到回复后，验证是否是自己发起的询问（验证zxid是否一致），然后获取对方的id（myid），并存储到当前询问对象列表中，最后获取对方提议的leader相关信息（id,zxid），并将这些信息存储到当次选举的投票记录表中；

4. 收到所有Server回复以后，就计算出**zxid最大的那个Serve**r，并将这个Server相关信息设置成下一次要投票的Server；

5. 线程将当前zxid最大的Server设置为当前Server要推荐的Leader，如果此时获胜的Server获得n/2 + 1的Server票数，设置当前推荐的leader为获胜的Server，将根据获胜的Server相关信息设置自己的状态，否则，继续这个过程，直到leader被选举出来。

### 选举总结：

fast paxos 是在选举初始时，各个server都提议自己称为leaer。basic paxos 是

通过流程分析我们可以得出：要使Leader获得多数Server的支持，则Server总数必须是奇数2n+1，且存活的Server的数目不得少于n+1.

每个Server启动后都会重复以上流程。在恢复模式下，如果是刚从崩溃状态恢复的或者刚启动的server还会从磁盘快照中恢复数据和会话信息，zk会记录事务日志并定期进行快照，方便在恢复时进行状态恢复。













## Zookeeper 应用场景抉择

在**粗粒度分布式锁，分布式选主，主备高可用切换等，不需要高TPS** 支持的场景下有不可替代的作用，而这些需求往往多集中在大数据、离线任务等相关的业务领域，因为大数据领域，讲究分割数据集，并且大部分时间分任务多进程/线程并行处理这些数据集，但是总是有一些点上需要将这些任务和进程统一协调，这时候就是 ZooKeeper 发挥巨大作用的用武之地。

但是在**交易场景交易链路**上，在主**业务数据存取**，**大规模服务发现（服务注册）**、**大规模健康监测**等方面有**天然的短板**，应该竭力避免在这些场景下引入 ZooKeeper，在阿里巴巴的生产实践中，应用对ZooKeeper申请使用的时候要进行严格的场景、容量、SLA需求的评估。

因为ZooKeeper 的**写并不是可扩展**的，**不可以通过加节点解决水平扩展性**问题。一个实践中可以考虑的方法是想办法梳理业务，垂直划分业务域，将其划分到多个 ZooKeeper 注册中心。但是因自己提供的服务能力不足要业务按照技术的指挥棒配合划分治理业务，是不会被允许的。不能因为注册中心自身的原因（能力不足）破坏了服务的可连通性，妨碍业务服务的发展。

而且zookeeper 的长链接也容易出现闪断，闪断后，期间的业务操作是否需要重试等机制也需要异常捕获业务控制。

详情看：https://mp.weixin.qq.com/s/CbYJaPa8sMqyXZIEeN8P3w





- ZooKeeper 本身就是一个分布式程序（只要半数以上节点存活，ZooKeeper 就能正常服务）。

- 为了保证高可用，最好是以集群形态来部署 ZooKeeper，这样只要集群中大部分机器是可用的（能够容忍一定的机器故障），那么 ZooKeeper 本身仍然是可用的。

- **ZooKeeper 将数据保存在内存中，这也就保证了高吞吐量和低延迟**（但是内存限制了能够存储的容量不太大，此限制也是保持 znode 中存储的数据量较小的进一步原因）。

- **ZooKeeper 是高性能的。在“读”多于“写”的应用程序中尤其的高性能，因为“写”会导致所有的服务器间同步状态。**（“读”多于“写”是协调服务的典型场景。）

- ZooKeeper 底层其实只提供了两个功能：

- - 管理（存储、读取）用户程序提交的数据
  - 为用户程序提交数据节点监听服务