package com.us.demo;

import java.io.RandomAccessFile;

/**
 * Created by yangyibo on 17/3/6.
 */
public class FileTest {

    private RandomAccessFile randomAccessFile = null;
    private static String OS_NAME = System.getProperty("os.name").toLowerCase();

    public static void main(String[] args) {
//        ArraysTest();
        System.out.println(OS_NAME.contains("mac"));
//       File file=new File("/Users/yangyibo/photo");
//        System.out.println(file); //获取当前路径下的所有文件

    }



}
