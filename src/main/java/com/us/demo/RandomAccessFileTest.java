package com.us.demo;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yangyibo on 17/3/6.
 */
public class RandomAccessFileTest {

    private RandomAccessFile randomAccessFile = null;

    public static void main(String[] args) {
        ArraysTest();
//       File file=new File("/Users/yangyibo/photo");
//        System.out.println(file); //获取当前路径下的所有文件

    }


    public static void ArraysTest(){

        String[] strs= {"tail.log","tail.log.1","tail.log.2","tail.log.10","tail.log.4","tail.log.5","tail.log.6"};
        Arrays.sort(strs);
        Arrays.stream(strs).forEach(x-> System.out.println(x));
    }
}
