package com.reward.lottery.utils;

import com.reward.lottery.common.enumeration.LotteryType;

import java.math.BigInteger;

/**
 * 彩票组合计算工具
 */
public class LotteryCombinationsUtils {

    /**
     * 单式大乐透组合数
     * @return
     */
    public static BigInteger getLottoCombinations() {
        String lotteryType = LotteryType.LOTTO.getType();
        return getCombinations(lotteryType, getRedBalls(lotteryType), getBlueBalls(lotteryType));
    }

    /**
     * 单式双色球组合数
     * @return
     */
    public static BigInteger getTwoColorBallCombinations() {
        String lotteryType = LotteryType.TWO_COLOR_BALL.getType();
        return getCombinations(lotteryType, getRedBalls(lotteryType), getBlueBalls(lotteryType));
    }

    /**
     * 根据投注号码(复式)红球、蓝球数量计算组合数
     * @param lotteryType
     * @param redBallsToChoose
     * @param blueBallsToChoose
     * @return
     */
    public static BigInteger getCombinations(String lotteryType, int redBallsToChoose, int blueBallsToChoose) {
        if(LotteryType.LOTTO.getType().equals(lotteryType)) {
            if((redBallsToChoose < 5 || redBallsToChoose > 35 || blueBallsToChoose < 2 || blueBallsToChoose > 12)) {
                throw new IllegalArgumentException();
            }
            return combinations(redBallsToChoose, 5).multiply(combinations(blueBallsToChoose, 2));
        }

        if(LotteryType.TWO_COLOR_BALL.getType().equals(lotteryType)) {
            if((redBallsToChoose < 6 || redBallsToChoose > 33 || blueBallsToChoose < 1 || blueBallsToChoose > 16)) {
                throw new IllegalArgumentException();
            }
            return combinations(redBallsToChoose, 6).multiply(combinations(blueBallsToChoose, 1));
        }

        throw new IllegalArgumentException();
    }

    /**
     * 计算组合数
     * @param total 总数
     * @param choose 取出球数
     * @return
     */
    private static BigInteger combinations(int total, int choose) {
        return factorial(total).divide(factorial(choose).multiply(factorial(total - choose)));
    }

    /**
     * 阶乘
     * @param n
     * @return
     */
    private static BigInteger factorial(int n) {
        BigInteger result = BigInteger.valueOf(1);
        for (int i = 2; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

    private static int getRedBalls(String lotteryType) {
        if (LotteryType.TWO_COLOR_BALL.getType().equals(lotteryType)) {
            return 33;
        } else {
            return 35;
        }
    }

    private static int getBlueBalls(String lotteryType) {
        if (LotteryType.TWO_COLOR_BALL.getType().equals(lotteryType)) {
            return 16;
        } else {
            return 12;
        }
    }

    public static void main(String[] args) {
        System.out.println("大乐透单式共有" + getLottoCombinations() + "种组合");
        System.out.println("双色球单式共有" + getTwoColorBallCombinations() + "种组合");
    }
}
