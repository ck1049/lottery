package com.reward.lottery.service;

import com.reward.lottery.domain.LotteryTrend;
import com.reward.lottery.vo.HistoricalInformationVo;
import com.reward.lottery.vo.LotteryInformationVo;

import java.util.List;

public interface ILotteryService {

    /**
     * 开奖信息
     * @return
     */
    List<LotteryInformationVo> lotteryInformation();

    /**
     * 历史开奖信息
     * @param enName 彩票类型
     * @param minIssueNumber 最小期号
     * @param pageSize 本页条目数
     * @return
     */
    List<HistoricalInformationVo> historicalInformation(String enName, String minIssueNumber, Integer pageSize);

    /**
     * 走势图
     * @param enName
     * @return
     */
    List<LotteryTrend> trend(String enName);
}
