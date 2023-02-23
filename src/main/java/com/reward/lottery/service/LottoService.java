package com.reward.lottery.service;

import com.github.pagehelper.PageHelper;
import com.reward.lottery.common.enumeration.LotteryType;
import com.reward.lottery.domain.Lotto;
import com.reward.lottery.domain.LotteryResVo;
import com.reward.lottery.mapper.LottoDao;
import com.reward.lottery.utils.DateUtils;
import com.reward.lottery.utils.LotteryCombinationsUtils;
import com.reward.lottery.utils.LotteryStatisticsUtils;
import com.reward.lottery.utils.LotteryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LottoService {

    @Autowired
    private LottoDao lottoDao;

    public void save(){
        List<LotteryResVo> list = LotteryStatisticsUtils.historyList("dlt");
        for (LotteryResVo lotteryResVo : list) {
            lottoDao.insert(LotteryUtils.setAndReturnLotto(lotteryResVo));
        }
    }

    public void saveLast() {
        Lotto lastLotto = getLastlotto();
        Example example = new Example(Lotto.class);
        example.createCriteria().andEqualTo("issueNumber", lastLotto.getIssueNumber());
        Lotto lotto = lottoDao.selectOneByExample(example);
        if (lotto == null){
            lottoDao.insert(lastLotto);//根据期号查询本地数据库，没有该条记录时进行插入
        }else {
            lastLotto.setPkId(lotto.getPkId());
            lottoDao.updateByPrimaryKey(lastLotto);
        }
    }

    /**
     * 根据期号保存大乐透开奖信息
     * @param start
     * @param end
     */
    public void saveByIssueNumbers(String start, String end) {
        List<Lotto> lottoList = getByIssueNumbers(start, end);
        for (Lotto lotto : lottoList) {
            updateLottoByIssueNumber(lotto);
        }
    }

    public List<Lotto> queryAll(Integer start, Integer pageSize){
        Example example = new Example(Lotto.class);
        example.orderBy("issueNumber").desc();
        PageHelper.startPage(start, pageSize);
        return lottoDao.selectByExample(example);
    }

    public Lotto queryById(String id){
        return lottoDao.selectByPrimaryKey(id);
    }

    /**
     * 获取最近一期大乐透
     * @return
     */
    public Lotto getLastlotto(){
        LotteryResVo lottoMap = LotteryStatisticsUtils.getLastLotteryInfo("dlt");
        return LotteryUtils.setAndReturnLotto(lottoMap);
    }

    /**
     * 根据期号范围查询大乐透开奖信息
     * @param start
     * @param end
     * @return
     */
    public List<Lotto> getByIssueNumbers(String start, String end) {
        List<LotteryResVo> lottoResList = LotteryStatisticsUtils.historyList("dlt", start, end);
        if (!CollectionUtils.isEmpty(lottoResList)) {
            return lottoResList.stream().map(LotteryUtils::setAndReturnLotto).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public void create(Lotto lotto){
        lottoDao.insert(lotto);
    }

    public Lotto queryByIssueNumber(String issueNumber) {
        Example example = new Example(Lotto.class);
        example.createCriteria().andEqualTo("issueNumber", issueNumber);
        return lottoDao.selectOneByExample(example);
    }


    public Lotto queryByAwardDate(String date) {
        Example example = new Example(Lotto.class);
        example.createCriteria().andEqualTo("awardDate", DateUtils.formatDateToString(date));
        return lottoDao.selectOneByExample(example);
    }

    /**
     * 根据给出的大乐透号码计算成本
     * @param redBalls 红球
     * @param blueBalls 蓝球
     * @param additionalMultiple 追加倍数
     * @return
     */
    public Long costCalculationByNumber(String redBalls, String blueBalls, Integer additionalMultiple) {
        return (2 + additionalMultiple) * getCombinationsByNumber(redBalls, blueBalls);
    }

    /**
     * 根据给出的大乐透号码计算成本
     * @param multipleType 复式类型 例：6,3 或 6，3 或 6+3
     * @param additionalMultiple 追加倍数
     * @return
     */
    public Long costCalculationByMultipleType(String multipleType, Integer additionalMultiple) {
        return (2 + additionalMultiple) * getCombinationsByMultipleType(multipleType);
    }

    /**
     * 根据给出的大乐透号码计算组合数
     * @param redBalls 红球
     * @param blueBalls 蓝球
     * @return
     */
    public Long getCombinationsByNumber(String redBalls, String blueBalls) {
        String[] readBallArray = redBalls.split("[,，\\s]");
        String[] blueBallArray = blueBalls.split("[,，\\s]");
        if (readBallArray.length < 5 || readBallArray.length > 35 || blueBallArray.length < 2 || blueBallArray.length > 12) {
            return 0L;
        }
        return LotteryCombinationsUtils.getCombinations(LotteryType.LOTTO.getType(), readBallArray.length, blueBallArray.length).longValue();
    }

    /**
     * 根据复式类型计算组合数
     * @param multipleType 复式类型 例：6,3 或 6，3 或 6+3
     * @return
     */
    public Long getCombinationsByMultipleType(String multipleType) {
        String[] types = multipleType.split("[\\+,，]");
        if (types.length != 2) {
            return 0L;
        }
        return LotteryCombinationsUtils.getCombinations(LotteryType.LOTTO.getType(), Integer.parseInt(types[0]), Integer.parseInt(types[1])).longValue();
    }

    /**
     * 根据期号查询或更新大乐透开奖数据
     */
    public void updateLottoByIssueNumber(Lotto lotto) {
        Example example = new Example(Lotto.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("issueNumber", lotto.getIssueNumber());
        lottoDao.deleteByExample(example);
        lottoDao.insert(lotto);
    }
}
