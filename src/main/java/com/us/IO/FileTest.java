package com.us.IO;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by yangyibo on 17/3/6.
 */
public class FileTest {

    private RandomAccessFile randomAccessFile = null;
    private static String OS_NAME = System.getProperty("os.name").toLowerCase();

    public static void main(String[] args) {
        System.out.println(OS_NAME.contains("mac"));
        File file = new File("src/main/resource");
        System.out.println(file); //获取当前路径下的所有文件
        try {
            write();
        } catch (Exception e) {

        }

    }

    private static void write() throws IOException {
        RandomAccessFile rf = new RandomAccessFile("src/main/resource/test.json", "rw");
        rf.seek(10);
        rf.writeBytes("abel");
        rf.close();
        System.out.println();

    }

}
