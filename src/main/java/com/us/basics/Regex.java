package com.us.basics;

import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yangyibo on 17/1/7.
 */
public class Regex {
    public static void main(String[] args) throws ParseException {

//        pattern2();
//        pattern3();
//        compress();
//        zybCompress();
//        zabbixSplit();
//        zabbixSplit2();
//        solarwinds();
        getSn();
    }

    private static void pattern1() {

        String body = "2017-01-06T11:32:48: Debug: D-UNK-000-000: [Event Processor] EventSeqNo:\t1\n" +
                "2017-01-06T11:32:48: Debug: D-UNK-000-000: [Event Processor] Processing alert {0 remaining}\n" +
                "2017-01-06T11:32:48: Debug: D-UNK-000-000: <<<<< Entering.............. ITM Rules File ................... >>>>>\n" +
                "2017-01-06T11:32:48: Debug: D-UNK-000-000: <<<<< Leaving............... ITM Rules File ................... >>>>>\n" +
                "2017-01-06T11:32:48: Debug: D-UNK-000-000: Rules file processing took 332 usec.\n" +
                "2017-01-06T11:32:48: Debug: D-UNK-000-000: Flushing events to object servers\n" +
                "2017-01-06T11:32:48: Debug: D-UNK-000-000: 1 buffered alerts\n" +
                "\n" +
                "2017-01-06T11:33:18: Debug: D-JPR-000-000: Parsing events: Omegamon_Base;cms_hostname='itmserver';cms_port='37076';integration_type='U';master_reset_flag='';appl_label='';situation_name='disk';situation_type='S';situation_origin='itmserver:LZ';situation_time='01/06/2017 11:33:23.000';situation_status='N';situation_thrunode='TEMS_TEST';situation_fullname='home_disk_error';situation_displayitem='';source='ITM';sub_source='itmserver:LZ';hostname='itmserver';origin='192.168.100.50';adapter_host='itmserver';date='01/06/2017';severity='CRITICAL';msg='itm server home directory > 80%';situation_eventdata='~';END\n" +
                "2017-01-06T11:33:18: Debug: D-UNK-000-000: [Event Processor] EventString:\tOmegamon_Base;\n" +
                "   adapter_host='itmserver';\n" +
                "   cms_hostname='itmserver';\n" +
                "   situation_type='S';\n" +
                "   situation_eventdata='~';\n" +
                "   integration_type='U';\n" +
                "   situation_displayitem='';\n" +
                "   msg='itm server home directory > 80%';\n" +
                "   sub_source='itmserver:LZ';\n" +
                "   situation_time='01/06/2017 11:33:23.000';\n" +
                "   situation_thrunode='TEMS_TEST';\n" +
                "   master_reset_flag='';\n" +
                "   appl_label='';\n" +
                "   hostname='itmserver';\n" +
                "   situation_fullname='home_disk_error';\n" +
                "   cms_port='37076';\n" +
                "   situation_status='N';\n" +
                "   source='ITM';\n" +
                "   severity='CRITICAL';\n" +
                "   situation_origin='itmserver:LZ';\n" +
                "   date='01/06/2017';\n" +
                "   situation_name='disk';\n" +
                "   origin='192.168.100.50';\n" +
                "END\n" +
                "\n" +
                "2017-01-06T11:33:18: Debug: D-UNK-000-000: [Event Processor] ClassName:\tOmegamon_Base\n" +
                "2017-01-06T11:33:18: Debug: D-UNK-000-000: [Event Processor] adapter_host:\titmserver\n" +
                "2017-01-06T11:33:18: Debug: D-UNK-000-000: [Event Processor] cms_hostname:\titmserver";

        String bodyRegex = "";
        String pattern6 = "(Parsing events)(.*)(END)";

        Pattern r6 = Pattern.compile(pattern6);
        Matcher m6 = r6.matcher(body);
        if (m6.find()) {
            bodyRegex = m6.group(0);
        }

        System.out.println(bodyRegex);
    }


    private static void pattern2() {
        String body = "2017-01-06T11:33:18: Debug: D-JPR-000-000: Parsing events: Omegamon_Base;cms_hostname='itmserver';cms_port='37076';integration_type='U';master_reset_flag='asdf';appl_label='';situation_name='disk';situation_type='S';situation_origin='itmserver:LZ';situation_time='01/06/2017 11:33:23.000';situation_status='N';situation_thrunode='TEMS_TEST';situation_fullname='home_disk_error';situation_displayitem='';source='ITM';sub_source='itmserver:LZ';hostname='itmserver';origin='192.168.100.50';adapter_host='itmserver';date='01/06/2017';severity='CRITICAL';msg='itm server home directory > 80%';situation_eventdata='~';END\n";
        String bodyRegex;
        String pattern = "cms_hostname=.([^;]*).;cms_port=.([^;]*).;integration_type=.([^;]*).;master_reset_flag=.([^;]*).;";
//        String pattern2 = "((\\w)*)=([^;]*);";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(body);
        if (m.find() && m.group(4) != null && m.group(3) != null && m.group(2) != null && m.group(1) != null) {
            bodyRegex = m.group(1);
            System.out.println(bodyRegex + "-----" + m.group(2) + "-----" + m.group(3) + "-----" + m.group(4));
        }
    }

