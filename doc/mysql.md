#单列索引、组合索引

* 在 vc_Name字段 上建立了索引，查询时MYSQL不用扫描整张表，效率有所提高
* 在 vc_City 和 i_Age 分别建立的MySQL单列索引的效率相似。
* 如果分别在 vc_Name,vc_City，i_Age 上建立单列索引，让该表有 3 个单列索引，查询时和上述的组合索引效率，远远低于的组合索引。虽然此时有了三个索引，但 MySQL 只能用到其中的那个它认为似乎是最有效率的单列索引。
* 在什么情况下建立索引呢?一般来说，在WHERE和JOIN中出现的列需要建立索引，但也不完全如此，因为MySQL只对<，<=，=，>，>=，BETWEEN，IN，以及某些时候的LIKE才会使用索引


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

#单列索引、组合索引

* 在 vc_Name字段 上建立了索引，查询时MYSQL不用扫描整张表，效率有所提高
* 在 vc_City 和 i_Age 分别建立的MySQL单列索引的效率相似。
* 如果分别在 vc_Name,vc_City，i_Age 上建立单列索引，让该表有 3 个单列索引，查询时和上述的组合索引效率，远远低于的组合索引。虽然此时有了三个索引，但 MySQL 只能用到其中的那个它认为似乎是最有效率的单列索引。
* 

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

#