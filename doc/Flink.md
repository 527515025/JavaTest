 ## Flink

https://ververica.cn/developers/apache-flink-basic-zero-iii-datastream-api-programming/

![image-20200827175824098](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827175824098.png)

处理数据密集型，和任务密集型

![image-20200827154028921](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827154028921.png)

处理单元（计算资源）增加，协作困难 



试卷题目划分（数据并行度），试卷划分（处理小组处理一部分试卷） （计算并行性）  

A组需要取试卷 source  节点

B transformation 节点

C组，统计总分，任务  sink 节点

![image-20200827154916234](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827154916234.png)

声明式编程之需要告诉机器完成什么样的任务，不需要 像命令式编程，那样需要传递详细的一步一步完成任务的过程

声明式：相对比较繁琐

sql 也是声明式的编程方式

Flink api：偏向声明式



新老api 对比，

DataStream APi 和 TableAPi 转化为同一层面

![image-20200827160137972](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827160137972.png)

 数据逐个 *2  取合

![image-20200827161443975](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827161443975.png)

必须最后提交，2，3，4 过程不会触发处理数据的过程，只是在绘制DAG 拓扑图，只有在第5步提交后，才会根据图提交到集群进行执行。

![image-20200827161849220](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827161849220.png)



![image-20200827164801537](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827164801537.png)

每一个dataStream 对象在调用transfrom 方法时都会产生一个新的转换，对应一个新的算子，并添加到DAG图中。

API在调用transfrom 方法的时候会产生一个新的对象，我们可以在新的对象上进行调用方法计算，

转化方法决定了操作。

![image-20200827165205776](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827165205776.png)

shuffle ：数据分区，方便数据查找，提高效率。

流处理，partition 数据分区

![image-20200827165428950](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827165428950.png)

keyBy 不是底层的数据分区策略。类似一个操作，rescale 是本地的轮流分配，不会跨网络的分配。

自定义单播（对于一个数据只能指定一个下游，不能复制多分，分给多个下游）

![image-20200827165709566](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827165709566.png)

可以没有sink 必须有 source。 关于 CSV source 可以监控文件目录，也可以只是读取一次某个文件。需要关注自己的souce是持续关注的还是仅一次性读取。

sink 可以面对静态数据（kafka，追加）也可以面向动态数据（mysql 可修改）

![image-20200827171400845](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827171400845.png)

有状态的 计算和 无状态计算

无状态计算（纯函数：输出结果只和输入数据有关，与之前计算结果和外部状态无关）

有状态的（记录之前的计算结果，并应用到新的计算中)

1. 记录上次输出结果，并将结果当作下一次计算的一个输入参数 （spark Streaming 的聚合操作，好处，可以重用无状态算子）

2. 算子每次到来新的数据时会考虑已有的状态和新输入的数据进行计算（有状态算子，flink）

![image-20200827172048299](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827172048299.png)

将原生语言的 List 、Map 等转化使用Flink 的的原语

 分区状态：keyed State 

1.可以把已有的状态，按照逻辑提供的分区，分成不同状态的块。每一块的计算和状态是绑定在一起的。不同的key值之间，读写和计算都是隔离的。每个key只需管理好自己的计算逻辑和状态就可以，不用取考虑其他的key值所对应的状态和计算逻辑。

![image-20200827172556435](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827172556435.png)

RichFunction 的不同，是有自己的生命周期，可以将自己的逻辑与生命周期方法（open close 等等）结合。

如果state是新的则只有当前状态，如果是从系统中获取的则有以前的状态。操作时不用考虑并发问题。

#### 累加示例

![image-20200827172954147](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827172954147.png)

print 方法相当于sink print 方式。因为key 是按照奇偶数划分的分区所以后面并行度设置为2进行计算

![image-20200827172927190](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827172927190.png)

processing time  机器时间，真实的时间

eventTIme 数据中包含的时间 （有乱序问题，但是因为数据存在所以可以重现处理计算结果）

 ![image-20200827173848163](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827173848163.png)

乱序问题（区间范围的乱序）。根据数据中的时间戳，进行粗粒度划分，缓解乱序。化解掉初步的乱序

通过watermark 来解决乱序，标记当前时间段最后的时间。预示着可以处理watermark标记时间之前的数据计算了。

总体来言，processing TIme 递增，eventTime 存在一定程序的乱序

![image-20200827174333623](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827174333623.png)

flink 中内置了一些生成watermark的一些方法。比如currentWatermark方法为当前时间-时间段 生成 watermark

![image-20200827174726431](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827174726431.png)



![image-20200827175428317](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827175428317.png)





## RUNTIME

![image-20200827175754919](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827175754919.png)

![image-20200827175720122](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827175720122.png)



![image-20200827180016736](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827180016736.png)

![image-20200827180133784](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827180133784.png)

![image-20200827180327971](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827180327971.png)

![image-20200827180508739](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827180508739.png) 

job master 可以有一个或多个 通过dispatcher 管理。 管理作业

resourceManager 用来进行资源管理管理所有worker 节点。服务于所有的作业

rest Server 用来处理 client （web 和 console） 的请求，可以用来 提交作业，查询作业状态、停止作业等。

worker ：TaskExecutor 执行任务的容器

![image-20200827180811314](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827180811314.png)

![image-20200827180829892](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827180829892.png)

分布式快照：

checkpoint 为出错服务。（容错）

Rpc endpoint 用于作业的维护和升级和迁移

scheduler 负责5个指责

![ ](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827181038353.png)

作业流程 created -》 running -〉finished

如果有作业异常，则如 failing ，如果异常不可恢复则进入 failed 状态，如果可恢复则进入 restarting 状态，如果重启次数没有超过上限则进行重启。

canceled是手动触发

suspengded：意味着 jobmaster 终止了

![image-20200827181450471](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827181450471.png)

eager 服务于流作业

lazy from 服务于批作业，开始只调度 source作业，数据存够一批后，进行sink  （节省资源）

pipelined 意味着有 flink 边的计算节点子在同一个 piplined region中 ，上下游节点进行流式计算。（也可以节省调度花费时间，一部分计算同时调度，可以更深度的资源优化）

![image-20200827182040601](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827182040601.png)

![image-20200827182127980](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827182127980.png)

restartPipeline 不仅重启失败的任务节点，还会重启所有的下游节点。

restartAll 失败后，会重启作业所有的任务节点

单点重启，（可以，短期内可以解决）

![image-20200827182501203](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827182501203.png)

![image-20200827182704237](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827182704237.png)

![image-20200827182906391](/Users/yangyibo/Library/Application Support/typora-user-images/image-20200827182906391.png)