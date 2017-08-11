package com.us.demo;

import java.util.*;

/**
 * Created by yangyibo on 17/6/13.
 */
public class SetListMap {
    public static void main(String[] args) {
//        setTest();
        intersect();

    }

    private static void setTest() {
        Set set = new HashSet();
        set.add("sdf");
        set.add("sdf");
        set.add("asdf");
        set.stream().forEach(x -> System.out.println(x));

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
     * @param map
     */
    public static void getMap(Map<String, Object> map){
        map.put("1", "value1");
        map.put("2", "value2");
        map.put("3", "value3");

        //第一种：普遍使用，二次取值
        System.out.println("通过Map.keySet遍历key和value：");
        for (String key : map.keySet()) {
            System.out.println("key= "+ key + " and value= " + map.get(key));
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
}
