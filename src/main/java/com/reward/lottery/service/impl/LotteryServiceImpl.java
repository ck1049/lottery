package com.reward.lottery.service.impl;

import com.github.pagehelper.PageHelper;
import com.reward.lottery.common.enumeration.LotteryType;
import com.reward.lottery.domain.HistoricalInformation;
import com.reward.lottery.domain.LotteryTrend;
import com.reward.lottery.mapper.LotteryTrendDao;
import com.reward.lottery.vo.LotteryResVo;
import com.reward.lottery.domain.Lotto;
import com.reward.lottery.mapper.LotteryDao;
import com.reward.lottery.mapper.LottoDao;
import com.reward.lottery.mapper.TwoColorBallDao;
import com.reward.lottery.service.ILotteryService;
import com.reward.lottery.utils.DateUtils;
import com.reward.lottery.utils.LotteryCombinationsUtils;
import com.reward.lottery.utils.LotteryStatisticsUtils;
import com.reward.lottery.utils.LotteryUtils;
import com.reward.lottery.vo.HistoricalInformationVo;
import com.reward.lottery.vo.LotteryInformationVo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.reward.lottery.common.enumeration.LotteryType.LOTTO;
import static com.reward.lottery.common.enumeration.LotteryType.TWO_COLOR_BALL;

@Service
public class LotteryServiceImpl implements ILotteryService {

    @Resource
    private TwoColorBallDao twoColorBallDao;

    @Resource
    private LottoDao lottoDao;
    @Resource
    private LotteryDao lotteryDao;

    @Resource
    private LotteryTrendDao lotteryTrendDao;

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

    public Lotto queryByIssueNumber(Integer issueNumber) {
        Example example = new Example(Lotto.class);
        example.createCriteria().andEqualTo("issueNumber", issueNumber);
        return lottoDao.selectOneByExample(example);
    }


