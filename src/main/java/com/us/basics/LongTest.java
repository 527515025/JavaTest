package com.us.basics;

/**
 * Created by yangyibo on 17/5/9.
 */
public class LongTest {
    public static void main(String[] args) {

        Long startTime = 1494375360000l, endTime = 1494332628678l, nowTime = 1494332688678l;

        if (endTime > nowTime)//抛异常，处理
        {//传入数据错误
            System.out.println("1");
        }

        if (startTime > nowTime) {
            System.out.println("2");

        }

        if (endTime - startTime <= 0) {
            System.out.println("3");
        }

    }
}
