package com.us.spi;

import java.util.ServiceLoader;

/**
 * SPI全称Service Provider Interface，是Java提供的一套用来被第三方实现或者扩展的API，它可以用来启用框架扩展和替换组件。
 * Java SPI 实际上是“基于接口的编程＋策略模式＋配置文件”组合实现的动态加载机制。
 * @author yyb
 * @time 2020/4/13
 */
public class JavaSPITest {

    
    public static void main(String[] args) {
        ServiceLoader<Robot> serviceLoader = ServiceLoader.load(Robot.class);
        System.out.println("Java SPI");
        serviceLoader.forEach(Robot::sayHello);
    }
}
