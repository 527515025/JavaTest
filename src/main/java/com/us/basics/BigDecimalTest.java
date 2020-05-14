package com.us.basics;

import java.math.BigDecimal;

/**
 * @author yyb
 * @time 2020/2/27
 */
public class BigDecimalTest {

    public static void compareToTest(){
        BigDecimal one = new BigDecimal(0);
        if(one.compareTo(new BigDecimal(0)) <= 0){
            System.out.println("小于等于");
        }
    }

    public static void fDiscount(){
        BigDecimal commodityPrice = new BigDecimal(2.9) ;
        BigDecimal pay = new BigDecimal(2.9) ;
        BigDecimal subsidy = new BigDecimal(0) ;
        BigDecimal freight = new BigDecimal(0) ;
        BigDecimal fDiscount = commodityPrice.subtract(pay).subtract(subsidy).divide(new BigDecimal(3), 6, BigDecimal.ROUND_HALF_UP);
        System.out.println(fDiscount);
    }


    public static void amount(){
        BigDecimal commodityPrice = new BigDecimal(2.9) ;
        BigDecimal amount = new BigDecimal(2) ;
        BigDecimal freight = new BigDecimal(2) ;
        BigDecimal commodityPrice1 = commodityPrice.multiply(amount).add(freight);
        System.out.println(commodityPrice1);
    }

    public static void main(String[] args) {

        amount();
    }
}
