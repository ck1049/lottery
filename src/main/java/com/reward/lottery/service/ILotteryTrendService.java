package com.reward.lottery.service;

import com.reward.lottery.common.enumeration.LotteryType;

/**
 * <p>
 * 彩票走势表 服务类
 * </p>
 *
 * @author loafer
 * @since 2023-07-20
 */
public interface ILotteryTrendService {

    /**
     * 递归调用更新方法，issue==null时全部更新完成
     * @param lotteryType
     * @param issue
     * @param limit
     */
    void updateIntervalCount(LotteryType lotteryType, Integer issue, Integer limit);

}
