package com.us.acm;

/**
 * @author yyb
 * @time 2020/11/4
 */
public class StockProfitTest {
    public static void main(String[] args) {
        System.out.println(maxProfit(init()));
        System.out.println(maxProfit2(init()));

    }

    private static int[] init() {
//        int[] arrayToSort = {2, 68, 34, 98, 7, 37, 5, 8, 3, 10, 1, 33, 76, 23, 94, 31, 67, 97, 35, 38};
        int[] arrayToSort = {9, 4, 3, 5, 6, 8, 5, 10};
        return arrayToSort;
    }

    /**
     * 买卖股票的最佳时机
     * 给定一个数组，它的第 i 个元素是一支给定股票第 i 天的价格。
     * <p>
     * 如果你最多只允许完成一笔交易（即买入和卖出一支股票一次），设计一个算法来计算你所能获取的最大利润。
     * <p>
     * 注意：你不能在买入股票前卖出股票。
     * <p>
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     * 思想：记录最小值，获取最大差值
     *
     * @param prices
     * @return
     */
    public static int maxProfit(int[] prices) {
        int k = Integer.MAX_VALUE, result = 0;
        for (int i = 0; i < prices.length; i++) {
            if (prices[i] < k) {
                k = prices[i];
            }
            int profit = prices[i] - k;
            if (profit > result) {
                result = profit;
            }
        }
        return result;
    }

    /**
     * 给定一个数组，它的第i 个元素是一支给定股票第 i 天的价格。
     * <p>
     * 设计一个算法来计算你所能获取的最大利润。你可以尽可能地完成更多的交易（多次买卖一支股票）。
     * <p>
     * 注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
     * <p>
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-ii
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     *
     * 思想，计算累加
     */
    public static int maxProfit2(int[] prices) {
        int result = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i - 1] < prices[i]) {
                result += prices[i] - prices[i-1];
            }
        }
        return result;
    }
}
