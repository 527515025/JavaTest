package com.us.basics.http;

import com.alibaba.fastjson.JSON;
import com.us.basics.http.logisticsMode.LogisticsResponse;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yyb
 * @time 2019/12/4
 */
public class Test {
    public static void main(String[] args) {
        String host = "https://jisukdcx.market.alicloudapi.com";
        String path = "/express/query";
        String method = "GET";
        String appcode = "xxxxxxxx";
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("number", "1193020101430");
        querys.put("type", "auto");
        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            if (response.getStatusLine().getStatusCode() == 200) {
//                String resp = EntityUtils.toString(response.getEntity(), "UTF-8");

                LogisticsResponse logisticsResponse = JSON.toJavaObject(HttpUtils.getJson(response), LogisticsResponse.class);
                System.out.println(logisticsResponse.getMsg());
            } else {
                System.out.println("请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
