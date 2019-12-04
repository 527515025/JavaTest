package com.us.basics.http;

import com.alibaba.fastjson.JSON;
import com.us.basics.http.logisticsMode.LogisticsResponse;
import com.us.basics.http.logisticsMode.Result;
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
        String appcode = "xxx";
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("number", "1193020101430");
        querys.put("type", "yangyibo");
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
            switch (response.getStatusLine().getStatusCode()) {
                case 200:
                    Result logisticsResponse = JSON.toJavaObject((JSON) HttpUtils.getJson(response).get("result"), Result.class);
                    System.out.println(logisticsResponse.getNumber());
                    break;
                case 208:
                    System.out.println("单号没有信息");
                    break;
                case 207:
                    System.out.println("快递单号错误次数过多");
                    break;
                case 203:
                    System.out.println("快递公司不存在");
                    break;
                case 205:
                    System.out.println("没有信息");
                    break;
                default:
                    System.out.println("没有查询到信息");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
