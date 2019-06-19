package com.us.geoip;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.*;

import java.io.File;
import java.net.InetAddress;

/**
 * @author yyb
 * @time 2019/6/19
 */
public class GeoipTest {

    public static void main(String[] args) throws Exception {
        //GeoIP2-City 数据库文件D
        File database = new File("/Users/yangyibo/test/GeoLite2-City/GeoLite2-City.mmdb");

        // 创建 DatabaseReader对象
        DatabaseReader reader = new DatabaseReader.Builder(database).build();

        InetAddress ipAddress = InetAddress.getByName("60.184.36.182");

        // 获取查询结果
        CityResponse response = reader.city(ipAddress);

        // 获取国家信息
        Country country = response.getCountry();
        System.out.println("国家code:"+country.getIsoCode());
        System.out.println("国家:"+country.getNames().get("zh-CN"));

        // 获取省份
        Subdivision subdivision = response.getMostSpecificSubdivision();
        System.out.println("省份code:"+subdivision.getIsoCode());
        System.out.println("省份:"+subdivision.getNames().get("zh-CN"));

        //城市
        City city = response.getCity();
        System.out.println("城市code:"+city.getGeoNameId());
        System.out.println("城市:"+city.getName());

        // 获取城市
        Location location = response.getLocation();
        System.out.println("经度:"+location.getLatitude());
        System.out.println("维度:"+location.getLongitude());

    }



    //https://blog.csdn.net/huiyanshizhen21/article/details/85201638
}
