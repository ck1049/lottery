package com.reward.lottery.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 彩票开奖信息响应类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LotteryInformationVo {

    private String name;
    private String enName;
    private String issueNumber;
    private String awardDate;
    private String week;
    private BigDecimal bonusPool;
    private String[] redBalls;
    private String[] blueBalls;
}
