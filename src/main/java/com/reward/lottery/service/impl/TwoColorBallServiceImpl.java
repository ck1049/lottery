package com.reward.lottery.service.impl;

import com.github.pagehelper.PageHelper;
import com.reward.lottery.model.Lotto;
import com.reward.lottery.vo.LotteryResVo;
import com.reward.lottery.model.TwoColorBall;
import com.reward.lottery.mapper.TwoColorBallMapper;
import com.reward.lottery.service.ITwoColorBallService;
import com.reward.lottery.utils.DateUtils;
import com.reward.lottery.utils.LotteryStatisticsUtils;
import com.reward.lottery.utils.LotteryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import java.util.List;

@Service
public class TwoColorBallServiceImpl implements ITwoColorBallService {

    @Autowired
    private TwoColorBallMapper twoColorBallMapper;

    public void save(){
        List<LotteryResVo> list = LotteryStatisticsUtils.historyList("ssq");
        for (LotteryResVo lotteryResVo : list) {
            twoColorBallMapper.insert(LotteryUtils.setAndReturnTwoColorBall(lotteryResVo));
        }
    }

    @Override
    public void saveLast() {
        TwoColorBall lastTwoColorBall = getLastTwoColorBall();
        Example example = new Example(Lotto.class);
        example.createCriteria().andEqualTo("issueNumber", lastTwoColorBall.getIssueNumber());
        TwoColorBall twoColorBall = twoColorBallMapper.selectOneByExample(example);
        if (twoColorBall == null){
            twoColorBallMapper.insert(lastTwoColorBall);//根据期号查询本地数据库，没有该条记录时进行插入
        }else {
            twoColorBallMapper.updateByPrimaryKey(twoColorBall);
        }
    }

    /**
     * 获取最近一期双色球
     * @return
     */
    public TwoColorBall getLastTwoColorBall(){
        LotteryResVo lotteryResVo = LotteryStatisticsUtils.getLastLotteryInfo("ssq");
        return LotteryUtils.setAndReturnTwoColorBall(lotteryResVo);
    }

    public void create(TwoColorBall twoColorBall){
        twoColorBallMapper.insert(twoColorBall);
    }

}
