package com.us.demo;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yangyibo on 17/4/19.
 */
public class StringTest {
    public static void main(String[] args) {

//        stringIntern();
//        isNumeric("-1");
//        System.out.println(stampToDate("1494376080000"));
        valueOf();

    }


    public static void isNumeric(String str) {
        if (StringUtils.isNumeric(str) && !str.startsWith("0")) {
            //为正数
            System.out.println(str);
        }
    }


    public static void stringIntern() {
        String s1 = "Hello";
        String s2 = new String("Hello");
//        s2 = s2.intern();
        if (s1 == s2) {
            System.out.println("s1 == s2");
        } else {
            System.out.println("s1 != s2");
        }
        if (s1.equals(s2)) {
            System.out.println("s1 equals s2");
        } else {
            System.out.println("s1 not equals s2");
        }
    }


    /*
    * 将时间戳转换为时间
    */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    private static void valueOf() {
        try {
            if (Integer.valueOf("sdfsd") > 2) {
                System.out.println("ok");
            }
        } catch (NumberFormatException e) {

        }
        System.out.println("no");
    }

}