package com.us.demo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
}
