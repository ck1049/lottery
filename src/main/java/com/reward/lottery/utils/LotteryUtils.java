package com.reward.lottery.utils;

import com.reward.lottery.common.enumeration.LotteryType;
import com.reward.lottery.domain.Lottery;
import com.reward.lottery.domain.Lotto;
import com.reward.lottery.domain.LotteryResVo;
import com.reward.lottery.domain.TwoColorBall;
import java.util.*;

public class LotteryUtils {

    /**大乐透红球*/
    public static String[] LOTTO_RED ={
            "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
            "31", "32", "33", "34", "35"};

    /**大乐透蓝球*/
    public static String[] LOTTO_BLUE = {
            "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
            "11", "12"};

    /**双色球红球*/
    public static String[] TWO_COLOR_BALL_RED = {
            "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
            "31", "32", "33"};

    /**双色球蓝球*/
    public static String[] TWO_COLOR_BALL_BLUE = {
            "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
            "11", "12", "13", "14", "15", "16"};

    public static Map<String, List<String>> randomLottery(String type, String multipleType){
        int[] typeBalls = checkMultipleType(type, multipleType);

        Map<String, List<String>> result = new HashMap<>();
        Set<String> red = new HashSet<>();
        Set<String> blue = new HashSet<>();
        Random random = new Random();
        if ("ssq".equals(type)){
            while(red.size() < (StringUtils.isBlank(multipleType) ? 6 : typeBalls[0])) {
                int  ran = random.nextInt(33);
                red.add(TWO_COLOR_BALL_RED[ran]);
            }
            while(blue.size() < (StringUtils.isBlank(multipleType) ? 1 : typeBalls[1])){
                int ran = random.nextInt(12);
                blue.add(TWO_COLOR_BALL_BLUE[ran]);
            }
        }else if ("dlt".equals(type)){
            while(red.size() < (StringUtils.isBlank(multipleType) ? 5 : typeBalls[0])){
                int ran = random.nextInt(35);
                red.add(LOTTO_RED[ran]);
            }
            while(blue.size() < (StringUtils.isBlank(multipleType) ? 2 : typeBalls[1])){
                int ran = random.nextInt(12);
                blue.add(LOTTO_BLUE[ran]);
            }
        }
        ArrayList<String> redList = new ArrayList<>(red);
        ArrayList<String> blueList = new ArrayList<>(blue);
        Collections.sort(redList);
        Collections.sort(blueList);
        result.put("redBalls", redList);
        result.put("blueBalls", blueList);
        return result;
    }

    public static Lotto setAndReturnLotto(LotteryResVo lotteryResVo){
        return (Lotto) setAndReturnLottery("lotto", lotteryResVo);
    }

    public static TwoColorBall setAndReturnTwoColorBall(LotteryResVo lotteryResVo){
        return (TwoColorBall) setAndReturnLottery("twoColorBall", lotteryResVo);
    }

    public static Lottery setAndReturnLottery(String mold, LotteryResVo lotteryResVo){
        String issueNumber = lotteryResVo.getIssueNumber();//期号
        String awardDate = lotteryResVo.getAwardDate();
        String number = lotteryResVo.getNumber();
        String bonusPool = lotteryResVo.getBonusPool();//奖池金额
        String totalBets = lotteryResVo.getTotalBets();//总投注额
        String firstPrizeNumber = lotteryResVo.getFirstPrizeNumber();//一等奖注数
        String firstPrizeAmount = lotteryResVo.getFirstPrizeAmount();//一等奖金额
        String secondPrizeNumber = lotteryResVo.getSecondPrizeNumber();//二等奖注数
        String secondPrizeAmount = lotteryResVo.getSecondPrizeAmount();//二等奖金额
        String ball1 = number.substring(0, 2);
        String ball2 = number.substring(2, 4);
        String ball3 = number.substring(4, 6);
        String ball4 = number.substring(6, 8);
        String ball5 = number.substring(8, 10);
        String ball6 = number.substring(10, 12);
        String ball7 = number.substring(12);
        Lottery lottery;
        if ("lotto".equals(mold)){
            lottery = new Lotto(number, ball1, ball2, ball3, ball4, ball5,ball6, ball7, "0", 0);
        }else if ("twoColorBall".equals(mold)){
            lottery = new TwoColorBall(number, ball1, ball2, ball3, ball4, ball5,ball6, ball7, "0", 0);
        }else {
            return null;
        }
        lottery.setPkId(KeyUtils.uuid());
        lottery.setAwardDate(awardDate);
        lottery.setIssueNumber(issueNumber);
        lottery.setBonusPool(Long.parseLong(bonusPool));
        lottery.setTotalBets(Long.parseLong(totalBets));
        lottery.setPrice(0);
        lottery.setFirstPrizeNumber(Integer.parseInt(firstPrizeNumber));
        lottery.setFirstPrizeAmount(Integer.parseInt(firstPrizeAmount));
        lottery.setSecondPrizeNumber(Integer.parseInt(secondPrizeNumber));
        lottery.setSecondPrizeAmount(Integer.parseInt(secondPrizeAmount));
        lottery.setWinningAmount(0);
        return lottery;
    }

    public static int[] checkMultipleType(String lotteryType, String multipleType) {
        try {
            String[] multipleTypes = multipleType.split("[，,+]");
            int redBallsNum = Integer.parseInt(multipleTypes[0]);
            int blueBallsNum = Integer.parseInt(multipleTypes[1]);

            return checkBallsNum(lotteryType, redBallsNum, blueBallsNum);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static int[] checkBallsNum(String lotteryType, int redBallsNum, int blueBallsNum) {

        if(LotteryType.LOTTO.getType().equals(lotteryType)) {
            if((redBallsNum < 5 || redBallsNum > 35 || blueBallsNum < 2 || blueBallsNum > 12)) {
                throw new IllegalArgumentException();
            }
            return new int[]{redBallsNum, blueBallsNum};
        }

        if(LotteryType.TWO_COLOR_BALL.getType().equals(lotteryType)) {
            if((redBallsNum < 6 || redBallsNum > 33 || blueBallsNum < 1 || blueBallsNum > 16)) {
                throw new IllegalArgumentException();
            }
            return new int[]{redBallsNum, blueBallsNum};
        }
        throw new IllegalArgumentException();
    }
}
