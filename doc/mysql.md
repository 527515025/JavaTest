# 索引

索引是一个数据结构，索引的算法有btree 和 hash

## mysql 存储引擎

- **Innodb引擎**：Innodb引擎提供了对数据库ACID事务的支持。并且还提供了行级锁和外键的约束。它的设计的目标就是处理大数据容量的数据库系统。
- **MyIASM引擎**(原本Mysql的默认引擎)：不提供事务的支持，也不支持行级锁和外键。
- **MEMORY引擎**：所有的数据都在内存中，数据的处理速度快，但是安全性不高



* **Normal** 普通索引   

  表示普通索引，大多数情况下都可以使用

* **Unique** 唯一索引  

  表示唯一的，不允许重复的索引，如果该字段信息保证不会重复例如身份证号用作索引时，可设置为unique。Unique(要求列唯一)和Primary Key(primary key = unique + not null 列唯一)约束均为列或列集合中提供了唯一性的保证，Primary Key是拥有自动定义的Unique约束，但是每个表中可以有多个Unique约束，但是只能有一个Primary Key约束

* **FullText** 全文索引

  表示全文收索，在检索长文本的时候，效果最好，FULLTEXT 用于搜索很长一篇文章的时候，效果最好。用在比较短的文本，如果就一两行字的，普通的 INDEX 也可以

* **SPATIAL** 空间索引（mysql 对GIS空间数据的支持）

  空间索引是对空间数据类型的字段建立的索引，MYSQL中的空间数据类型有4种，分别是GEOMETRY、POINT、LINESTRING、POLYGON。

  创建空间索引的列，必须将其声明为NOT NULL，空间索引只能在存储引擎为MYISAM的表中创建

### btree索引和hash索引的区别

mysql通过存储引擎取数据，基本上90%的人用的就是InnoDB了，按照实现方式分，InnoDB的索引类型只有两种：BTREE（B树）索引和HASH索引

1、BTREE（B树（可以是多叉树）） {主流使用} https://developer.aliyun.com/ask/281188?spm=a2c6h.13066369.0.0.752a76c5dB6cCA
2、HASH（key,value） 这种方式对范围查询支持得不是很好 。底层的数据结构是哈希表

hash 索引结构的特殊性，其检索效率非常高，索引的检索可以一次定位，不像B-Tree 索引需要从根节点到枝节点，最后才能访问到页节点这样多次的IO访问，所以 **Hash 索引的查询效率要远高于 B-Tree 索引**。
可 能很多人又有疑问了，既然 Hash 索引的效率要比 B-Tree 高很多，为什么大家不都用 Hash 索引而还要使用 B-Tree 索引呢？任何事物都是有两面性的，Hash 索引也一样，虽然 Hash 索引效率高，但是 Hash 索引本身由于其特殊性也带来了很多限制和弊端，主要有以下这些。

（1）**Hash 索引仅仅能满足”=”,”IN”和”<=>”查询**，不能使用范围查询。

由于 Hash 索引比较的是进行 Hash 运算之后的 Hash 值，所以它**只能用于等值的过滤，不能用于基于范围的过滤**，因为经过相应的 Hash 算法处理之后的 Hash 值的大小关系，并不能保证和Hash运算前完全一样。

（2）**Hash 索引无法被用来避免数据的排序操作**。

由于 Hash 索引中存放的是经过 Hash 计算之后的 Hash 值，而且Hash值的大小关系并不一定和 Hash 运算前的键值完全一样，所以数据库无法利用索引的数据来避免任何排序运算；

（3）Hash 索引**不能利用部分索引键查询**。

对于组合索引，Hash 索引在计算 Hash 值的时候是**组合索引键合并后再一起计算 Hash 值**，而**不是单独计算 Hash 值**，所以通过组合索引的前面一个或几个索引键进行查询的时候，Hash 索引也无法被利用。

（4）Hash 索引在任何时候都不能避免表扫描。

