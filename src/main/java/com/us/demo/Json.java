package com.us.demo;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.Map;

/**
 * Created by yangyibo on 17/6/9.
 */
public class Json {
    public static void main(String[] args) {
        System.out.println(newJsonStr());
//        jsonToMap(newJsonStr());
        System.out.println(newJsonArray());
    }

    /**
     * {"name":"abel","age":21}
     * @return
     */
    private static String newJsonStr() {
        JSONObject jb = new JSONObject();
        jb.put("name", "abel");
        jb.put("age", 21);
        return jb.toJSONString();
    }

    /**
     * [{"name":"abel","age":21}]
     * @return
     */
    private static String newJsonArray() {
        JSONArray ja = new JSONArray();
        JSONObject jb = new JSONObject();
        jb.put("name", "abel");
        jb.put("age", 21);
        JSONObject jb2 = new JSONObject();
        jb2.put("name", "an");
        jb2.put("age", 22);
//        ja.add(jb);
//        ja.add(jb2);
        //都是 0 的话 jb 会被挤到后面 ，index 可以指定顺序
        ja.add(0,jb);
        ja.add(0,jb2);
        return ja.toJSONString();
    }

    private static Map<String, Object> jsonToMap(String jsonStr) {
        Map<String, Object> map = JSONObject.parseObject(jsonStr);
        printMap(map);
        Map<String, Object> map1 = JSONArray.parseObject(jsonStr);
        printMap(map1);
        Map<String, Object> map2 = JSON.parseObject(jsonStr, new TypeReference<Map<String, Object>>() {
        });
        printMap(map2);
        return map;
    }

    private static void printMap(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println("key: " + entry.getKey() + "value : " + entry.getValue());
        }
    }

}
