package com.us.basics;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Created by yangyibo on 17/2/18.
 */
public class PipedStream {
    public static void main(String[] args) {
        try {
            piped(1);
        } catch (Exception e) {

        }
        System.out.println("主线程运行结束");
    }

    public static void piped(int count) throws Exception { //打印
        for (int i = 0; i < count; i++) {
            PipedInputStream pin = new PipedInputStream();
            PipedOutputStream pout = new PipedOutputStream();
            pin.connect(pout);  //输入流与输出流连接
            ReadThread readTh = new ReadThread(pin);
            WriteThread writeTh = new WriteThread(pout);
            new Thread(readTh).start();
            new Thread(writeTh).start();
        }
    }
}

class ReadThread implements Runnable {
    private PipedInputStream pin;

    ReadThread(PipedInputStream pin) {
        this.pin = pin;
    }

    public void run() //由于必须要覆盖run方法,所以这里不能抛,只能try
    {
        try {
            System.out.println(Thread.currentThread());
            System.out.println("R:读取前没有数据,阻塞中...等待数据传过来再输出到控制台...");
            byte[] buf = new byte[1024];
            int len = pin.read(buf);  //read阻塞
            System.out.println("R:读取数据成功,阻塞解除...");
            String s = new String(buf, 0, len);
            System.out.println(s);    //将读取的数据流用字符串以字符串打印出来
            pin.close();
        } catch (Exception e) {
            throw new RuntimeException("R:管道读取流失败!" + e);
        }
    }
}

class WriteThread implements Runnable {
    private PipedOutputStream pout;

    WriteThread(PipedOutputStream pout) {
        this.pout = pout;
    }

    public void run() {
        try {
            System.out.println(Thread.currentThread());
            System.out.println("W:开始将数据写入:但等个5秒让我们观察...");
            String str = "2017-02-16T18:51:00: Debug: D-JPR-000-000: Parsing events: Omegamon_Base;cms_hostname='itmserver';cms_port='3661';integration_type='U';master_reset_flag='';appl_label='';situation_name='opt_check';situation_type='S';situation_origin='itmserver:LZ';situation_time='02/16/2017 18:51:00.000';situation_status='N';situation_thrunode='TEMS_TEST';situation_fullname='data_check_waring';situation_displayitem='';source='ITM';sub_source='itmserver:LZ';hostname='itmserver';origin='192.168.100.50';adapter_host='itmserver';date='02/16/2017';severity='WARNING';msg='data directory >60';situation_eventdata='~';END\n";
            pout.write(str.getBytes());  //管道输出流
            pout.close();
        } catch (Exception e) {
            throw new RuntimeException("W:WriteThread写入失败... " + e);
        }
    }

}
