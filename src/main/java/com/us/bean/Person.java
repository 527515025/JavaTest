package com.us.bean;

import java.util.List;

/**
 * @author yyb
 * @time 2019/9/26
 */
public class Person {
    int age;
    String name;
    int sex;
    String group;
    List<String> friend;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<String> getFriend() {
        return friend;
    }

    public void setFriend(List<String> friend) {
        this.friend = friend;
    }
}
