package com.us.java8;

/**
 * Created by yangyibo on 16/12/27.
 */
public interface Defaulable2 {

    void printName();

    default void printAge(){
        System.out.println(19);
    }
}
