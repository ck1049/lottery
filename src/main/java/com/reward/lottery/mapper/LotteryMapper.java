package com.reward.lottery.mapper;

import com.reward.lottery.model.LotteryInformation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LotteryMapper {

    List<LotteryInformation> lotteryInformationList();

}