前面已经知道，Hash 索引是将索引键通过 Hash 运算之后，将 Hash运算结果的 Hash 值和所对应的行指针信息存放于一个 Hash 表中，由于**不同索引键存在相同 Hash 值**，所以即使取满足某个 Hash 键值的数据的记录条数，也无法从 Hash 索引中直接完成查询，还是要通过访问表中的实际数据进行相应的比较，并得到相应的结果。

（5）Hash 索引遇到大量Hash值相等的情况后性能并不一定就会比B-Tree索引高。

对于**选择性比较低的索引键，如果创建 Hash 索引，那么将会存在大量记录指针信息存于同一个 Hash 值相关联**。这样要**定位某一条记录时就会非常麻烦，会浪费多次表数据的访问**，而造成整体性能低下。

## mysql 为什么用b+ 数作为索引的基础数据结构，而不用二叉树，红黑树，B树

索引为整型自增时，用二叉树会导致，树不平衡，右子树很长，没有左子树，树高度很高， 且浪费空间，因为mysql 默认的预加载数据为16k，查找速度为n

用红黑树，会进行自平衡变色，但是仍然会导致左子树比较长。树高度较高 时间复杂度为log(n)

用b树则树的高度比较低，但是当一块叶子结点的数据查询完后，仍需从跟节点向下再次查询。所以选用B+树

##单列索引、组合索引 （最左原则，执行sql 会优化）

* 在 vc_Name字段 上建立了索引，查询时MYSQL不用扫描整张表，效率有所提高
* 在 vc_City 和 i_Age 分别建立的MySQL单列索引的效率相似。
* 如果分别在 vc_Name,vc_City，i_Age 上建立单列索引，让该表有 3 个单列索引，查询时和上述的组合索引效率，远远低于的组合索引。虽然此时有了三个索引，但 MySQL 只能用到其中的那个它认为似乎是最有效率的单列索引。
* 在什么情况下建立索引呢?一般来说，在WHERE和JOIN中出现的列需要建立索引，但也不完全如此，因为MySQL只对<，<=，=，>，>=，BETWEEN，IN，以及使用 LIKE 谓词时，只有前方一致的匹配才能用到索引。
* 实际的情况，需要控制IN查询的范围。
  1. IN 的条件过多，会导致索引失效，走索引扫描
  2. IN 的条件过多，返回的数据会很多，可能会导致应用堆内内存溢出。

## 

**MVCC：Multiversion concurrency control**，多版本并发控制

多版本控制: 指的是一种提高并发的技术。最早的数据库系统，只有读读之间可以并发，读写，写读，写写都要阻塞。引入多版本之后，只有写写之间相互阻塞，其他三种操作都可以并行，这样大幅度提高了 InnoDB 的并发度。

在内部实现中，与 Postgres 在数据行上实现多版本不同，InnoDB 是在 undolog 中实现的，通过 undolog 可以找回数据的历史版本。找回的数据历史版本可以提供给用户读(按照隔离级别的定义，有些读请求只能看到比较老的数据版本)，也可以在回滚的时候覆盖数据页上的数据。在 InnoDB 内部中，会记录一个全局的活跃读写事务数组，其主要用来判断事务的可见性。

一致性视图只会在 RR 与 RC 下才会生成，对于 RR 来说，一致性视图会在**第一个查询语句**的时候生成。而对于 RC 来说，每个查询语句都会重新生成视图。

## **当前读与快照读**

MySQL 使用 MVCC 机制，可以读取之前版本数据。这些旧版本记录不会且也无法再去修改，就像快照一样。所以我们将这种查询称为**快照读**。

当然并不是所有查询都是快照读，select .... for update/ in share mode 这类加锁查询只会查询当前记录最新版本数据。我们将这种查询称为当前读。



# sql优化小技巧

