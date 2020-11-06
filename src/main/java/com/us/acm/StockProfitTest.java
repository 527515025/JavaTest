package com.us.acm;

import java.util.Arrays;

/**
 * @author yyb
 * @time 2020/11/4
 */
public class StockProfitTest {
    public static void main(String[] args) {
//        System.out.println(maxProfit(init()));
//        System.out.println(maxProfit2(init()));
        System.out.println(maxProfit3(init()));
        System.out.println(maxProfit4(2, init()));
        System.out.println(maxProfit5(2, init()));


    }

    private static int[] init() {
//        int[] arrayToSort = {2, 68, 34, 98, 7, 37, 5, 8, 3, 10, 1, 33, 76, 23, 94, 31, 67, 97, 35, 38};
        int[] arrayToSort = {3, 2, 6, 5, 0, 3};
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
     * <p>
     * 思想，计算累加
     */
    public static int maxProfit2(int[] prices) {
        int result = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i - 1] < prices[i]) {
                result += prices[i] - prices[i - 1];
            }
        }
        return result;
    }


    /**
     * 买卖股票的最佳时机 III
     * 给定一个数组，它的第 i 个元素是一支给定的股票在第 i 天的价格。
     * <p>
     * 设计一个算法来计算你所能获取的最大利润。你最多可以完成 两笔 交易。
     * <p>
     * 注意: 你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
     * <p>
     * 示例 1:
     * <p>
     * 输入: [3,3,5,0,0,3,1,4]
     * 输出: 6
     * 解释: 在第 4 天（股票价格 = 0）的时候买入，在第 6 天（股票价格 = 3）的时候卖出，这笔交易所能获得利润 = 3-0 = 3 。
     *      随后，在第 7 天（股票价格 = 1）的时候买入，在第 8 天 （股票价格 = 4）的时候卖出，这笔交易所能获得利润 = 4-1 = 3 。
     * <p>
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-iii
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     * <p>
     * 思路： 动态规划： 所谓动态规划，就是把复杂的问题简化成规模较小的子问题，再从简单的子问题自底向上一步一步递推，最终得到复杂问题的最优解。
     * 分析两次股票交易总共 5中状态 。0，观望不买，1第一次买入，2第一次卖出，3第二次买入，4第二次卖出 。假设状态为k，所以 K的取值为[0,4]。n为第几天
     * 所以得出公式最大利润为 公式F(n,k) 所以 得出各种状态下最大利润的公式如下：
     * fstBuy: 在该天第一次买入股票可获得的最大收益 max（-p，F（n-1，1）） 当天股价的负数 和 之前每天的第一次买入 利润对比取最大值。 （第一次买入最大利润应该为负数，即第一次买入股价的最小值）
     * fstSell: 在该天第一次卖出股票可获得的最大收益 max（F（n-1，2）,F（n-1，1）+ P）当天股价的正数 + 之前第一次买入的最大利润   和 之前每天的第一次卖出利润 对比取最大值。（第一次卖出最大利润应该为正数，即第一次卖出的最大值）
     * secBuy: 在该天第二次买入股票可获得的最大收益 max（F（n-1，3）,F（n-1，2）- P） 当天股价的负数 + 之前第一次卖出的最大利润   和 之前每天的第二次买入利润 对比取最大值。
     * secSell: 在该天第二次卖出股票可获得的最大收益 max（F（n-1，4）,F（n-1，3）+ P）当天股价的正数 + 之前第二次买入的最大利润   和 之前每天的第二次卖出利润 对比取最大值。
     * 分别对四个变量进行相应的更新, 最后secSell就是最大
     *
     * @param prices
     * @return
     */
    public static int maxProfit3(int[] prices) {
        //初始值为第一天 的四个状态的值，第一次买 为 负股价，设为最小值，第一次卖为 0，第二次买 为 负股价，设为最小值，第二次卖为 0
        int fstBuy = Integer.MIN_VALUE;
        int fstSell = 0;
        int secBuy = Integer.MIN_VALUE;
        int secSell = 0;
        for (int p : prices) {
            fstBuy = Math.max(fstBuy, -p);
            fstSell = Math.max(fstSell, fstBuy + p);
            secBuy = Math.max(secBuy, fstSell - p);
            secSell = Math.max(secSell, secBuy + p);
        }
        return secSell;
    }


    /**
     * 给定一个整数数组 prices ，它的第 i 个元素 prices[i] 是一支给定的股票在第 i 天的价格。
     * <p>
     * 设计一个算法来计算你所能获取的最大利润。你最多可以完成 k 笔交易。
     * <p>
     * 注意: 你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
     * <p>
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-iv
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     * <p>
     * 动态规划+空间优化
     * <p>
     * 自己逻辑正序
     *
     * @param prices
     * @return
     */
    public static int maxProfit4(int k, int[] prices) {
        if (prices == null || prices.length == 0) {
            return 0;
        }
        int n = prices.length;
        //当k非常大时转为无限次交易，其实就是每两天交易一次。
        if (k >= n / 2) {
            int dp0 = 0, dp1 = -prices[0];
            for (int p : prices) {
                dp0 = Math.max(dp0, dp1 + p);
                dp1 = Math.max(dp1, dp0 - p);
            }
            return Math.max(dp0, dp1);
        }

        int[][] dp = new int[k + 1][2];
        //先把每次买卖的初始值顶一下
        for (int i = 0; i <= k; ++i) {
            dp[i][0] = -prices[0];
            dp[i][1] = 0;
        }
        for (int p : prices) {
            for (int j = 1; j <= k; j++) {
                dp[j][0] = Math.max(dp[j][0], dp[j - 1][1] - p);
                dp[j][1] = Math.max(dp[j][1], dp[j][0] + p);
            }
        }
        return dp[k][1];
    }

    /**
     * 官方答案倒叙
     *
     * @param k
     * @param prices
     * @return
     */
    public static int maxProfit5(int k, int[] prices) {
        if (prices == null || prices.length == 0) {
            return 0;
        }
        int n = prices.length;
        //当k非常大时转为无限次交易
        if (k >= n / 2) {
            int dp0 = 0, dp1 = -prices[0];
            for (int p : prices) {
                dp0 = Math.max(dp0, dp1 + p);
                dp1 = Math.max(dp1, dp0 - p);
            }
            return Math.max(dp0, dp1);
        }
        //定义二维数组，交易了多少次、当前的买卖状态
        int[][] dp = new int[k + 1][2];
        for (int i = 0; i <= k; ++i) {
            dp[i][0] = 0;
            dp[i][1] = -prices[0];
        }
        for (int i = 1; i < n; ++i) {
            for (int j = k; j > 0; --j) {
                //处理第k次买入
                dp[j - 1][1] = Math.max(dp[j - 1][1], dp[j - 1][0] - prices[i]);
                //处理第k次卖出
                dp[j][0] = Math.max(dp[j][0], dp[j - 1][1] + prices[i]);
            }
        }
        return dp[k][0];
    }

}
