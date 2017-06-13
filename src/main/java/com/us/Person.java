package com.us;

import com.us.demo.Reflect;

import java.util.List;

/**
 * Created by yangyibo on 16/12/26.
 */
public class Person {

    int age;
    String name;
    int sex;
    List<String>  friend;

    public List<String> getFriend() {
        return friend;
    }

    public void setFriend(List<String> friend) {
        this.friend = friend;
    }


    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;

    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", friend=" + friend +
                '}';
    }
}
