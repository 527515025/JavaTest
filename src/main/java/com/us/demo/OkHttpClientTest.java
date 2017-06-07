package com.us.demo;

import com.alibaba.fastjson.JSONArray;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyibo on 17/3/13.
 */
public class OkHttpClientTest {
    private static String url = "http://localhost:8080/dataCollect/rawEvents";
    private static String content = "Parsing events: Omegamon_Base;cms_hostname='itmserver';cms_port='370$i’;integration_type='U';master_reset_flag='';appl_label='';situation_name='disk';situation_type='S';situation_origin='itmserver:LZ';situation_time='01/06/2017 11:33:$i.000';situation_status='N';situation_thrunode='TEMS_TEST';situation_fullname='home_disk_error';situation_displayitem='';source='ITM';sub_source='itmserver:LZ';hostname='itmserver';origin='192.168.100.$i’;adapter_host='itmserver';date=‘$i/06/2017';severity='CRITICAL';msg='itm server home directory > 80%';situation_eventdata='~';END";

    public static void main(String[] args) {
        send();
    }

    public static void send() {
        List<String> contents = new ArrayList<>();
        contents.add(content);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), JSONArray.toJSON(contents).toString());
        Request request = new Request.Builder()
                .url(url)
                .addHeader("key","23423kjh4jk23xc98")
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
                    System.out.println ("重连三次第"+i+"次--response : " + response);
                }
                if(response != null && response.isSuccessful()) {
                    System.out.println ("重连三次第"+i+"次链接成功--response : " + response);
                }
                i++;
            }
        }
    }
}
