package com.reward.lottery.mapper;

import com.reward.lottery.domain.HistoricalInformation;
import com.reward.lottery.domain.TwoColorBall;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TwoColorBallDao extends Mapper<TwoColorBall> {

    /**
     * 历史开奖信息（小程序使用）
     * @param minIssueNumber
     * @param pageSize
     * @return
     */
    List<HistoricalInformation> historicalInformation(@Param("minIssueNumber") String minIssueNumber, @Param("pageSize") Integer pageSize);
}
