package com.us.basics.http;

import com.alibaba.fastjson.JSONArray;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyibo on 17/3/13.
 */
public class OkHttpClientTest {

    public static void main(String[] args) {
//        testPost();
        testGet();

    }

    public static void testPost() {
        String url = "http://192.168.100.156:8080/eventCollect/api/default";
        String content = ":\"\"##:\"\"##:\"10.0.170.144\"##:\"goldora1\"##:\"tivoli_eif\"##:\"ITM\"##:\"ITM_KLZ_DB\"##:\"Database oracle\"##:\"5\"##:\"cliffcliffcliffSMDGSIZE=414315 ASMDGUSEDPC=85.02 CHECKTM=-07-15-17.29.09 HOSTIP=10.1.84.88 HOSTNAME=goldora1  时间:07/15/ 17:40:53.001\"##:\"\"##:\"\"##:\"09/21/2017 10:29:01 AM\"##:\"\"##:\"1\"##:\"\"##:\"\"##:\"\"##:\"\"";

        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(100);
                sendPost(url, content);
            } catch (Exception e) {
                System.out.println(e.getMessage().toString());
            }

        }
    }

    public static void sendPost(String url, String content) {
        List<String> contents = new ArrayList<>();
        contents.add(content);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), JSONArray.toJSON(contents).toString());
        Request request = new Request.Builder()
                .url(url)
                .addHeader("appkey", "23423kjh4jk23xc98")
                .post(body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                System.out.println("response: " + response);
            }
        } catch (IOException e) {
            int i = 1;
            while (i <= 3) {
                try {
                    response = client.newCall(request).execute();
                } catch (Exception ex) {
                    System.out.println("重连三次第" + i + "次--response : " + response);
                }
                if (response != null && response.isSuccessful()) {
                    System.out.println("重连三次第" + i + "次链接成功--response : " + response);
                }
                i++;
            }
        }
    }

    public static void testGet() {
        String url = "http://localhost:8081/easyviews/alarmInfo/list2?start=queryStartTime&end=queryEndTime";
        sendGet(url);
    }

    public static void sendGet(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("appkey", "23423kjh4jk23xc98")
                .get().build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                System.out.println("response: " + response);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
