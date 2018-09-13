package com.us.basics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;

/**
 * Created by yangyibo on 17/11/3.
 request.body.mapping=agentHost=$.data[n].componentName##description=$.data[n].componentName+$.data[n].constraintDesc##manager=$.data[n].productType##alertType=$.data[n].alarmType+$.data[n].componentName##eventTime=$.data[n].alarmTime##severity=$.data[n].alarmLevel
 */
public class JsonToEventConterver {

    /**
     * json 转化为 event
     *
     * @param path
     * @param jsonObject
     */
    public static List<?> jsonConterver(String path, JSONObject jsonObject, Integer addSecond, String datePattern) {
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
        return analysisMap(map, addSecond, datePattern);
    }

    /**
     * 使用 jsonPath［n］递归解析json
     * $.data   如果data是个数组，则data 中的 属性合并显示
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
                }
            }
        } else {
            //以最后一个点分割放入map
            map.put(path.substring(0, path.lastIndexOf(".")), JSONPath.eval(jsonObject, path));
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


    /***
     * 将解析出来的每条事件放到一个map 中
     * @param map
     * @return
     */
    private static List<?> analysisMap(Map<String, Object> map, Integer addSecond, String datePattern) {
        List<?> list = new ArrayList<>();
//        EventAgentDTO eventDTO = new EventAgentDTO();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Map<String, Object> eventMap = new HashMap<>();
            Arrays.stream(entry.getValue().toString().split("#;#")).forEach(x -> {
                String[] keyAndValue = x.split("#:#");
                eventMap.put(keyAndValue[0], keyAndValue[1]);
            });
//            list.add(convertEvent(eventMap, eventDTO, addSecond, datePattern));
        }
        return list;
    }

    /**
     * 将map 转化为eventDto
     *
     * @param map
     */
    private static void convertEvent(Map<String, Object> map, Object eventDTO, Integer addSecond, String datePattern) {
//        EventAgentDTO result = eventDTO.clone();
//        result.setAgentHost(judgeMap(map, "agentHost"));
//        result.setAgentSubcategory(judgeMap(map, "agentSubcategory"));
//        result.setAlertType(judgeMap(map, "alertType"));
//        result.setDescription(judgeMap(map, "description"));
//        result.setSeverity(judgeMap(map, "severity"));
//        if (null != addSecond) {
//            result.setEventTime(stringToDate(judgeMap(map, "eventTime"), datePattern, addSecond));
//        }else {
//            result.setEventTime(stringToDate(judgeMap(map, "eventTime"), datePattern, addSecond));
//        }
//        result.setInternalLastEventTime(null == map.get("internalLastEventTime") ?
//                stringToDate(judgeMap(map, "eventTime"), datePattern, null) : stringToDate(map.get("internalLastEventTime").toString(), datePattern, null));
//
//        result.setEventType(null == map.get("eventType") ? "1" : map.get("eventType").toString());
//        result.setCount(Integer.valueOf(null == map.get("count") ? "1" : map.get("count").toString()));
//        result.setManager(null == map.get("manager") ? "RestApi" : map.get("manager").toString());
//        result.setSource(null == map.get("source") ? "RestApi-agent" : map.get("source").toString());
//        result.setAgentName(null == map.get("agentName") ? "RestApi-agent" : map.get("agentName").toString());
//        return result;
    }

    private static Date stringToDate(String dateStr, String datePattern, Integer addSeconds) {
//        return CommonUtil.dateAddSeconds(CommonUtil.parseDateWithStyle(dateStr, datePattern), addSeconds);
        return null;
    }

    private static String judgeMap(Map<String, Object> map, String key) {
        return null == map.get(key) ? null : map.get(key).toString();
    }


    public static void main(String[] args) {
        String str = " request.body.mapping=agentHost=$.data[n].componentName##description=$.data[n].componentName+$.data[n].constraintDesc##manager=$.data[n].productType##alertType=$.data[n].alarmType+$.data[n].componentName##eventTime=$.data[n].alarmTime##severity=$.data[n].alarmLevel";
//        jsonConterver(str,);
    }
}
