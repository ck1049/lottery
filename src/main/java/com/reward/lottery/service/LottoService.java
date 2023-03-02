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
import tk.mybatis.mapper.entity.Example;
import java.util.*;
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
        }else if (LotteryType.TWO_COLOR_BALL.getType().equals(lotteryType)){
            colorBallNumMap.put("red", LotteryType.TWO_COLOR_BALL.getRedBallsNum());
            colorBallNumMap.put("blue", LotteryType.TWO_COLOR_BALL.getBlueBallsNum());
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
        arrayList.add(String.join(",",
                Arrays.stream(item)
                        .map(it -> String.format("%02d", it))
                        .collect(Collectors.joining(","))));
        int index = num - 1;
        while (true) {
            while (true) {
                item[index]++;
                arrayList.add(String.join(",",
                        Arrays.stream(item)
                                .map(it -> String.format("%02d", it))
                                .collect(Collectors.joining(","))));
                if (item[index] >= ballsTotalNum + index - num + 1) {
                    item[index] = num;
                    break;
                }
                index = num - 1;
            }
            index--;
            if (index < 0) {
                for (int i = 0; i < num; i++) {
                    item[i] = ballsTotalNum + i - num +1;
                }
                arrayList.add(String.join(",",
                        Arrays.stream(item)
                                .map(it -> String.format("%02d", it))
                                .collect(Collectors.joining(","))));
                break;
            }
        }

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
//        List<String> combList = getCombByBallsNum(3, LotteryType.LOTTO.getType(), "red");
        List<String> combList = sss();
        long endTime = new Date().getTime();
        //System.out.println("初始化时间" + (endTime - startTime) + "ms, 共" + map.keySet().size() + "个组合" + map.keySet());
        System.out.println("初始化时间" + (endTime - startTime) + "ms, 共" + combList.size() + "个组合\n" + String.join("\n", combList));
    }
}
