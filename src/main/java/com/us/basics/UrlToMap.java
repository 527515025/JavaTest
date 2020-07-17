package com.us.basics;


import com.us.bean.Person;
import org.mortbay.util.MultiMap;
import org.mortbay.util.UrlEncoded;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yangyibo on 2018/12/13.
 */
public class UrlToMap {
    public static void main(String[] args) {
//        urlToMap();
//        removeRepeat(init());
        splitKeyAndVauleToMap();
    }

    private static void urlToMap() {
        String content = "gid=02054896c951cfb4133f141074b01708c44;appid=1;plat=1;sver=5965;sys=android;sysver=7.1.1;pn=miao-114;mfo=OPPO;mfov=OPPO+A73;";
        MultiMap multiMap = new MultiMap();
        UrlEncoded.decodeTo(content, multiMap, "UTF-8");
        System.out.println();

    }

    private static void splitKeyAndVauleToMap() {
        String content = "appid=1; plat=1; pn=miao-yqhy-1; gid=390675019";
        if (content.contains(";")) {
            String[] arr = content.split(";");
            Map<String, String> map = new HashMap<>();
            Arrays.stream(arr).forEach(x -> {
                if (x.contains("=")) {
                    String[] keyAndValue = x.split("=");
                    if (keyAndValue.length > 1){
                        map.put(keyAndValue[0].trim(), keyAndValue[1].trim());
                    }
                }
            });
            System.out.println(map.toString());
        }
    }


    private static List<Person> init() {

        List<Person> personList = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            Person p = new Person();
            p.setAge(i);
            p.setName("token=12312312312&profile_id=123123123123&order_sn=22222222222" + i + "&is_subsidy=-1&is_wallet_balance=-1&pay_method=wechat&order_source=1");
            personList.add(p);
        }

        Person p = new Person();
        p.setAge(15);
        p.setName("token=123123123123&profile_id=213123123&order_sn=2222222222222&is_subsidy=-1&is_wallet_balance=-1&pay_method=wechat&order_source=1");
        personList.add(p);
        return personList;
    }


    private static List<Person> removeRepeat(List<Person> businessLogs) {
        Map<String, Integer> map = new HashMap<>();
        String regex = "&order_sn=(.*?&)";
        Pattern p = Pattern.compile(regex);
        for (int i = 0; i < businessLogs.size(); i++) {
            Matcher m = p.matcher(businessLogs.get(i).getName());
            if (m.find()) {
                if (null != map.get(m.group(0)) && map.get(m.group(0)) > 0) {
                    businessLogs.remove(i);
                } else {
                    map.put(m.group(0), businessLogs.get(i).getAge());
                }
            }
        }
        return businessLogs;
    }
}