* in 的内容为子查询时 使用 **EXISTS** 代替 **IN**
  
    使用 **EXISTS** ，那么只要查到一行数据满足条件就会终止 查询，不用像使用 **IN** 时一样扫描全表。在这一点上 **NOT** **EXISTS** 也一样。
    
    当 IN 的参数是子查询时，数据库首先会执行子查询，然后将结果存 储在一张临时的工作表里（内联视图），然后扫描整个视图，很多情况下很消耗资源。使用 EXISTS 的话，数据库不会生成临时的工作表。
    
    ```sql
    -- 慢
    SELECT *
      FROM Class_A
    WHERE id IN (SELECT id
                   FROM Class_B);
                   
    -- 快
    SELECT *
      FROM Class_A A
    WHERE EXISTS (SELECT *
                    FROM Class_B B
                  WHERE A.id = B.id);
    ```
    
    


# mysql 与 oracle

* oracle

由于其诞生早、结构严谨、高可用、高性能等特点，使其在传统数据库应用中大杀四方，金融、通信、能源、运输、零售、制造等各个行业的大型公司基本都是用了Oracle，早些年的时候，世界500强几乎100%都是Oracle的用户

主要在传统行业的数据化业务中，比如：银行、金融这样的对可用性、健壮性、安全性、实时性要求极高的业务；零售、物流这样对海量数据存储分析要求很高的业务。此外，高新制造业如芯片厂也基本都离不开Oracle；电商也有很多使用者，如京东（正在投奔Oracle）、阿里巴巴（计划去Oracle化）

* MySQL

MySQL的最初的核心思想，主要是开源、简便易用.
MySQL基本是生于互联网，长于互联网。其应用实例也大都集中于互联网方向，MySQL的高并发存取能力并不比大型数据库差，同时价格便宜，安装使用简便快捷，深受广大互联网公司的喜爱。

##为什么需要MyCat？

虽然云计算时代，传统数据库存在着先天性的弊端，但是NoSQL数据库又无法将其替代。如果传统数据易于扩展，可切分，就可以避免单机（单库）的性能缺陷。

MyCat的目标就是：低成本地将现有的单机数据库和应用平滑迁移到“云”端，解决数据存储和业务规模迅速增长情况下的数据瓶颈问题。2014年MyCat首次在上海的《中华架构师》大会上对外宣讲引发围观，更多的人参与进来，随后越来越多的项目采用了MyCat。

MyCat截至到2015年4月，保守估计已经有超过60个项目在使用，主要应用在电信领域、互联网项目，大部分是交易和管理系统，少量是信息系统。比较大的系统中，数据规模单表单月30亿。

##MyCat是什么？

从定义和分类来看，它是一个开源的分布式数据库系统，是一个实现了MySQL协议的服务器，前端用户可以把它看作是一个数据库代理，用MySQL客户端工具和命令行访问，而其后端可以用MySQL原生协议与多个MySQL服务器通信，也可以用JDBC协议与大多数主流数据库服务器通信，其核心功能是分表分库，即将一个大表水平分割为N个小表，存储在后端MySQL服务器里或者其他数据库里。

MyCat发展到目前的版本，已经不是一个单纯的MySQL代理了，它的后端可以支持MySQL、SQL Server、Oracle、DB2、PostgreSQL等主流数据库，也支持MongoDB这种新型NoSQL方式的存储，未来还会支持更多类型的存储。而在最终用户看来，无论是那种存储方式，在MyCat里，都是一个传统的数据库表，支持标准的SQL语句进行数据的操作，这样一来，对前端业务系统来说，可以大幅降低开发难度，提升开发速度

## 





#操作

#MYSQL

##mysql报错

ERROR 1045 (28000): Access denied for user 'root'@'localhost' (using password: YES)

为密码错误

_:6zvQ=KtIa<

##Mysql 变量

mysql中变量不用事前申明，在用的时候直接用“@变量名”使用就可以了。

