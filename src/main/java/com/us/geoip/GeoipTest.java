package com.us.geoip;

import com.fasterxml.jackson.databind.JsonNode;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.*;

import java.io.File;
import java.io.IOException;

import com.maxmind.db.Reader;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.maxmind.db.Reader.FileMode;

import com.maxmind.db.CHMCache;

/**
 * https://github.com/maxmind/MaxMind-DB-Reader-java
 * <p>
 * Java 通过ip 获取 地理位置
 *
 * @author yyb
 * @time 2019/6/19
 */
public class GeoipTest {
    private static volatile DatabaseReader readerIp;

    public static void main(String[] args) throws Exception {
//        noCache("1.180.164.207");
//        haveCache("1.180.164.207");
    }

    private static void haveCache(String ip) throws IOException {
        File database = new File("/Users/yangyibo/test/GeoLite2-City/GeoLite2-City.mmdb");
        Reader r = new Reader(database, FileMode.MEMORY_MAPPED, new CHMCache());
        InetAddress ipAddress = InetAddress.getByName(ip);
        // 获取查询结果
        JsonNode response = r.get(ipAddress);
        //国家
        System.out.println(response.get("country").get("iso_code"));
        System.out.println(response.get("country").get("geoname_id"));
        System.out.println(response.get("country").get("names").get("zh-CN"));

        //省
        System.out.println(response.get("subdivisions").get("iso_code"));
        System.out.println(response.get("subdivisions").get("geoname_id"));
        System.out.println(response.get("subdivisions").get("names").get("zh-CN"));

        //城市
        System.out.println(response.get("city").get("iso_code"));
        System.out.println(response.get("city").get("geoname_id"));
        System.out.println(response.get("city").get("names").get("zh-CN"));

        //经纬度
        System.out.println(response.get("location").get("latitude"));
        System.out.println(response.get("location").get("longitude"));

    }


    private static String noCache(String ip) throws Exception {

        DatabaseReader reader = GeoipTest.getIpInstance();
        InetAddress ipAddress = InetAddress.getByName(ip);

        // 获取查询结果
        CityResponse response = reader.city(ipAddress);

        // 获取国家信息
        Country country = response.getCountry();
        System.out.println("国家code:" + country.getIsoCode());
        System.out.println("国家:" + country.getNames().get("zh-CN"));

        // 获取省份
        Subdivision subdivision = response.getMostSpecificSubdivision();
        System.out.println("省份code:" + subdivision.getIsoCode());
        System.out.println("省份:" + subdivision.getNames().get("zh-CN"));

        //城市
        City city = response.getCity();
        System.out.println("城市code:" + city.getGeoNameId());
        System.out.println("城市:" + city.getNames().get("zh-CN"));

        // 获取经纬度
        Location location = response.getLocation();
        System.out.println("经度:" + location.getLatitude());
        System.out.println("维度:" + location.getLongitude());
        return subdivision.getNames().get("zh-CN") + "--";
    }


    /**
     * 获取数据库连接对象，单例
     *
     * @return
     */
    public static DatabaseReader getIpInstance() throws IOException {
        if (readerIp == null) {
            synchronized (GeoipTest.class) {
                if (readerIp == null) {
                    File database = new File("/Users/yangyibo/test/GeoLite2-City/GeoLite2-City.mmdb");
                    readerIp = new DatabaseReader.Builder(database).build();
                }
            }
        }
        return readerIp;
    }

}
