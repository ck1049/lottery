package com.reward.lottery.vo;

import com.reward.lottery.model.HistoricalInformation;
import com.reward.lottery.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 历史开奖信息响应类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalInformationVo {

    private String name;
    private String enName;
    private Integer issueNumber;
    private String awardDate;
    private String week;
    private BigDecimal bonusPool;
    private String[] redBalls;
    private String[] blueBalls;
    private BigDecimal saleVolume;
    private BigDecimal accumulatedPrizePool;
    private List<Detail> detailList;

    public HistoricalInformationVo(HistoricalInformation historicalInformation) {
        this.name = historicalInformation.getName();
        this.enName = historicalInformation.getEnName();
        this.issueNumber = historicalInformation.getIssueNumber();
        this.awardDate = DateUtils.format(DateUtils.parse(historicalInformation.getAwardDate(), "yyyy-MM-dd"), "MM.dd");
        Date awardDate = DateUtils.parse(historicalInformation.getAwardDate(), "yyyy-MM-dd");

        this.week = DateUtils.format(new Date(), "yyyy-MM-dd").equals(historicalInformation.getAwardDate())
                ? "今天" : DateUtils.format(DateUtils.offsetDay(new Date(), -1), "yyyy-MM-dd").equals(historicalInformation.getAwardDate())
                ? "昨天" : DateUtils.week(awardDate).getSimpleName();
        this.bonusPool = historicalInformation.getBonusPool();
        this.redBalls = historicalInformation.getRedBalls().split(",");
        this.blueBalls = historicalInformation.getBlueBalls().split(",");
        this.saleVolume = new BigDecimal("0");
        this.accumulatedPrizePool = historicalInformation.getBonusPool();
        this.detailList = new ArrayList<>();
        this.detailList.add(new Detail("一等奖", historicalInformation.getFirstPrizeNumber(), historicalInformation.getFirstPrizeAmount()));
        this.detailList.add(new Detail("二等奖", historicalInformation.getSecondPrizeNumber(), historicalInformation.getSecondPrizeAmount()));
    }

    /**
     * 奖级详情
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    protected static class Detail {
        private String level;
        private Long winningBetsNum;
        private Long singleBetBonus;
    }
}
