package com.us.demo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by yangyibo on 17/6/13.
 */
public class SetListMap {
    public static void main(String[] args) {
        setTest();

    }

    private static void setTest() {
        Set set=new HashSet();
        set.add("sdf");
        set.add("sdf");
        set.add("asdf");
        set.stream().forEach(x-> System.out.println(x));

    }
}
