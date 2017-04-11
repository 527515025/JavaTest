package com.us.demo;

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
        CalculationSecond(date,30);
        maptest();
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

    public static boolean CalculationSecond(Date oldDate, Integer second) {
        Date nowDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(oldDate);
        c.add(Calendar.SECOND, second);
        oldDate = c.getTime();
        System.out.println(oldDate.getTime()-nowDate.getTime());
        return false;
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
