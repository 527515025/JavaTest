package com.us.demo;


import com.alibaba.fastjson.*;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by yangyibo on 17/6/9.
 */
public class Json {
    public static void main(String[] args) {
//        System.out.println(newJsonStr());
//        jsonToMap(newJsonStr());
//        jsonPath();
        recursion();
//        System.out.println(newJsonArray());
    }

    /**
     * {"name":"abel","age":21}
     *
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
     *
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
        ja.add(0, jb);
        ja.add(0, jb2);
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


    private static void jsonPath() {
        String data = "{\n" +
                "    \"code\": \"0\",\n" +
                "    \"msg\": \"success\",\n" +
                "    \"exception\": null,\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"businessName\": \"柜面1\",\n" +
                "            \"componentName\": \"认证APP\",\n" +
                "            \"alarmLevel\": \"1\",\n" +
                "            \"alarmKey\": \"bff2f3decc181a8ba21c82aecff1ddfa\",\n" +
                "            \"constraintDesc\": \"[交易类型:TS991203][所有交易数]\",\n" +
                "            \"alarmTime\": \"2017-10-17 17:34:00\",\n" +
                "            \"startTime\": \"2017-10-17 17:34:00\",\n" +
                "            \"duration\": 1,\n" +
                "            \"manageState\": \"异常\",\n" +
                "            \"alarmType\": \"阀值告警\",\n" +
                "            \"productType\": \"BPM\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"businessName\": \"柜面2\",\n" +
                "            \"componentName\": \"认证F5\",\n" +
                "            \"alarmLevel\": \"2\",\n" +
                "            \"alarmKey\": \"665b7d6f49f3d85d4b15b6a9ca38df90\",\n" +
                "            \"constraintDesc\": \"[6.1.12.27][响应时间(ms)]\",\n" +
                "            \"alarmTime\": \"2017-10-17 17:30:00\",\n" +
                "            \"startTime\": \"2017-10-17 17:30:00\",\n" +
                "            \"duration\": 2,\n" +
                "            \"manageState\": \"未处理\",\n" +
                "            \"alarmType\": \"维度阀值告警\",\n" +
                "            \"productType\": \"BPMC\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

//        JSONArray jsonObject = JSONArray.parseArray(data);
//        System.out.println(JSONPath.size(jsonArray,"$."));
        // 判断是否是个json 对象
        System.out.println(JSONPath.contains(new JSONPath(data), "$.*"));

        if (data.startsWith("[") && data.endsWith("]")) {
            System.out.println("-----------JSONArray----------------");
        } else if (data.startsWith("{") && data.endsWith("}")) {
            System.out.println("-----------JSONObject---------------");
        }

        JSONObject jsonObject = JSONObject.parseObject(data);

        System.out.println("data.size " + JSONPath.size(jsonObject, "$.data"));

        System.out.println(JSONPath.eval(jsonObject, "$.data.componentName"));

        System.out.println(JSONPath.eval(jsonObject, "$.data[0:].componentName"));
        // data list 中duration > 1
        System.out.println(JSONPath.eval(jsonObject, "$.data[duration > 1]"));
        // data list 中 componentName ＝  认证F5
        System.out.println(JSONPath.eval(jsonObject, "$.data[componentName = '认证F5']"));
        // data[0] 的所有属性值 只有value 没有 key
        System.out.println(JSONPath.eval(jsonObject, "$.data[0].*"));
        // 只取data list 中所有对象的 componentName 和  businessName属性
        System.out.println(JSONPath.eval(jsonObject, "$.data['componentName','businessName']"));

    }


    /**
     * 使用 jsonPath［n］递归解析json
     */
    private static void recursion() {
        String data = "{\n" +
                "    \"code\": \"0\",\n" +
                "    \"msg\": \"success\",\n" +
                "    \"exception\": null,\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"businessName\": \"柜面\",\n" +
                "            \"componentName\": [\n" +
                "                {\n" +
                "                    \"code\": \"01\",\n" +
                "                    \"msg\": \"succes1\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"code\": \"02\",\n" +
                "                    \"msg\": \"succes2\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"constraintDesc\": \"[6.1.12.27][响应时间(ms)]\",\n" +
                "            \"manageState\": \"未处理\",\n" +
                "            \"alarmType\": \"阀值告警\",\n" +
                "            \"productType\": \"BPM\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"businessName\": \"柜面2\",\n" +
                "            \"componentName\": [\n" +
                "                {\n" +
                "                    \"code\": \"03\",\n" +
                "                    \"msg\": \"succes3\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"code\": \"04\",\n" +
                "                    \"msg\": \"succes4\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"constraintDesc\": \"[6.1.12.27][响应时间(ms)]\",\n" +
                "            \"manageState\": \"未处理2\",\n" +
                "            \"alarmType\": \"阀值告警2\",\n" +
                "            \"productType\": \"BPM2\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        JSONObject jsonObject = JSONObject.parseObject(data);
        String path = "$.data[0:].componentName[0:].msg";

        recursionPath(path, jsonObject);
    }


    /**
     * 使用 jsonPath［n］递归解析json
     * $.data[0:] 代表  data是个数组，且data 中的 属性合并显示
     * $.data[n] 代表  data是个数组，且data 中的 属性单条显示
     */
    private static void recursionPath(String path, JSONObject jsonObject) {
        if (path.contains("[n]")) {
            int index = path.indexOf("[n]");
            String path1 = path.substring(0, index);
            for (int i = 0; i < JSONPath.size(jsonObject, path1); i++) {
                String path2 = path.substring(index + 3);
                if (path2.contains("[n]")) {
                    recursionPath(path1 + "[" + i + "]" + path2, jsonObject);
                } else {
                    System.out.println(path1 + "[" + i + "]" + path2 + "------------------------" + JSONPath.eval(jsonObject, path1 + "[" + i + "]" + path2));
                }
            }
        } else {
            System.out.println(path + "------------------------" + JSONPath.eval(jsonObject, path));
        }
    }
}
