match_phrase是分词的，text也是分词的。match_phrase的分词结果必须在text字段分词中都包含，而且顺序必须相同，而且必须都是连续的。

## collapse 和 agg 区别

 agg 不能分页 只能size + after 滚动

collapse是和from,size,query等结构是同级并列的情况,耗时上也非常可观,它并不是在整个索引库里面进行折叠,而是在召回结果拿到以后再进行的折叠

collapse的 top hits 永远是精确的

字段折叠只在 top hits 层执行，不需要每次都在完整的结果集上对为每个折叠主键计算实际的 doc values 值，只对 top hits 这小部分数据操作就可以，和 term agg 相比要节省很多内存。因为只在 top hits 上进行折叠，所以相比组合聚合的方式，速度要快很多。



折叠 top docs 不需要使用全局序列（global ordinals）来转换 string，相比 agg 这也节省了很多内存。

分页成为可能，和常规搜索一样，具有相同的局限，先获取 from+size 的内容，再合并。

search_after 和 scroll 暂未实现，不过具备可行性。折叠只影响搜索结果，不影响聚合，搜索结果的 total 是所有的命中纪录数，去重的结果数未知（无法计算）。