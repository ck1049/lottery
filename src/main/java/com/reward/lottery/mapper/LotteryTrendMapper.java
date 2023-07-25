package com.reward.lottery.mapper;

import com.reward.lottery.model.LotteryTrend;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * <p>
 * 彩票走势表 Mapper 接口
 * </p>
 *
 * @author loafer
 * @since 2023-07-20
 */
public interface LotteryTrendMapper extends Mapper<LotteryTrend> {

    void batchDelete(@Param("list") List<LotteryTrend> list);
    void batchInsert(@Param("list") List<LotteryTrend> list);
}
