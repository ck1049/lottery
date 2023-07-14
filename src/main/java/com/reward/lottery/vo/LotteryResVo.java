package com.reward.lottery.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LotteryResVo {

    private String number;
    private Integer issueNumber;
    private String awardDate;
    private String firstPrizeNumber;
    private String firstPrizeAmount;
    private String secondPrizeNumber;
    private String secondPrizeAmount;
    private String bonusPool;
    private String totalBets;

}
