package com.us.basics;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.us.bean.Person;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.python.google.common.collect.Lists;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by yangyibo on 17/6/13.
 */
public class SetListMap {
    private static ConcurrentHashMap<String, List<Integer>> concurrentHashMap = new ConcurrentHashMap(128);

    public static void main(String[] args) {
//        list();
//        setTest();
//        setTest2();
//        intersect();
//        getMap();
//        listPartition();
//        mapNullTest();
//        stringsLength();
//        mapIsNullTest();
//        splitList();
//        listIterator();
//        mapToUrlParam();
//        initMap();
        concurrentHashMapTest();
        concurrentHashMapTest2();
        System.out.println();
    }

    private static void setTest() {
        Set set = new HashSet();
        set.add("sdf");
        set.add("sdf");
        set.add("asdf");
        set.stream().forEach(x -> System.out.println(x));

    }

    private static void setTest2() {
        Set<String> set = new HashSet();
//        set.add("sdf");
//        set.add("sdf");
//        set.add("asdf");
        System.out.println(set);
//        String ss= set.toString();
//        ss = ss.substring(1, ss.length()-1);
//        System.out.println(ss+"-----------");

        System.out.println("set size" + set.size());
        StringBuffer s = new StringBuffer();
        s.append(set.toString());
        s.deleteCharAt(0);
        s.deleteCharAt(s.length() - 1);
        System.out.println(s.toString() + "-------------");
        set.stream().forEach(x -> System.out.println(x));

    }

    private static void printList(List<String> strings) {
        for (String str : strings) {
            System.out.println("---" + str);
        }
    }

    private static void intersect() {
        List<String> lst1 = new ArrayList<>();
        lst1.add("1");
        lst1.add("2");
        lst1.add("3");
        List<String> lst2 = new ArrayList<>();
        lst2.add("1");
        lst2.add("2");
        lst2.add("4");
        unionList(lst1, lst2);
//        //lst1去除掉 lst2 不包含的元素 "3" 取交集
//        lst1.retainAll(lst2);
//        lst1.forEach(x -> System.out.println(x));

//        //lst1去除掉 lst2 中的所有元素 "1"，"2"
//        lst1.removeAll(lst2);
//        lst1.forEach(x -> System.out.println(x));
        System.out.println("－－－－－－－－－－－－－－－－－－－－－");
        lst2.removeAll(lst1);
        lst2.forEach(x -> System.out.println(x));
    }

    /**
     * 去除重复两个集合中重复的数据
     *
     * @param list1
     * @param list2
     * @param <E>
     * @return
     */
    public static <E> List<E> unionList(List<E> list1, List<E> list2) {
        List<E> resultList = new ArrayList<E>();
        List<E> tmpList1;
        List<E> tmpList2;
        if (null != list1) {
            tmpList1 = new ArrayList<E>(list1);
            if (null != list2) {
                tmpList2 = new ArrayList<E>(list2);
                tmpList2.removeAll(tmpList1);
                resultList.addAll(tmpList2);
            }
            resultList.addAll(tmpList1);
        } else if (null != list2) {
            tmpList2 = new ArrayList<E>(list2);
            resultList.addAll(tmpList2);
        }
        return resultList;
    }

    public static void maptest() {
        Map<String, Object> map = new HashMap<>();
        if (map.get("refresh") != null) {
            //检查超时
            System.out.println("true");
        }
        System.out.println("flase");
    }

    /**
     * 遍历map
     *
     * @param
     */
    public static void getMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("1", "value1");
        map.put("2", "value2");
        map.put("3", "value3");
        map.put("4", null);

        //第一种：普遍使用，二次取值
        System.out.println("通过Map.keySet遍历key和value：");
        for (String key : map.keySet()) {
            System.out.println("key= " + key + " and value= " + map.get(key));
        }

