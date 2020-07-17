package com.us.java8;


import com.us.bean.Person;

import java.util.Optional;

/**
 * Created by yangyibo on 16/12/27.
 */
public class DefaulableImpl implements Defaulable {
    @Override
    public void printName() {
        System.out.println("abel");
    }

    public static void main(String[] args) {
//        Defaulable d = Defaulable.create(DefaulableImpl::new);
////        Defaulable d=new DefaulableImpl();
//        d.printName();
//        d.printAge();
//        Defaulable.printSex();

        Person person = new Person();
//        person = null;
        Boolean flag = Optional.ofNullable(person).isPresent();
        System.out.println("Full Name is set? " + flag);


    }
}
