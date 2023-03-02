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
        return getCombinations(lotteryType, LotteryUtils.LOTTO_RED.length + "+" + LotteryUtils.LOTTO_BLUE.length);
    }

    /**
     * 单式双色球组合数
     * @return
     */
    public static BigInteger getTwoColorBallCombinations() {
        String lotteryType = LotteryType.TWO_COLOR_BALL.getType();
        return getCombinations(lotteryType, LotteryUtils.TWO_COLOR_BALL_RED.length + "+" + LotteryUtils.TWO_COLOR_BALL_BLUE.length);
    }

    /**
     * 根据投注号码(复式)红球、蓝球数量计算组合数
     * @param lotteryType
     * @param multipleType
     * @return
     */
    public static BigInteger getCombinations(String lotteryType, String multipleType) {
        int[] typeBallsNum = LotteryUtils.checkMultipleType(lotteryType, multipleType);
        if (LotteryType.LOTTO.getType().equals(lotteryType)) {
            return combinations(typeBallsNum[0], 5).multiply(combinations(typeBallsNum[1], 2));
        }
        if (LotteryType.TWO_COLOR_BALL.getType().equals(lotteryType)) {
            return combinations(typeBallsNum[0], 6).multiply(combinations(typeBallsNum[1], 1));
        }
        throw new IllegalArgumentException();
    }

    /**
     * 根据投注号码(复式)红球、蓝球数量计算组合数
     * @param lotteryType
     * @param redBallsNum
     * @param blueBallsNum
     * @return
     */
    public static BigInteger getCombinations(String lotteryType, int redBallsNum, int blueBallsNum) {
        LotteryUtils.checkBallsNum(lotteryType, redBallsNum, blueBallsNum);
        if (LotteryType.LOTTO.getType().equals(lotteryType)) {
            return combinations(redBallsNum, 5).multiply(combinations(blueBallsNum, 2));
        }
        if (LotteryType.TWO_COLOR_BALL.getType().equals(lotteryType)) {
            return combinations(redBallsNum, 6).multiply(combinations(blueBallsNum, 1));
        }
        throw new IllegalArgumentException();
    }

    /**
     * 计算组合数
     * @param total 总数
     * @param choose 取出球数
     * @return
     */
    public static BigInteger combinations(int total, int choose) {
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

    public static void main(String[] args) {
        System.out.println("大乐透单式共有" + getLottoCombinations() + "种组合");
        System.out.println("双色球单式共有" + getTwoColorBallCombinations() + "种组合");
    }
}
