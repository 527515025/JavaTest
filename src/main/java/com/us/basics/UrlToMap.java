package com.us.basics;

import com.us.Person;
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
        removeRepeat(init());
//        splitKeyAndVauleToMap();
    }

    private static void urlToMap() {
        String content = "token=7a2af1b4dad21384f203102d95e48859&profile_id=34289671&order_sn=O01090801048eb27BF8E3&is_subsidy=-1&is_wallet_balance=-1&pay_method=wechat&order_source=1";
        MultiMap multiMap = new MultiMap();
        UrlEncoded.decodeTo(content, multiMap, "UTF-8");
        System.out.println();

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


