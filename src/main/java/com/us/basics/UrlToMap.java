package com.us.basics;

import org.mortbay.util.MultiMap;
import org.mortbay.util.UrlEncoded;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangyibo on 2018/12/13.
 */
public class UrlToMap {
    public static void main(String[] args) {
//        urlToMap();
        splitKeyAndVauleToMap();
    }

    private static void urlToMap() {
        String content = "userId=1703110002519634&assessmentId=88&open_id=d2a75cd307b41684c758a64b00f0ea6c&access_token=ed90a091cc4ff170b6ec0d896b7f791b";
        MultiMap multiMap = new MultiMap();
        UrlEncoded.decodeTo(content, multiMap, "UTF-8");

    }

    private static void splitKeyAndVauleToMap() {
        String content = "access_token=ed90a091cc4ff170b6ec0d896b7f791b; open_appid=mpqhwzwqknmifhbscx; open_id=d2a75cd307b41684c758a64b00f0ea6c; user_id=1703110002519634";
        Map<String, String> map = new HashMap<>();
        if (content.contains(";")) {
            String[] arr = content.split(";");
            Arrays.stream(arr).forEach(x -> {
                if (x.contains("=")) {
                    String[] keyAndValue = x.split("=");
                    map.put(keyAndValue[0].trim(), keyAndValue[1].trim());
                }
            });
        }
        System.out.println(map.toString());
    }
}
