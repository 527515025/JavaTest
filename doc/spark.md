

### 什么是Apache Spark？

Apache Spark是一个开源集群运算框架，最初由加州大学柏克莱分校AMPLab开发。

### Apache Spark的特点是什么？
*  开源
* 高性能，基于内存进行数据处理的
*  易用，支持R、Scala、Java等
*  通用，支持批处理、流处理、机器学习等多种场景

spark Sql  + data frames  处理结构化数据的能力
stream 流式数据模块

### Apache Spark的生态是怎样的？

![spark1](/Users/yangyibo/Desktop/spark1.jpeg)

上层模块，根据场景来设计：

* Spark SQL + DataFrames 处理结构化数据的
* Streaming 支持流式场景的模块
* MLlib 机器学习模块
* GraphX 图计算模块

底层模块，Spark Core API，所有的场景模块都是基于这一核心进行设计的，也是Spark最原始的模块，多语言支持便由它来提供。

![spark2](/Users/yangyibo/Desktop/spark2.jpeg)

在Spark中，比较核心的有三块
1. Driver
*  运行作业主函数
*  创建SparkContext（SparkContext的作用是：帮助程序使用spark提供的一些功能）
2. Cluster Manager（目前为止支持以下四种，主要的作用有两个：分配资源、把Driver要执行的任务分配到对应的Executor）
* Standalone
* Mesos
* K8S
* Yarn
3. Executor（执行任务的实际进程）
* 执行task
* 处理rdd一个分区的数据

整个执行流程：
写好程序 -> 在Driver端跑起来了 -> 通过Cluster Manager申请到了资源 -> 将任务调度到了Executor上
Cluster Manager 分配资源把Driver任务分配到 Executor，Executor（执行任务的实际进程）在每个task执行的过程中去处理 rdd 一个分区的数据

## 其他基本概念
• Spark Application
	• 用户写的Spark应用程序
• Job
	• 一个application会生成多个job
	• Job直接可以有依赖关系
	• 可以并行执行
• Stage
	• 一个job会有多个Stage
	• Stage之间可以有依赖关系
	• 可以并行执行
• Task
	• 任务执行的最小单元
![spark3](/Users/yangyibo/Desktop/spark3.jpeg)

### SparkContext & SparkSession

- SparkContext是Spark2.0以前应用运行入口
	- 一个进程只能有一个
  - jar包管理
  - 事件处理
  - 资源管理
  - 运行时环境管理
  - job stage管理
  - dag
  - DAG schedule
- SparkSession是Spark2.0之后的应用运行入口
	- 支持SparkContext的特性
	- 支持操作dataframes操作



##RDD

![spark4](/Users/yangyibo/Desktop/spark4.jpeg)

RDD是Spark最核心概念，最基础数据结构，全称：弹性分布式数据集（Resilient Distributed Datasets）
其特点是：

- 基于内存
- 延时计算（只有当我们执行一个action API时才会进行计算）
- 容错（相较于map rudece 的优势）
- 只读
- 只分区（实现分布式计算）

![spark5](/Users/yangyibo/Desktop/spark5.jpeg)

rdd 分为4个分区，有四个分区如果有4个Executor 节点后 后就可以启动四个task 并行的去处理这四个分区，可以直接的理解时间成本为原来的4分之一


创建RDD的两种方式
1. 在driver中对已有集合进行并行化
2. 基于外部数据源生成

RDD操作类型
• Transformations 基于已有的RDD生成新的RDD
• Actions 触发生成job开始运算

## Spark SQL（DataFrames）
Spark SQL
Spark SQL是一个用来处理结构化数据的Spark组件。它提供了一个叫做DataFrames的可编程抽象数据模型，并可以视为一个分布式的SQL查询引擎。（通过sparkSql 组建执行sql 操作各种各样格式的数据源)

![spark6](/Users/yangyibo/Desktop/spark6.jpeg)
### DataFrames
DataFrame是类似于关系表的分布式数据集。其特点是：
• 分布式 
• 只读
• SQL查询
• Schema （表结构）
• Catalyst优化）

问题： Schema 的检测在运行时进行。比如说我们写完了作业，提交到测试环境后才能发现问题。

### DataSet
DataSet 是结合DataFrame和RDD优势的分布式数据集（可以在编译时就进行检验的能力，同时兼备DataFrame 的特性包括查询优化和 Schema）
• Scale & Java（目前只支持这两种语言）
• 类型安全（RDD）
• 关系型模型（DF）
• 查询优化（DF）

![spark8](/Users/yangyibo/Desktop/spark8.jpeg)

### Structured Streaming（SS）
SS是构建于Spark SQL的流处理引擎。特性是：
• 可扩展
• 容错
• 无限增长的表格（把数据流视为无限增长的表格）
• SQL （可以使用sql 和 datafrom）
• DataFrame
![spark9](/Users/yangyibo/Desktop/spark9.jpeg)

### ML Pipeline
ML pipelines 是基于DataFrame抽象的，用于构建机器学习工作流的API
• DataFrame：表示数据集
• Transformer：DataFrame转换的算法
• Estimatior：作用于DataFrame生成Transformer
• Pipeline：ML工作流
• Parameter：指定Transformer和Estimator的参数

![spark10](/Users/yangyibo/Desktop/spark10.jpeg)

# spark Sql

* 灵活易用
* 功能强大
* 生态丰富
* spark演进的基础模块


求平均数

用户写的 sql 或dataFrame 优化的过程是有spark框架进行的，不需要用户参与的

sql -词法解析，语法解析变成抽象语法树，翻译成逻辑执行计划，逻辑执行计划通过catalog拿到表的原信息（Schema、字段等信息），逻辑执行计划经过优化器，进行各种个样的基于规则的等价转换优化，然后变成一个或多个的物理执行计划（物理执行计划是可以真正被执行的）。在spark中会经过一个cpu代价的优化器，多个物理执行计划的话会去选择执行代价最小的执行物理计划，然后包在一个rdd中，提交到spark集群上执行

catalyst sql 执行计划的优化器，通过rule和oprate 等价进行优化



## Delta lake

事物支持（事物日志，数据变更，记录事物日志）
数据校验 （校验上游数据的Schema ，Schema发生变化，根据配置，确定是否修改Schema）
数据演化


