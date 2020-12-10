match_phrase是分词的，text也是分词的。match_phrase的分词结果必须在text字段分词中都包含，而且顺序必须相同，而且必须都是连续的。

## collapse 和 agg 区别

 agg 不能分页 只能size + after 滚动

collapse是和from,size,query等结构是同级并列的情况,耗时上也非常可观,它并不是在整个索引库里面进行折叠,而是在召回结果拿到以后再进行的折叠

collapse的 top hits 永远是精确的

字段折叠只在 top hits 层执行，不需要每次都在完整的结果集上对为每个折叠主键计算实际的 doc values 值，只对 top hits 这小部分数据操作就可以，和 term agg 相比要节省很多内存。因为只在 top hits 上进行折叠，所以相比组合聚合的方式，速度要快很多。



折叠 top docs 不需要使用全局序列（global ordinals）来转换 string，相比 agg 这也节省了很多内存。

分页成为可能，和常规搜索一样，具有相同的局限，先获取 from+size 的内容，再合并。

search_after 和 scroll 暂未实现，不过具备可行性。折叠只影响搜索结果，不影响聚合，搜索结果的 total 是所有的命中纪录数，去重的结果数未知（无法计算）。





term 查询会查找我们指定的精确值。term 查询是简单的，它接受一个字段名以及我们希望查找的数值。

想要类似mysql中如下sql语句的查询操作：

SELECT document FROM products WHERE price = 20;



我们会使用过滤器（filters）。过滤器很重要，因为它们执行速度非常快，不会计算相关度（直接跳过了整个评分阶段）而且很容易被缓存。如下： 使用 constant_score 查询以非评分模式来执行 term 查询并以一作为统一评分。



match  匹配查询接受文本/数字/日期类型，分析它们，并构造查询。
1）匹配查询的类型为boolean。 这意味着分析所提供的文本，并且分析过程从提供的文本构造一个布尔查询，
可以将运算符标志设置为或以控制布尔子句（默认为或）；
2）文本分析取决于mapping中设定的analyzer（中文分词，我们默认选择ik分词器）；
3） fuzziness——模糊性允许基于被查询的字段的类型进行模糊匹配；
4）”operator”: “and”——匹配与操作（默认或操作）；
5） “minimum_should_match”: “75%”——这让我们可以指定必须匹配的词项数用来表示一个文档是否相关。





## 搜索请求处理

> 节点处理，由谁分发，就由谁交付

1. 请求节点2
2. 节点2客串协调者，将查询请求发送到索引中的每一个分片的副本
3. 没个分片在本地进行查询（每个分片本地都是一个物理的Lucene实例，迷你搜素引擎）然后将结果交给Node 2。Node 2进行排序和聚合、折叠
4. 根据上一步结果，Node 2 知道需要获取哪些文档，并向相关的分片发送多个GET请求。
5. 每个分片loads documents然后将他们返回给Node 2
6. Node 2将搜索结果交付给客户端





[万字长文，理解Elasticsearch和面试总结](https://mp.weixin.qq.com/s/G6cM4w-xikGBONa2zMWAeQ)

