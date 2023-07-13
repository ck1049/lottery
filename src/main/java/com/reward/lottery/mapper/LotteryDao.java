package com.reward.lottery.mapper;

import com.reward.lottery.domain.LotteryInformation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LotteryDao {

    List<LotteryInformation> lotteryInformationList();

}
