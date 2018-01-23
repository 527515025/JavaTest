package com.us.redis;

import redis.clients.jedis.JedisPubSub;

/**
 * Created by yangyibo on 2018/1/23.
 */
public class MyJedisPubSub extends JedisPubSub {

    // 取得订阅的消息后的处理
    public void onMessage(String channel, String message) {
        System.out.println(channel + "=" + message);
    }

    // 初始化订阅时候的处理
    public void onSubscribe(String channel, int subscribedChannels) {
        // System.out.println(channel + "=" + subscribedChannels);
    }

    // 取消订阅时候的处理
    public void onUnsubscribe(String channel, int subscribedChannels) {
        // System.out.println(channel + "=" + subscribedChannels);
    }

    // 初始化按表达式的方式订阅时候的处理
    public void onPSubscribe(String pattern, int subscribedChannels) {
        // System.out.println(pattern + "=" + subscribedChannels);
    }

    // 取消按表达式的方式订阅时候的处理
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        // System.out.println(pattern + "=" + subscribedChannels);
    }

    // 取得按表达式的方式订阅的消息后的处理
    public void onPMessage(String pattern, String channel, String message) {
        System.out.println(pattern + "=" + channel + "=" + message);
    }
}
