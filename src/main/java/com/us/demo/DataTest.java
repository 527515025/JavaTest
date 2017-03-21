package com.us.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangyibo on 17/3/16.
 */
public class DataTest {
    public static void main(String[] args) {
        Date date = new Date();
        Integer minute = 30;
//        System.out.println(CalculationData(date, minute));
        maptest();
//        CalculationData1();
    }


    public static boolean CalculationData(Date oldDate, Integer minute) {
        Date nowDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(oldDate);
        c.add(Calendar.MINUTE, minute);
        oldDate = c.getTime();
        if (oldDate.getTime() > nowDate.getTime()) {
            return true;
        }
        return false;
    }


    public static void CalculationData1() {
        String str = "2013-07-18 ";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
//            Date myDate = formatter.parse(str);
            Date myDate = new Date();
            Date nowDate = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(myDate);
            c.add(Calendar.MINUTE, 10);
            myDate = c.getTime();
            System.out.println(myDate + "aaaaa");
            System.out.println(myDate.getTime() + "-----" + nowDate.getTime());
            if (nowDate.getTime() < myDate.getTime()) {
                System.out.println("true");
            }
//            System.out.println(formatter.format(myDate));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    public static String formatDate(Date dateTime) {
        String result;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        result = simpleDateFormat.format(dateTime);
        return result;
    }

    public static void maptest(){
        Map<String ,Object> map = new HashMap<>();
        if(map.get("refresh")!=null){
            //检查超时
            System.out.println("true");
        }
        System.out.println("flase");
    }

}
