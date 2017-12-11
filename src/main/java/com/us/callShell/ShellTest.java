package com.us.callShell;

import org.apache.commons.lang3.ArrayUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Created by yangyibo on 17/11/29.
 * 脚本要有可执行权限 chmod +x  file
 */
public class ShellTest {
    public static void main(String[] args) {
        execShell(true, "{\"date\":\"2017-11-29 14:40:54\",\"severity\":\"MAJOR\",\"mc_object\":\"CELL:10.193.22.239:182\",\"msg\":\"Cell on 10.193.22.239:1828 is disconnected from Integration Server 10.193.16.183:12125.\",\"mc_appname\":\"\",\"mc_tool_class\":\"NGMS_BPPM\",\"mc_host_address\":\"10.193.16.183\",\"event_handle\":\"1118702\",\"AREA\":\"田林中心\",\"mc_parameter_value\":\"85.06\",\"mc_parameter\":\"CPUprcrProcessorTimePercent\",\"mc_object_class\":\"PROACTIVENET_COMPONENT\",\"SOURCE_IP\":\"10.193.16.176\",\"OCTOPUS\":\"34\",\"updatetime\":\"2017-11-29 16:29:55\",\"status\":\"BLACKOUT\"} ");
    }

    private static void execShell(boolean execCmd, String... para) {
        StringBuffer paras = new StringBuffer();
        Arrays.stream(para).forEach(x -> paras.append(x).append(" "));
        try {
            String cmd = "", shpath = "";
            if (execCmd) {
                shpath = "echo";
            } else {
                shpath = "/Users/yangyibo/Desktop/callShell.sh";

            }
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

    private void execShell(String scriptPath, String ... para) {
        try {
            String[] cmd = new String[]{scriptPath};
            cmd=ArrayUtils.addAll(cmd,para);

            ProcessBuilder builder = new ProcessBuilder("/bin/chmod", "755",scriptPath);
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
