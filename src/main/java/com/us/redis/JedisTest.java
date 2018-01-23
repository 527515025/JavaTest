package com.us.redis;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * Created by yangyibo on 2018/1/12.
 */
public class JedisTest {

    public static void main(String[] args) {
//        testString();
//        testMap();
//        testList();
//        testSet();
//        testSortSet();
        subscribe();
        publish();
    }

    public static Jedis connect() {
//        Jedis jedis = new Jedis("192.168.100.76", 6379);
//        jedis.auth("admin");
//        //ping通显示PONG
//        System.out.println(jedis.ping());//去ping我们redis的主机所在ip和端口

        //从redis 连接池中获取
        Jedis jedis = JedisPoolTest.getJedis();
        return jedis;
    }

    /**
     * 操作string类型的key
     * string 是最基本的类型,而且 string 类型是二进制安全的。意思是 redis 的 string 可以 包含任何数据。
     * 比如 jpg 图片或者序列化的对象。从内部实现来看其实 string 可以看作 byte数组,最大上限是 1G 字节
     */
    public static void testString() {
        Jedis jedis = connect();
        jedis.set("name", "abel");
        // set 多个key and value
        jedis.mset("name", "abel", "age", "23", "qq", "123244232");
        //age  + 1
        jedis.incr("age");
        System.out.println(jedis.get("name"));
        // delete key
        jedis.del("name");
        jedis.close();
    }

    /**
     * 操作map
     * hash 是一个 string 类型的 field 和 value 的映射表。添加,删除操作都是 O(1)(平均)。
     * hash 特别适合用于存储对象。相对于将对象的每个字段存成单个 string 类型。将一个对象存储在 hash 类型中会占用更少的内存,并且可以更方便的存取整个对象。
     */
    public static void testMap() {
        Map<String, String> map = new HashMap<>();
        map.put("address", "上海");
        map.put("name", "abel");
        map.put("age", "23");
        Jedis jedis = connect();
        jedis.hmset("user", map);

        // 从map 中取出 value
        // 第一个参数是存入redis中map对象的key，后面跟的是放入map中的对象的key，后面的key可以跟多个，是可变
        List<String> getmap = jedis.hmget("user", "address");
        System.out.println(getmap);
        List<String> getmap2 = jedis.hmget("user", "address", "age");
        System.out.println(getmap2);

        //删除map中的某个键值
        jedis.hdel("user", "age");

        System.out.println(jedis.hlen("user")); //返回key为user的键中存放的值的个数2

        System.out.println(jedis.exists("user"));//是否存在key为user的记录 返回true
        System.out.println("all keys ： " + jedis.hkeys("user"));//返回map对象中的所有key
        System.out.println("all values ： " + jedis.hvals("user"));//返回map对象中的所有value

        //获取 user 中的所有key
        Set<String> keys = jedis.hkeys("user");
        keys.stream().forEach(x -> System.out.println("key: " + x));
    }

    /**
     * list 链表
     * list 是一个链表结构,可以理解为一个每个子元素都是 string 类型的双向链表
     */
    private static void testList() {
        Jedis jedis = connect();
        //移除 lists 中所有的内容
        jedis.del("lists");

        // 向key lists 链表头部添加字符串元素
        jedis.lpush("lists", "abel1");
        jedis.lpush("lists", "abel2");
        jedis.lpush("lists", "abel3");
        // 向key lists 链表尾部添加字符串元素
        jedis.rpush("lists", "abel4");
        jedis.rpush("lists", "abel5");

        //获取lists 的长度
        System.out.println(jedis.llen("lists"));
        //按顺序输出链表中所有元素
        System.out.println(jedis.lrange("lists", 0, -1));
        //在abel4 前插入 abelLinsert
        jedis.linsert("lists", BinaryClient.LIST_POSITION.BEFORE, "abel4", "abelLinsert");
        System.out.println(jedis.lrange("lists", 0, -1));

        //在下标为2的位置插入 abelLset 前插入abel0
        jedis.lset("lists", 2, "abelLset");
        System.out.println(jedis.lrange("lists", 0, -1));


        //插入两个 abel2 为了后面的删除
        jedis.lpush("lists", "abel2");
        jedis.lpush("lists", "abel2");
        // 从 lists 中删除 3 个 value = abel2 的元素 , 可以不连续
        // 当删除 count = 0 个 时则删除全部 value = abel2 的元素
        jedis.lrem("lists", 0, "abel2");
        System.out.println(jedis.lrange("lists", 0, -1));

    }

