package com.us.basics;

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
//        System.out.println(sendEmailJudgementTime("16:32:39","16:56:39"));
//        System.out.println(timeStampToData("1497117051"));
//        zabbixTime();
        System.out.println(getDateInterval());

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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
            if (dateString.toUpperCase().contains("PM")) {
                if (c.get(Calendar.HOUR_OF_DAY) != 12) {
                    c.add(Calendar.HOUR, 12);
                }
            } else if (c.get(Calendar.HOUR_OF_DAY) == 12) {
                c.add(Calendar.HOUR, -12);
            }
            date = c.getTime();
            return date;
        } catch (ParseException ex) {
        }
        return null;
    }


    public static Boolean sendEmailJudgementTime(String startTime, String endTime) {
        boolean flag = false;
        DateFormat fmt = new SimpleDateFormat("HH:mm:ss");
        Date startDate, endDate, nowDate = new Date();
        try {
            nowDate = fmt.parse(fmt.format(nowDate));
            startDate = fmt.parse(startTime);
            endDate = fmt.parse(endTime);
            if (startDate.getTime() < nowDate.getTime() && nowDate.getTime() < endDate.getTime()) {
                flag = true;
            }
        } catch (ParseException ex) {
        }
        return flag;
    }


    private static void zabbixTime() {
        String time = "10:15:23";
        Date nowDate = new Date();
        String nowDateStr = formatDate(nowDate);
        time = nowDateStr + " " + time;
//        time = nowDateStr.substring(0,nowDateStr.indexOf(" ")+1)+time;
        System.out.println(time);
    }

    private static Date timeStampToData(String time) {
        try {
            Long times = Long.valueOf(time);
            times = times * 1000;
            Date date = new Date();
            date.setTime(times);
            return date;
        } catch (NumberFormatException e) {
            e.getMessage();
        }
        return null;
    }

    /**
     * 计算两个时间的间隔
     *
     * @return
     */
    public static Integer getDateInterval() {
        long diff=0;
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d1 = df.parse("2004-03-26 13:31:40");
            Date d2 = df.parse("2004-03-06 13:31:20");
            diff= d1.getTime() - d2.getTime();
        } catch (Exception e) {

        }
        return (int) (diff/1000);
    }

}
