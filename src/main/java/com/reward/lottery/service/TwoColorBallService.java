package com.reward.lottery.service;

import com.github.pagehelper.PageHelper;
import com.reward.lottery.domain.Lotto;
import com.reward.lottery.domain.LotteryResVo;
import com.reward.lottery.domain.TwoColorBall;
import com.reward.lottery.mapper.TwoColorBallDao;
import com.reward.lottery.utils.DateUtils;
import com.reward.lottery.utils.LotteryStatisticsUtils;
import com.reward.lottery.utils.LotteryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class TwoColorBallService {

    @Autowired
    private TwoColorBallDao twoColorBallDao;

    public void save(){
        List<LotteryResVo> list = LotteryStatisticsUtils.historyList("ssq");
        for (LotteryResVo lotteryResVo : list) {
            twoColorBallDao.insert(LotteryUtils.setAndReturnTwoColorBall(lotteryResVo));
        }
    }

    public void saveLast() {
        TwoColorBall lastTwoColorBall = getLastTwoColorBall();
        Example example = new Example(Lotto.class);
        example.createCriteria().andEqualTo("issueNumber", lastTwoColorBall.getIssueNumber());
        TwoColorBall twoColorBall = twoColorBallDao.selectOneByExample(example);
        if (twoColorBall == null){
            twoColorBallDao.insert(lastTwoColorBall);//根据期号查询本地数据库，没有该条记录时进行插入
        }else {
            lastTwoColorBall.setPkId(twoColorBall.getPkId());
        }
    }

    public List<TwoColorBall> queryAll(Integer start){
        Example example = new Example(TwoColorBall.class);
        example.orderBy("issueNumber").desc();
        PageHelper.startPage(start, 20);
        return twoColorBallDao.selectByExample(example);
    }

    public TwoColorBall queryById(String id){
        return twoColorBallDao.selectByPrimaryKey(id);
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
        twoColorBallDao.insert(twoColorBall);
    }

    public TwoColorBall queryByIssueNumber(String issueNumber) {
        Example example = new Example(TwoColorBall.class);
        example.createCriteria().andEqualTo("issueNumber", issueNumber);
        return twoColorBallDao.selectOneByExample(example);
    }

    public TwoColorBall queryByAwardDate(String date) {
        Example example = new Example(TwoColorBall.class);
        example.createCriteria().andEqualTo("awardDate", DateUtils.formatDateToString(date));
        return twoColorBallDao.selectOneByExample(example);
    }
}
