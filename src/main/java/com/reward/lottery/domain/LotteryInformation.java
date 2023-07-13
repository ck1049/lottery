package com.reward.lottery.domain;

import lombok.Data;

import java.math.BigDecimal;
/**
 * 彩票开奖信息
 */
@Data
public class LotteryInformation {

    private String name;
    private String enName;
    private String issueNumber;
    private String awardDate;
    private String week;
    private BigDecimal bonusPool;
    private String redBalls;
    private String blueBalls;
}
