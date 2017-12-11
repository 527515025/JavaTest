package com.us.basics;

import java.util.Base64;

/**
 * Created by yangyibo on 17/1/6.
 */
public class Base64Test {
    public static void main(String[] args) {

        getEppEncoder("abel:abel");
        getDecoder("YWRtaW46YWRtaW4=");
    }

    //加密
    private static void getEncoder(String pass) {
        //flume database password encoder
        pass = Base64.getEncoder().encodeToString((pass + ",Unistacks").getBytes());
        String Dpass = new String(Base64.getDecoder().decode(pass));
        Dpass = Dpass.substring(0, Dpass.indexOf(","));
        System.out.println(pass + "---------" + Dpass);
    }

    //解密
    private static void getDecoder(String str) {
        byte[] bytes = Base64.getDecoder().decode(str);
        System.out.println(new String(bytes));
    }

    private static void getEppEncoder(String pass) {
        pass = Base64.getEncoder().encodeToString(pass.getBytes());
        String Dpass = new String(Base64.getDecoder().decode(pass));
        System.out.println(pass + "---------" + Dpass);
    }
}
