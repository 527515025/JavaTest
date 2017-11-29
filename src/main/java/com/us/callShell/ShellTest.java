package com.us.callShell;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Created by yangyibo on 17/11/29.
 * 脚本要有可执行权限 chmod +x  file
 */
public class ShellTest {
    public static void main(String[] args) {
        execShell(false, "654654", "jgjhgjhg");
    }

    private static void execShell(boolean execCmd, String... para) {
        StringBuffer paras = new StringBuffer();
        Arrays.stream(para).forEach(x -> paras.append(x).append(" "));
        try {
            String cmd = "", shpath = "";
            if (execCmd) {
                shpath = "sh /Users/yangyibo/Desktop/callShell.sh" + " " + paras.toString();
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
}
