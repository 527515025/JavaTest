package com.us.basics;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.us.bean.Person;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by yangyibo on 17/4/19.
 */
public class StringTest {
    public static void main(String[] args) {

//        stringIntern();
//        isNumeric("-1");
//        System.out.println(stampToDate("1494376080000"));
//        valueOf();
//        trim();
//        instanceofTest(2);
//        getUchat();
//        splitTest();
//        splitTestEnter();
//        replaceAllTest();
//        spiltTest();
        stringToarray();
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

    private static void trim() {
        String str1 = " aa  ", str2 = "  bb", str3 = "cc  ", str4 = "  d d  ";
        str1 = str1.trim();
        str2 = str2.trim();
        str3 = str3.trim();
        str4 = str4.trim();
        System.out.println(str1 + " length: " + str1.length());
        System.out.println(str2 + " length: " + str2.length());
        System.out.println(str3 + " length: " + str3.length());
        System.out.println(str4 + " length: " + str4.length());
    }

    private static void instanceofTest(int flag) {
        Object object = null;
        if (flag == 1) {
            object = "sdfsdfsdfsdf";
        } else {
            Person person = new Person();
            person.setName("abel");
            object = person;
        }
        if (object instanceof String) {
            System.out.println(object);
        }
        if (object instanceof Person) {
            Person person2 = (Person) object;
            System.out.println("Person:" + person2.getName());
        }
    }

    /**
     * 驼峰转化为下划线
     */
    private static void getUchat() {
        String str = "isResponsedTimeout";
        String pattern = "[A-Z]";
        str = str.replaceAll(pattern, "_$0").toLowerCase();
        System.out.println(str);
    }

    private static void splitTest() {
        String s = "ownedByUser_cnname";
        String[] strings = s.split("_");
        Arrays.stream(strings).forEach(x -> System.out.println(x + "\n"));
    }

    private static void splitTestEnter() {
        String s = "测试\n" +
                "测试：请问";
        if (s.contains("\n")) {
            System.out.println("true");
        }
        String[] strings = s.split("\n");
        Arrays.stream(strings).forEach(x -> System.out.println(x + "1 \n"));
    }

    private static void replaceAllTest() {
        String str = "sdfsf$abel.123.12ssd";
        str = str.replaceAll("\\$.*?\\.", "bo");
        System.out.println(str);
    }

    private static void spiltTest() {
        String str = "asd&=&123123";
        String[] split = str.split("&=&");
        String[] spiltTest = split[1].split(",");
        System.out.println(spiltTest[0]);
    }


    private static void stringToarray() {
        String str = "1;2";
        try {
            JSONArray ja = JSON.parseArray(str);
            ja.forEach(x -> {
                System.out.println(x);
            });
        } catch (JSONException e) {
            Arrays.stream(str.toString().split(";")).forEach(x -> {
                System.out.println(x);
            });
        }
    }
}