        //第二种
        System.out.println("通过Map.entrySet使用iterator遍历key和value：");
        Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }

        //第三种：推荐，尤其是容量大时
        System.out.println("通过Map.entrySet遍历key和value");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }
        //第四种
        System.out.println("通过Map.values()遍历所有的value，但不能遍历key");
        for (Object v : map.values()) {
            System.out.println("value= " + v);
        }
    }


    /**
     * 查看hashMap的初始值
     * n = 16
     * sc = n - (n >>> 2);
     * sc = 12
     */
    public static void initMap() {
        ConcurrentHashMap<String, Integer> a = new ConcurrentHashMap();
        a.put("a", 0);
        System.out.println();
    }


    private static void list() {
        List<String> list = new ArrayList<>();
        Person p = new Person();
        for (int i = 0; i < 5; i++) {
            p.setName("abel" + i);
            list.add(p.toString());
        }
        printList(list);
    }

    /**
     * 将list 分成多份，依赖maven collections4
     */
    private static void listPartition() {
        List<Integer> intList = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8);
        System.out.println(ListUtils.partition(intList, 2));
    }

    /**
     * list 长度，
     */
    private static void stringsLength() {
        String[] strings = new String[]{"1", "2"};
        System.out.println(strings.length);
    }


    /**
     * map 取空
     */
    private static void mapNullTest() {
        Map<String, Object> map = new HashMap<>();
//        map.put("1", "value1");
//        map.put("2", "value2");
//        map.put("3", "value3");
        System.out.println(map.get("5"));
    }

    /**
     * map 判断空
     */
    private static void mapIsNullTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("1", "value1");
        map.put("2", "value2");
        map.put("3", "value3");
        map.put("1", "value5");
        map.remove("1");
        map.remove("2");
        map.remove("3");
//        map.put("1", map.get("1"));
        map = isExistAndPutData(map, "1", "123123");

        if (null == map || map.size() == 0) {
            System.out.println("map is null");
        }
    }


    /**
     * 判断key对应的value是不是为空，如果为空则插入
     *
     * @param map
     * @param key
     * @param value
     * @return
     */
    private static Map<String, Object> isExistAndPutData(Map<String, Object> map, String key, String value) {
        System.out.println(map.get(key));
        if (null == map.get(key)) {
            map.put(key, value);
            return map;
        }
        return map;
    }


    /**
     * list 等分切分
     */
    private static void splitList() {
        List<Integer> intList = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18);
        if (intList.size() >= 5) {
//           Integer sum =  intList.size()/5;
            List<List<Integer>> lists = ListUtils.partition(intList, 5);
            for (List<Integer> s : lists) {
                System.out.println(s.toString());
            }
        }
    }


    /**
     * list Iterator 遍历
     */
    private static void listIterator() {
        List<Integer> intList = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18);
        Iterator<Integer> iter = intList.iterator();
        while (iter.hasNext()) {
            Integer i = iter.next();
            System.out.println(i);
            if (i.equals(8)) {
                iter.remove();
                break;
            }
        }
        intList.stream().forEach(x -> System.out.println(x));
    }


    /**
     * map 参数按照升序排列
     *
     * @return
     */
    private static void mapToUrlParam() {
        String signSecret = "3E4BA352ED4A4984BD579AC72E08B258";
        Map<String, String> data = new HashMap<>();
        data.put("yang", "6");
        data.put("sbo", "5");
        data.put("ayi", "1");
        data.put("dan", "2");
        data.put("dao", "3");
        data.put("dui", "4");

        String str = Joiner.on("#").withKeyValueSeparator("=").join(data);
        List<String> list = com.google.common.collect.Lists.newArrayList(Splitter.on("#").trimResults().splitToList(str));
        Collections.sort(list);
        String req = Joiner.on("&").join(list).concat(signSecret);
        System.out.println(req);


    }

    /**
     * map 参数按照升序排列方法2
     *
     * @return
     */
    private static void mapToUrlParam2() {
        String signSecret = "3E4BA352ED4A4984BD579AC72E08B258";
        Map<String, String> data = new HashMap<>();
        data.put("yang", "6");
        data.put("sbo", "5");
        data.put("ayi", "1");
        data.put("dan", "2");
        data.put("dao", "3");
        data.put("dui", "4");

        List<String> keys = new ArrayList<>();
        data.entrySet().stream().parallel().forEach(x -> {
            keys.add(x.getKey());
        });
        StringBuffer sb = new StringBuffer();
        List<String> keysSorted = keys.stream().parallel().sorted().collect(Collectors.toList());
        keysSorted.forEach(x -> {
            sb.append(x).append("=").append(data.get(x)).append("&");
        });
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
            sb.append(signSecret);
        }
        System.out.println(sb.toString());
    }


    private static void concurrentHashMapTest() {
        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);
        integers.add(4);
        concurrentHashMap.put("1",integers);
        integers.addAll(integers);
    }

    private static void concurrentHashMapTest2() {
        List<Integer> integers2 = new ArrayList<>();
        integers2.add(5);
        integers2.add(6);
        integers2.add(7);
        integers2.add(8);
        concurrentHashMap.get("1").addAll(integers2);
        integers2.addAll(integers2);
    }
}