    /**
     * set 是无序集合,最大可以包含(2 的 32 次方-1)40多亿个元素。
     * set 的是通过 hash table 实现的, 所以添加,删除,查找的复杂度都是 O(1)。hash table 会随着添加或者删除自动的调整大小。
     */
    public static void testSet() {
        Jedis jedis = connect();
        jedis.sadd("person", "abel1");
        jedis.sadd("person", "abel2");
        jedis.sadd("person", "abel3");
        jedis.sadd("person", "abel4");
        jedis.sadd("person", "abel4");

        //获取所有加入的value
        System.out.println(jedis.smembers("person"));
        // 从 person 中 移除 abel4
        jedis.srem("person", "abel4");
        //获取所有加入的value
        System.out.println("values: " + jedis.smembers("person"));
        //判断 abels 是否是 person 集合的元素
        System.out.println(jedis.sismember("person", "abels"));
        //返回集合中的一个随机元素
        System.out.println(jedis.srandmember("person"));
        //返回集合的元素个数
        System.out.println(jedis.scard("person"));
    }

    /**
     * sorted
     * sorted set 是有序集合,它在 set 的基础上增加了一个顺序属性,这一属性在添加修 改元素的时候可以指定,每次指定后,会自动重新按新的值调整顺序。
     * 可以理解了有两列的 mysql 表,一列存 value,一列存顺序。
     * <p>
     * sort set和set类型一样，也是string类型元素的集合，也没有重复的元素，不同的是sort set每个元素都会关联一个权，
     * 通过权值可以有序的获取集合中的元素添加，删除，查找的复杂度都是O(1)
     */
    public static void testSortSet() {
        Jedis jedis = connect();
        System.out.println(jedis.flushDB());
        jedis.zadd("sortKey", 300, "abel");
        jedis.zadd("sortKey", 20, "mysql");
        jedis.zadd("sortKey", 40, "redis");

        // 按权值从小到大排序
        System.out.println(jedis.zrange("sortKey", 0, -1));
        // 按权值从大到小排序
        System.out.println(jedis.zrevrange("sortKey", 0, -1));

        // 元素个数
        System.out.println("元素个数：" + jedis.zcard("sortKey"));
        // 元素abel 的 下标
        System.out.println("元素xxx 的 下标：" + jedis.zscore("sortKey", "abel"));

        // 删除元素 abel
//        jedis.zrem("sortKey", "abel");

        //权值 0-100的总数
        System.out.println("0-100 的总数： " + jedis.zcount("sortKey", 0, 100));
        //给元素 redis 的 权值 + 50
        System.out.println("给元素的 权值  + 50： " + jedis.zincrby("sortKey", 50, "redis"));
        //权值在0-100的值
        System.out.println("权值在0-100的值： " + jedis.zrangeByScore("sortKey", 0, 100));
        //返回 mysql 的权值的排名，从0开始计数
        System.out.println(jedis.zrank("sortKey", "mysql"));
        // 输出整个集合值
        System.out.println("输出整个集合值： " + jedis.zrange("sortKey", 0, -1));
    }


    /**
     * 订阅一个频道
     */
    public static void subscribe(){
        Jedis jedis = connect();
        MyJedisPubSub jedisPubSub = new MyJedisPubSub();
        jedis.subscribe(jedisPubSub, "abelChannel");
    }

    /**
     * 向频道发送消息
     */
    public static void publish(){
        Jedis jedis = connect();
        int i =5 ;
        while (i > 0 ) {
            jedis.publish("abelChannel", "我是第"+i+"条消息。");
            i--;
        }
    }
}
