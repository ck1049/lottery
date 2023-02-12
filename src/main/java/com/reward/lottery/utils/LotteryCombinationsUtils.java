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
        return getCombinations(LotteryType.LOTTO.getType(), 5, 2);
    }

    /**
     * 单式双色球组合数
     * @return
     */
    public static BigInteger getTwoColorBallCombinations() {
        return getCombinations(LotteryType.TWO_COLOR_BALL.getType(), 6, 1);
    }

    /**
     * 根据投注号码(复式)红球、蓝球数量计算所有组合数
     * @param lotteryType
     * @param redBallsToChoose
     * @param blueBallsToChoose
     * @return
     */
    public static BigInteger getCombinations(String lotteryType, int redBallsToChoose, int blueBallsToChoose) {
        if(LotteryType.LOTTO.getType().equals(lotteryType)
                && (redBallsToChoose < 5 || redBallsToChoose > 35 || blueBallsToChoose < 2 || blueBallsToChoose > 12)) {
            throw new IllegalArgumentException();
        }
        if(LotteryType.TWO_COLOR_BALL.getType().equals(lotteryType)
                && (redBallsToChoose < 6 || redBallsToChoose > 33 || blueBallsToChoose < 1 || blueBallsToChoose > 16)) {
            throw new IllegalArgumentException();
        }
        int redBalls = getRedBalls(lotteryType);
        int blueBalls = getBlueBalls(lotteryType);
        return factorial(redBalls).divide(factorial(redBallsToChoose).multiply(factorial(redBalls - redBallsToChoose)))
                .multiply(factorial(blueBalls).divide(factorial(blueBallsToChoose).multiply(factorial(blueBalls - blueBallsToChoose))));
    }

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
}
