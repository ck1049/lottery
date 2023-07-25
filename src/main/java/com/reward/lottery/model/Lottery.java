package com.reward.lottery.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lottery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String awardDate;//开奖日期
    private Integer issueNumber;//期号
    private Long bonusPool; //奖池金额
    private Long totalBets; //总投注额
    private Integer firstPrizeNumber; //一等奖注数
    private Integer firstPrizeAmount; //一等奖金额
    private Integer secondPrizeNumber; //二等奖注数
    private Integer secondPrizeAmount; //二等奖金额


    public Lottery(Integer issueNumber, String awardDate){
        this.issueNumber = issueNumber;
        this.awardDate = awardDate;
    }
}
