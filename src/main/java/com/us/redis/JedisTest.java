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
        testSortSet();
    }

    public static Jedis connect() {
        Jedis jedis = new Jedis("192.168.100.76", 6379);
        jedis.auth("admin");
        //ping通显示PONG
        System.out.println(jedis.ping());//去ping我们redis的主机所在ip和端口
        return jedis;
    }

    /**
     * 操作string类型的key
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
     * 集合是通过哈希表实现的，所以添加，删除，查找的复杂度都是O(1)。 集合中最大的成员数为 232 - 1 (4294967295, 每个集合可存储40多亿个成员)。
     * set
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
     * Sortset
     * <p>
     * sort set和set类型一样，也是string类型元素的集合，也没有重复的元素，不同的是sort set每个元素都会关联一个权，通过权值可以有序的获取集合中的元素
     * 添加，删除，查找的复杂度都是O(1)
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
        System.out.println("元素个数："+jedis.zcard("sortKey"));
        // 元素abel 的 下标
        System.out.println("元素xxx 的 下标："+jedis.zscore("sortKey", "abel"));

        // 删除元素 abel
//        jedis.zrem("sortKey", "abel");

        //权值 0-100的总数
        System.out.println("0-100 的总数： " + jedis.zcount("sortKey", 0, 100));
        //给元素 redis 的 权值 + 50
        System.out.println("给元素的 权值  + 50： " + jedis.zincrby("sortKey", 50, "redis"));
        //权值在0-100的值
        System.out.println("权值在0-100的值： " + jedis.zrangeByScore("sortKey",  0, 100));
        //返回 mysql 的权值的排名，从0开始计数
        System.out.println(jedis.zrank("sortKey", "mysql"));
        // 输出整个集合值
        System.out.println("输出整个集合值： " + jedis.zrange("sortKey", 0, -1));
    }

}
