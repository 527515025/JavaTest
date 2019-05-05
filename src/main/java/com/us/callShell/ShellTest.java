package com.us.callShell;

import org.apache.commons.lang3.ArrayUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Created by yangyibo on 17/11/29.
 * 脚本要有可执行权限 chmod +x  file 使用第二个call 则不需要添加可执行权限
 */
public class ShellTest {
    public static void main(String[] args) {
//        execShell(true, "{\"date\":\"2017-11-29 14:40:54\",\"severity\":\"MAJOR\",\"mc_object\":\"CELL:10.193.22.239:182\",\"msg\":\"Cell on 10.193.22.239:1828 is disconnected from Integration Server 10.193.16.183:12125.\",\"mc_appname\":\"\",\"mc_tool_class\":\"NGMS_BPPM\",\"mc_host_address\":\"10.193.16.183\",\"event_handle\":\"1118702\",\"AREA\":\"田林中心\",\"mc_parameter_value\":\"85.06\",\"mc_parameter\":\"CPUprcrProcessorTimePercent\",\"mc_object_class\":\"PROACTIVENET_COMPONENT\",\"SOURCE_IP\":\"10.193.16.176\",\"OCTOPUS\":\"34\",\"updatetime\":\"2017-11-29 16:29:55\",\"status\":\"BLACKOUT\"} ");
        execShell("/Users/yangyibo/Desktop/login.sh", "2019-01-04", "2015-03-25", "miao-136", "sdfs234234m23j423423j4h23j", "18400293887", "156.23.45.22");
    }

    /**
     * 执行shell
     * 将参数拼接在 路径之后
     * @param para   传入参数
     */
    private static void execShell(boolean xx, String... para) {
        StringBuffer paras = new StringBuffer();
        Arrays.stream(para).forEach(x -> paras.append(x).append(" "));
        try {
            String cmd = "", shpath = "";

            shpath = "/Users/yangyibo/Desktop/callShell.sh";
            cmd = shpath + " " + paras.toString();
            Process ps = Runtime.getRuntime().exec(cmd);
            ps.waitFor();

            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
            System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解决了 参数中包含 空格和脚本没有执行权限的问题
     *
     * @param scriptPath 脚本路径
     * @param para       参数数组
     */
    private static void execShell(String scriptPath, String... para) {
        try {
            String[] cmd = new String[]{scriptPath};
            //为了解决参数中包含空格
            cmd = ArrayUtils.addAll(cmd, para);

            //解决脚本没有执行权限
            ProcessBuilder builder = new ProcessBuilder("/bin/chmod", "755", scriptPath);
            Process process = builder.start();
            process.waitFor();

            Process ps = Runtime.getRuntime().exec(cmd);
            ps.waitFor();

            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            //执行结果
            String result = sb.toString();
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
