package com.us;

import com.us.demo.Reflect;

/**
 * Created by yangyibo on 16/12/26.
 */
public class Person {

    int age;
    String name;
    int sex;


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

//    @Override
//    public String toString() {
//        return  "name: "+this.getName()+"  --AGE: "+this.getAge() +"  --SEX: "+this.getSex();
//    }

    @Override
    public String toString(){
        return Reflect.getString(this,this.getClass( ));
    }
}
