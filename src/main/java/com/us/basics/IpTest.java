package com.us.basics;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yangyibo on 2018/12/28.
 */
public class IpTest {
    /**
     * IP转换为整数
     * @param ip
     * @return
     */
    public static final long ipToLong(final String ip) {
        Pattern r = Pattern.compile("(\\d{1,3}\\.){3}\\d{1,3}");
        Matcher m = r.matcher(ip);
        if (!m.find()) {
            throw new IllegalArgumentException("[" + ip + "]不是有效的ip地址");
        }
        final String[] ipNums = ip.split("\\.");
        return (Long.parseLong(ipNums[0]) << 24)
                + (Long.parseLong(ipNums[1]) << 16)
                + (Long.parseLong(ipNums[2]) << 8)
                + (Long.parseLong(ipNums[3]));

    }
}
