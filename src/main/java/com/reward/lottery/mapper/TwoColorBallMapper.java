package com.reward.lottery.mapper;

import com.reward.lottery.model.HistoricalInformation;
import com.reward.lottery.model.TwoColorBall;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TwoColorBallMapper extends Mapper<TwoColorBall> {

    /**
     * 历史开奖信息（小程序使用）
     * @param minIssueNumber
     * @param pageSize
     * @return
     */
    List<HistoricalInformation> historicalInformation(@Param("minIssueNumber") String minIssueNumber, @Param("pageSize") Integer pageSize);
}
