package com.reward.lottery.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 历史开奖信息响应类
 */
@Data
public class HistoricalInformation {

    private String name;
    private String enName;
    private Integer issueNumber;
    private String awardDate;
    private String week;
    private BigDecimal bonusPool;
    private String redBalls;
    private String blueBalls;
    private BigDecimal saleVolume;
    private BigDecimal accumulatedPrizePool;
    private Long firstPrizeNumber;
    private Long firstPrizeAmount;
    private Long secondPrizeNumber;
    private Long secondPrizeAmount;

}
