package com.reward.lottery.utils;

public class MathUtils {
    /**
     * 约分
     * @param numerator 分子
     * @param denominator 分母
     * @return
     */
    public static Long[] reductionFraction(Long numerator, Long denominator) {
        long a = numerator;
        long c = denominator;
        long t;
        while(c!=0)
        {
            t = a%c;
            a = c;
            c = t;
        }
        numerator = numerator/a;
        denominator = denominator/a;
        System.out.printf("%d/%d\n",numerator,denominator);
        return new Long[]{numerator, denominator};
    }

    public static void main(String[] args) {
        reductionFraction(56L,21425712L);
    }
}
