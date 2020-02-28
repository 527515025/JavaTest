package com.us.basics;

import java.math.BigDecimal;

/**
 * @author yyb
 * @time 2020/2/27
 */
public class BigDecimalTest {

    public static void main(String[] args) {
        BigDecimal commodityPrice = new BigDecimal(15) ;
        BigDecimal pay = new BigDecimal(4) ;
        BigDecimal subsidy = new BigDecimal(1) ;
        BigDecimal fDiscount = commodityPrice.subtract(pay).subtract(subsidy).divide(new BigDecimal(3), 6, BigDecimal.ROUND_HALF_UP);

        System.out.println(fDiscount);
    }
}