第一种用法：set @num=1; 或set @num:=1; //这里要使用变量来保存数据，直接使用@num变量
第二种用法：select @num:=1; 或 select @num:=字段名 from 表名 where ……

##Mysql为日期增加一个时间间隔：date_add()

set @dt = now();

select date_add(@dt, interval 1 day);   - 加1天

select date_add(@dt, interval 1 hour);   -加1小时

select date_add(@dt, interval 1 minute);    - 加1分钟

select date_add(@dt, interval 1 second); -加1秒

select date_add(@dt, interval 1 microsecond);-加1毫秒

select  (@dt, interval 1 week);-加1周

select date_add(@dt, interval 1 month);-加1月

select date_add(@dt, interval 1 quarter);-加1季

select date_add(@dt, interval 1 year);-加1年

###示例

MySQL adddate(), addtime()函数，可以用date_add() 来替代。下面是date_add() 实现addtime() 功能示例：

mysql> set @dt = '2009-09-09 12:12:33';

mysql>

mysql> select date_add(@dt, interval '01:15:30' hour_second);-加上1小时15分30秒

 date_add(@dt, interval '01:15:30' hour_second)

 

结果:2009-09-09 13:28:03

##Mysql 所在机器的时区影响时间函数

mysql 所在机器的时区不正确会导致  

date_add  函数失效 （计算错误）
FROM_UNIXTIME 失效 （where中使用该函数只能大于等于，不能小于）

##mysql 使用正则

mysql sql 语句使用 REGEXP (正则匹配关键字)需要在 数据库连接字符串中添加如下配置

```
&useUnicode=true&characterEncoding=UTF-8
```

#oracle

打开cmd输入sqlplus

```
sqlplus
sys/manager as sysdba，以超级管理员的权限登录数据库
create user c##用户名 identified by 密码; 
授权
为刚创建的用户解锁：alter user c##用户名 account unlock;
授予新用户创建权限：grant create session to  c##用户名 ;
授予新用户数据库管理员权限：grant dba to c##用户名;
授予用户其它权限：
   GRANT CREATE USER,DROP USER,ALTER USER ,
            CREATE  ANY  VIEW , DROP ANY VIEW,
            EXP_FULL_DATABASE,IMP_FULL_DATABASE, 
            DBA,CONNECT,RESOURCE,CREATE SESSION  TO  c##用户名;  

```

到此，用户就创建成功了，到https://localhost:5500/em登录试试，登录成功。不过不能以管理员权限登录。 不用输入容器名

打开监控链接
https://jingyan.baidu.com/article/03b2f78c7a0ab75ea237ae33.html

##注意

oracle中 表名和 字段需要加 "" 

```
SELECT * from "person" where "create_time" > to_date('2017-10-25 11:45:20','yyyy-mm-dd HH:mi:ss');
```

##树状结构 mysql 查询

树状应该记录 父节点 和 子节点，然后循环递归查询。

将查询结果映射到java bean ，java bean应该 也是 嵌套设计
这样就可以将查询出来的 树 映射出来

```
public class Tree {
	private Integer id;
	private String description;
	private String severity;
	private List<Tree> childTree;
```

使用mysql 函数

```
DROP FUNCTION IF EXISTS getSituationTreeLst;  

delimiter //
CREATE FUNCTION `getSituationTreeLst`(rootId INT)
	RETURNS varchar(1000)
    BEGIN
    DECLARE sTemp VARCHAR(1000);
    DECLARE sTempChd VARCHAR(1000);
		SET sTemp = '$';
		SET sTempChd = cast(rootId as CHAR);
		WHILE sTempChd is not null DO
			SET sTemp = concat(sTemp,',',sTempChd);
			
			SELECT
		group_concat(s2.id)  INTO sTempChd
		FROM
			situation s2
		WHERE
			FIND_IN_SET(sTempChd , s2.story)
		END WHILE;
		RETURN sTemp;
END //
```





