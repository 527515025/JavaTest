package com.us.IO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by yangyibo on 17/9/25.
 */
public class NioFiles {
    public static void main(String[] args)  {
        getInode();
    }

    private static void  getInode () {
        File file = new File("/Users/yangyibo/Desktop/yi");
        try {
            Long inode = (long) Files.getAttribute(file.toPath(), "unix:ino");
            System.out.println(inode);
        }catch (IOException e){
            e.getMessage();
        }
    }
}
