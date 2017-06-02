package com.us.demo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yangyibo on 17/3/16.
 */
public class DataTest {
    public static void main(String[] args) {
//        Date date = new Date();
//        Integer minute = 30;
//        System.out.println(CalculationData(date, minute));
//        CalculationSecond(date, 30);
//        maptest();
//        System.out.println(stringToDate("05/25/2017 10:17:39 PM"));
        System.out.println(sendEmailJudgementTime("16:32:39","16:56:39"));

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
        System.out.println(oldDate.getTime() - nowDate.getTime());
        return false;
    }

    public static String formatDate(Date dateTime) {
        String result;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        result = simpleDateFormat.format(dateTime);
        return result;
    }

    public static Date stringToDate(String dateString) {
        DateFormat fmt = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        Date date;
        try {
            date = fmt.parse(dateString);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            if(dateString.toUpperCase().contains("PM")) {
              if(c.get(Calendar.HOUR_OF_DAY) != 12) {
                  c.add(Calendar.HOUR, 12);
              }
            }else if(c.get(Calendar.HOUR_OF_DAY) == 12) {
                c.add(Calendar.HOUR, -12);
            }
            date = c.getTime();
            return date;
        } catch (ParseException ex) {
        }
        return null;
    }



    public static Boolean sendEmailJudgementTime(String startTime,String endTime) {
        boolean flag=false;
        DateFormat fmt = new SimpleDateFormat("HH:mm:ss");
        Date startDate,endDate, nowDate=new Date();
        try {
            nowDate=fmt.parse(fmt.format(nowDate));
            startDate = fmt.parse(startTime);
            endDate=fmt.parse(endTime);
            if (startDate.getTime()<nowDate.getTime() && nowDate.getTime()<endDate.getTime())
            {flag=true;}
        } catch (ParseException ex) {
        }
        return flag;
    }

    public static void maptest() {
        Map<String, Object> map = new HashMap<>();
        if (map.get("refresh") != null) {
            //检查超时
            System.out.println("true");
        }
        System.out.println("flase");
    }

}
