package com.us.IO;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by yangyibo on 17/3/6.
 */
public class FileTest {

    private static String OS_NAME = System.getProperty("os.name").toLowerCase();
    private static Long pos = 0L;

    public static void main(String[] args) {
        System.out.println(OS_NAME.contains("mac"));
        try {
            write();
            read();
        } catch (Exception e) {

        }

    }

    /**
     * 获取当前路径下的所有文件
     */
    private static void getAllFileInPath() {
        File file = new File("src/main/resource");
        System.out.println(file);
    }

    /**
     * @throws IOException
     */
    private static void write() throws IOException {
        RandomAccessFile file = new RandomAccessFile("src/main/resource/testFile.txt", "rw");
        file.seek(pos);
        file.write("a bcd你好啊的撒法".getBytes("UTF-8"));
        file.close();
        System.out.println();

    }

    private static void read() throws IOException {
        RandomAccessFile rf = new RandomAccessFile("src/main/resource/testFile.txt", "r");
        System.out.println(new String(rf.readLine().getBytes("ISO-8859-1"), "utf-8"));
        pos = rf.length();
        rf.close();
        System.out.println();

    }

}
