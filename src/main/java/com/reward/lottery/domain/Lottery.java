package com.reward.lottery.domain;

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
    private String pkId;
    private String awardDate;//开奖日期
    private String issueNumber;//期号
    private Long bonusPool; //奖池金额
    private Long totalBets; //总投注额
    private Integer price; //彩票价格
    private Integer firstPrizeNumber; //一等奖注数
    private Integer firstPrizeAmount; //一等奖金额
    private Integer secondPrizeNumber; //二等奖注数
    private Integer secondPrizeAmount; //二等奖金额
    private Integer winningAmount; //中奖金额
    private String type; //0开奖号码，1自购号码， 2推荐号码
    private Integer sort;//序号

    public Lottery(String type, Integer sort){
        this.type = type;
        this.sort = sort;
    }

    public Lottery(String type, String issueNumber, String awardDate, Integer sort){
        this.type = type;
        this.issueNumber = issueNumber;
        this.awardDate = awardDate;
        this.sort = sort;
    }
}
