package com.reward.lottery.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * (MyLotto)实体类
 *
 * @author makejava
 * @since 2022-10-18 22:11:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "my_lotto")
public class MyLotto implements Serializable {
    private static final long serialVersionUID = 284536789720911103L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String pkId;
    /**
     * 期号
     */
    private String issueNumber;
    
    private String number;
    
    private String red1;
    
    private String red2;
    
    private String red3;
    
    private String red4;
    
    private String red5;
    
    private String red6;
    
    private String red7;
    
    private String red8;
    
    private String red9;
    
    private String red10;
    
    private String red11;
    
    private String red12;
    
    private String red13;
    
    private String red14;
    
    private String red15;
    
    private String red16;
    
    private String red17;
    
    private String red18;
    
    private String red19;
    
    private String red20;
    
    private String red21;
    
    private String red22;
    
    private String red23;
    
    private String red24;
    
    private String red25;
    
    private String red26;
    
    private String red27;
    
    private String red28;
    
    private String red29;
    
    private String red30;
    
    private String red31;
    
    private String red32;
    
    private String red33;
    
    private String red34;
    
    private String red35;
    
    private String blue1;
    
    private String blue2;
    
    private String blue3;
    
    private String blue4;
    
    private String blue5;
    
    private String blue6;
    
    private String blue7;
    
    private String blue8;
    
    private String blue9;
    
    private String blue10;
    
    private String blue11;
    
    private String blue12;
    /**
     * 彩票价格
     */
    private Integer price;
    /**
     * 一等奖注数
     */
    private Integer firstPrizeNumber;
    /**
     * 一等奖金额
     */
    private Integer firstPrizeAmount;
    /**
     * 二等奖注数
     */
    private Integer secondPrizeNumber;
    /**
     * 二等奖金额
     */
    private Integer secondPrizeAmount;
    /**
     * 中奖金额
     */
    private Integer winningAmount;
    /**
     * 奖池金额
     */
    private Long bonusPool;
    /**
     * 总投注额
     */
    private Long totalBets;
    /**
     * 0开奖号码，1自购号码， 2推荐号码
     */
    private String type;
    /**
     * 开奖日期
     */
    private String awardDate;
    /**
     * 序号
     */
    private String sort;

}

