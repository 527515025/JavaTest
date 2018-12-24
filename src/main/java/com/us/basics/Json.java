package com.us.basics;


import com.alibaba.fastjson.*;

import java.util.*;

/**
 * Created by yangyibo on 17/6/9.
 */
public class Json {
    public static void main(String[] args) {
//        System.out.println(newJsonStr());
//        jsonToMap(newJsonStr());
//        mapToJson(jsonToMap(newJsonStr()));
//        jsonPath();
//        recursion();
//        System.out.println(newJsonArray());
        jsonTest();
    }

    /**
     * {"name":"abel","age":21}
     *
     * @return
     */
    private static String newJsonStr() {
        JSONObject jb = new JSONObject();
        jb.put("name", "abel");
        jb.put("age", 23);
        jb.put("address", null);
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

    private static void mapToJson(Map<String, Object> map) {
        JSONObject json = JSONObject.parseObject(map.toString());
        System.out.println("to json String : " + json.toJSONString());
    }


    private static void printMap(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println("key: " + entry.getKey() + " value : " + entry.getValue());
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
        String data = "[{\n" +
                "    \"code\": \"0\",\n" +
                "    \"msg\": \"success\",\n" +
                "    \"exception\": \"exception1\",\n" +
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
                "}]";

        String path = "code=$.data[n].componentName[n].code##msg=$.data[n].componentName[n].msg+$.data[n].businessName##businessName=$.data[n].businessName##state=$.data[n].manageState##exception=$.exception";
        JSONArray jsonArray = JSONArray.parseArray(data);
        for (Object obj : jsonArray) {
            JSONObject jsonObject = JSONObject.parseObject(obj.toString());
            mark(path, jsonObject);
        }
//        JSONObject jsonObject = JSONObject.parseObject(data);
//        recursionPath(path, jsonObject);
    }


    /**
     * @param path
     * @param jsonObject
     */
    private static void mark(String path, JSONObject jsonObject) {
        String[] paths = path.split("##");
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < paths.length; i++) {//i 属性
            String[] subPath = paths[i].split("=");
            if (subPath[1].contains("+")) {
                String[] moreSubPath = subPath[1].split("\\+");
                for (String str : moreSubPath) {
                    map = mapPutALl(recursionPath(str, jsonObject), map, subPath[0]);
                }
            } else {
                map = mapPutALl(recursionPath(subPath[1], jsonObject), map, subPath[0]);
            }
        }
        analysisMap(map);
        printMap(map);
        System.out.println("end");
    }

    /**
     * 使用 jsonPath［n］递归解析json
     * $.data[0:] 代表  data是个数组，且data 中的 属性合并显示
     * $.data[n] 代表  data是个数组，且data 中的 属性单条显示
     */
    private static Map<String, Object> recursionPath(String path, JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<>();
        if (path.contains("[n]")) {
            int index = path.indexOf("[n]");
            String frontPath = path.substring(0, index);
            for (int i = 0; i < JSONPath.size(jsonObject, frontPath); i++) {
                String afterPath = path.substring(index + 3);
                if (afterPath.contains("[n]")) {
                    map.putAll(recursionPath(frontPath + "[" + i + "]" + afterPath, jsonObject));
                } else {
                    map.put(frontPath + "[" + i + "]", JSONPath.eval(jsonObject, frontPath + "[" + i + "]" + afterPath).toString());
//                    System.out.println(frontPath + "[" + i + "]" +":------"+JSONPath.eval(jsonObject, frontPath + "[" + i + "]" + afterPath).toString());
                }
            }
        } else {
            //以最后一个点分割放入map
            map.put(path.substring(0, path.lastIndexOf(".")), JSONPath.eval(jsonObject, path));
//            System.out.println(JSONPath.eval(jsonObject, path).toString());
        }
        return map;
    }

    /**
     * 将新解析的数据放到 原有数据的map 中，按照关系路径合并
     *
     * @param mapOld
     * @param mapNow
     * @param field
     * @return
     */
    private static Map<String, Object> mapPutALl(Map<String, Object> mapOld, Map<String, Object> mapNow, String field) {
        boolean flag = true;
        for (Map.Entry<String, Object> entry : mapOld.entrySet()) {
            if (null != mapNow.get(entry.getKey())) {
                if (mapNow.get(entry.getKey()).toString().contains("#;#" + field + "#:#")) {
                    // 说明是属性相加 ＝ epp 的一个属性
                    mapNow.put(entry.getKey(), mapNow.get(entry.getKey()) + ";" + entry.getValue());
                } else {
                    mapNow.put(entry.getKey(), mapNow.get(entry.getKey()) + "#;#" + field + "#:#" + entry.getValue());
                }
            } else {
                for (Map.Entry<String, Object> entryNow : mapNow.entrySet()) {
                    if (entryNow.getKey().contains(entry.getKey())) {
                        if (entryNow.getValue().toString().contains("#;#" + field + "#:#")) {
                            mapNow.put(entryNow.getKey(), entryNow.getValue() + ";" + entry.getValue());
                        } else {
                            mapNow.put(entryNow.getKey(), entryNow.getValue() + "#;#" + field + "#:#" + entry.getValue());
                        }
                        flag = false;
                    }
                }
                if (flag) {
                    mapNow.put(entry.getKey(), field + "#:#" + entry.getValue());
                    flag = true;
                }
            }

        }
        return mapNow;
    }


    private static String analysisMap(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Map<String, Object> eventMap = new HashMap<>();
            Arrays.stream(entry.getValue().toString().split("#;#")).forEach(x -> {
                String[] keyAndValue = x.split("#:#");
                eventMap.put(keyAndValue[0], keyAndValue[1]);
            });
            convertEvent(eventMap);
        }
        return null;
    }

    private static void convertEvent(Map<String, Object> map) {
        printMap(map);
        System.out.println("--------------------------------------------");
    }


    private static void jsonTest() {
        String test = "{\"_os\":\"Android\",\"_locale\":\"zh_CN\",\"_app_version\":\"4.7.6\",\"_device\":\"Best_sonny_LT580\",\"_density\":\"XXHDPI\",\"_resolution\":\"1080x1776\",\"_os_version\":\"5.1\"}";
        try{
            Map<String, Object> map = JSONObject.parseObject(test);
        }catch (JSONException e) {
            System.out.println("不是json串！！！");
        }
        System.out.println();

    }
}
