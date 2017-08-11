package com.us.java8;

import java.util.function.Supplier;

/**
 * Created by yangyibo on 16/12/27.
 */
public interface Defaulable {

    void printName();

    default void printAge(){
        System.out.println(19);
    }

     static void printSex(){
        System.out.println("女");
    }

    static Defaulable create(Supplier<Defaulable> supplier){
        return supplier.get();
    }

}