    private static void pattern3() {
        String body = "cmbms.log";

        String bodyRegex;
        String pattern = "cmbms.log.*";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(body);


        if (m.find()) {
            bodyRegex = m.group();
            System.out.println(bodyRegex);
        }
    }

    private static void compress() {
        String bodyRegex;
        String str = "2017-02-16T18:46:00: Debug: D-JPR-000-000: Parsing events: Omegamon_Base;cms_hostname='itmserver';cms_port='3661';integration_type='U';master_reset_flag='';appl_label='';situation_name='opt_check';situation_type='S';situation_origin='itmserver:LZ';situation_time='02/16/2017 18:46:00.000';situation_status='Y';situation_thrunode='TEMS_TEST';situation_fullname='data_check_waring';situation_displayitem='';source='ITM';sub_source='itmserver:LZ';hostname='itmserver';origin='192.168.100.50';adapter_host='itmserver';date='02/16/2017';severity='WARNING';msg='data directory >60';situation_eventdata='~';END";
        String pattern = "Parsing events:(.*)END";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        Map<String, String> map = new HashMap<>();
        if (m.find()) {
            bodyRegex = m.group(1);
            System.out.println(bodyRegex + "-------------\n\n");
            try {
                String[] args = bodyRegex.split(";");
                Arrays.stream(args).forEach(x -> {
                    if (x.contains("=")) {
                        String[] keyAndValue = x.split("=");
                        map.put(keyAndValue[0], keyAndValue[1].replace("'", ""));
                    }
                });
            } catch (Exception ex) {
                System.out.println("111");
            }
            System.out.println("通过Map.keySet遍历key和value：");
            for (String key : map.keySet()) {
                System.out.println(key + " ----------" + map.get(key));
            }
        }
    }

    public static void zybCompress() {
        List<String> list = new ArrayList<>();
        String str = "\"\":\"1qwsdf\"##\"\":2##\"\":\"3qwsdf\"##\"\":\"4qwsdf\"##\"\":\"5qwsdf\"##\"\":\"6qwsdf\"##\"\":\"7qwsdf\"##\"\":\"8qwsdf\"##\"\":9##\"\":\"10qwsdf\"##\"\":\"11qwsdf\"##\"\":\"12qwsdf\"##\"\":\"13qwsdf\"##\"\":\"14qwsdf\"##\"\":15##\"\":16##\"\":17##\"\":18##\"\":\"19qwsdf\"";
        String[] s = str.split("##");
        System.out.println("length: " + s.length);
        Arrays.stream(s).forEach(x -> {
            if (x.contains(":")) {
                x = x.substring(x.indexOf(":") + 1).replace("\"", "");
                System.out.println(x);
                list.add(x);
            }
        });
        System.out.println(list.get(18));
    }

    public static void zabbixSplit() {
        String raw_event;
        String content = "ZABBIX##Trigger_name:#abelProcessor load is too high on 35##Trigger_status:# PROBLEM##Trigger_severity:# High##Trigger_URL:# 192.168.100.35##Host_name :# 35##Host_ip:# 192.168.100.35##Events_time :#10:09:23##Item_name :#Processor load (1 min average per core)##Item_values:#1. Processor load (1 min average per core) (35:system.cpu.load[percpu,avg1]): 0.086426\n";
        Map<String, String> map = new HashMap<>();
        String[] args = content.split("##");
        Arrays.stream(args).forEach(x -> {
            if (x.contains(":#")) {
                String[] keyAndValue = x.split(":#");
                map.put(keyAndValue[0].trim(), keyAndValue[1].trim());
            }
        });
        for (String key : map.keySet()) {
            System.out.println(key + " ----------" + map.get(key));
        }
    }

    public static void zabbixSplit2() {
        String raw_event;
        String content = "sdfsdfsdsdf2342423f##key##ZABBIX##Trigger_name:#abelProcessor load is too high on 35##Trigger_status:# PROBLEM##Trigger_severity:# High##Trigger_URL:# 192.168.100.35##Host_name :# 35##Host_ip:# 192.168.100.35##Events_time :#10:09:23##Item_name :#Processor load (1 min average per core)##Item_values:#1. Processor load (1 min average per core) (35:system.cpu.load[percpu,avg1]): 0.086426\n";
        String[] args = content.split("##key##");
        Arrays.stream(args).forEach(x -> System.out.println(x));
    }


    public static void solarwinds() {
        String regex = "^:防火墙(([A-Z]){3}-([A-Z]){3}-([A-Z]){2}-([A-Z])*\\d*)";
        String content = ":防火墙DCC-ECC-FW-OASRX14引擎";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        if (m.find()) {
            System.out.println(m.group(0)+" --------- "+m.group(1));
        }
    }

    /**
     * 获取 &order_sn=O01090801048eb27BF8E3
     */
    public static void getSn(){
        String regex = "&order_sn=(.*?&)";
        String content = "token=234234234&profile_id=23423423&order_sn=234234234234234&is_subsidy=-1&is_wallet_balance=-1&pay_method=wechat&order_source=1";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        if (m.find()) {
            System.out.println(m.group(0)+" --------- "+m.group(1));
        }
    }
}