    public Lotto queryByAwardDate(String date) {
        Example example = new Example(Lotto.class);
        example.createCriteria().andEqualTo("awardDate", DateUtils.formatDateString(date));
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
        if (readBallArray.length < 5 || readBallArray.length > LotteryType.LOTTO.getRedBallsNum()
                || blueBallArray.length < 2 || blueBallArray.length > LotteryType.LOTTO.getBlueBallsNum()) {
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
        return LotteryCombinationsUtils.getCombinations(LotteryType.LOTTO.getType(), multipleType).longValue();
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


    /**
     * 开奖信息
     * @return
     */
    @Override
    public List<LotteryInformationVo> lotteryInformation() {
        return lotteryDao.lotteryInformationList().stream().map(item -> {
            Date awardDate = DateUtils.parse(item.getAwardDate(), "yyyy-MM-dd");
            String week = DateUtils.format(new Date(), "yyyy-MM-dd").equals(item.getAwardDate())
                    ? "今天" : DateUtils.format(DateUtils.offsetDay(new Date(), -1), "yyyy-MM-dd").equals(item.getAwardDate())
                    ? "昨天" : DateUtils.week(awardDate).getSimpleName();
            return new LotteryInformationVo(item.getName(), item.getEnName(), item.getIssueNumber(),
                    DateUtils.format(awardDate, "MM.dd"), week,
                    item.getBonusPool(), item.getRedBalls().split(","), item.getBlueBalls().split(","));
        }).collect(Collectors.toList());
    }

    @Override
    public List<HistoricalInformationVo> historicalInformation(String enName, String minIssueNumber, Integer pageSize) {
        if (TWO_COLOR_BALL.getType().equals(enName)) {
            List<HistoricalInformation> list = twoColorBallDao.historicalInformation(minIssueNumber, pageSize);
            return list.stream().map(HistoricalInformationVo::new).collect(Collectors.toList());
        }
        if (LOTTO.getType().equals(enName)) {
            List<HistoricalInformation> list = lottoDao.historicalInformation(minIssueNumber, pageSize);
            return list.stream().map(HistoricalInformationVo::new).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<LotteryTrend> trend(String enName, Integer limit) {
        if (TWO_COLOR_BALL.getType().equals(enName)) {
            limit *= (TWO_COLOR_BALL.getRedBallsNum() + TWO_COLOR_BALL.getBlueBallsNum());
        }
        if (LOTTO.getType().equals(enName)) {
            limit *= (LOTTO.getRedBallsNum() + LOTTO.getBlueBallsNum());
        }
        Example example = new Example(LotteryTrend.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("lotteryType", enName);
        example.setOrderByClause("issue desc limit " + limit);
        return lotteryTrendDao.selectByExample(example)
                .stream().sorted((o1, o2) -> {
                    // 期号正序，红球在前，号码正序
                    if (o1.getIssue() < o2.getIssue()) {
                        return -1;
                    }
                    if (o1.getIssue() > o2.getIssue()) {
                        return 1;
                    }
                    if (o1.getColor().equals("red") && o2.getColor().equals("blue")) {
                        return -1;
                    }
                    if (o1.getColor().equals("blue") && o2.getColor().equals("red")) {
                        return 1;
                    }
                    return Integer.parseInt(o1.getNumber()) - Integer.parseInt(o2.getNumber());
                })
                .collect(Collectors.toList());
    }

    public List<Map<String, Long>> computeCombinationRanking(int periodsNum) {
        return null;
    }

    /**
     * 根据球的个数计算所有的组合数 如：num:2 => 01,02  01,03  num:3 => 01,,02,03  01,02,04等
     * @return
     */
    public static List<String> getCombByBallsNum(int num, String lotteryType, String color) {
        List<String> list = new ArrayList<>();
        // 外层set:[[01,02...],[02,03...],[03,04...]]
        List<Set<String>> setList = new ArrayList<>();
        Map<String, Object> colorBallNumMap = new HashMap<>();
        if (LotteryType.LOTTO.getType().equals(lotteryType)) {
            colorBallNumMap.put("red", LotteryType.LOTTO.getRedBallsNum());
            colorBallNumMap.put("blue", LotteryType.LOTTO.getBlueBallsNum());
            colorBallNumMap.put("redBalls", LotteryUtils.LOTTO_RED);
            colorBallNumMap.put("blueBalls", LotteryUtils.LOTTO_BLUE);
        }else if (TWO_COLOR_BALL.getType().equals(lotteryType)){
            colorBallNumMap.put("red", TWO_COLOR_BALL.getRedBallsNum());
            colorBallNumMap.put("blue", TWO_COLOR_BALL.getBlueBallsNum());
            colorBallNumMap.put("redBalls", LotteryUtils.TWO_COLOR_BALL_RED);
            colorBallNumMap.put("blueBalls", LotteryUtils.TWO_COLOR_BALL_BLUE);
        }else {
            throw new IllegalArgumentException();
        }

        Integer ballsTotalNum = (Integer) colorBallNumMap.get(color);

        List<String> arrayList = new ArrayList<>();
        Integer[] item = new Integer[num];
        for (int i = 0; i < num; i++) {
            item[i] = i + 1;
        }
        while (true) {
            int index = num - 1;
            while (true) {
                // 内层循环只做最后一位加一
                arrayList.add(String.join(",",
                        Arrays.stream(item)
                                .map(it -> String.format("%02d", it))
                                .collect(Collectors.joining(","))));
                item[index]++;
                if (item[index] > ballsTotalNum + index - num + 1) {
                    break;
                }
            }

            // 36进制加法
            for (int idx = index; idx > 0; idx--) {
                if (item[idx] >= ballsTotalNum + index - num + 1) {
                    item[idx-1]++;
                    item[idx] = item[idx-1] >= ballsTotalNum + index - num + 1 ? num : item[idx-1] + 1;
                }
            }

            // 最后一组数字校正
            if (item[0] == ballsTotalNum - num + 1) {
                for (int i = 1; i < num; i++) {
                    item[i] = item[i-1] + 1;
                }
                arrayList.add(String.join(",",
                        Arrays.stream(item)
                                .map(it -> String.format("%02d", it))
                                .collect(Collectors.joining(","))));
                break;
            }
        }

        // 去重+排序
        Set<String> stringSet = new LinkedHashSet<>();
        for (String str : arrayList) {
            List<String> strList = Arrays.asList(str.split(","));
            Set<String> set = new HashSet<>();
            set.addAll(strList);
            if (set.size() == num) {
                strList.sort(((o1, o2) -> Integer.parseInt(o1) - Integer.parseInt(o2)));
                stringSet.add(String.join(",", strList));
            }
        }
        return new ArrayList<>(stringSet);
    }

    public static List<String> sss() {
        List<String> list = new ArrayList<>();
        for (int i = 1; i < 34; i++) {
            for (int j = i + 1; j < 35; j++) {
                for (int k = j + 1; k <= 35; k++) {
                    list.add(String.format("%02d", i) + "," + String.format("%02d", j) + "," + String.format("%02d", k));
                }
            }
        }
        return list;
    }

    public static void main(String[] args) {
        long startTime = new Date().getTime();
        List<String> combList = getCombByBallsNum(3, LotteryType.LOTTO.getType(), "red");
//        List<String> combList = sss();
        long endTime = new Date().getTime();
        //System.out.println("初始化时间" + (endTime - startTime) + "ms, 共" + map.keySet().size() + "个组合" + map.keySet());
        System.out.println("初始化时间" + (endTime - startTime) + "ms, 共" + combList.size() + "个组合\n" + String.join("\n", combList));
    }

}
