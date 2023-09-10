package com.reward.lottery.service.impl;

import com.github.pagehelper.PageHelper;
import com.reward.lottery.common.enumeration.LotteryType;
import com.reward.lottery.model.HistoricalInformation;
import com.reward.lottery.model.LotteryTrend;
import com.reward.lottery.mapper.LotteryTrendMapper;
import com.reward.lottery.vo.LotteryResVo;
import com.reward.lottery.model.Lotto;
import com.reward.lottery.mapper.LotteryMapper;
import com.reward.lottery.mapper.LottoMapper;
import com.reward.lottery.mapper.TwoColorBallMapper;
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

import jakarta.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.reward.lottery.common.enumeration.LotteryType.LOTTO;
import static com.reward.lottery.common.enumeration.LotteryType.TWO_COLOR_BALL;

@Service
public class LotteryServiceImpl implements ILotteryService {

    @Resource
    private TwoColorBallMapper twoColorBallMapper;

    @Resource
    private LottoMapper lottoMapper;
    @Resource
    private LotteryMapper lotteryMapper;

    @Resource
    private LotteryTrendMapper lotteryTrendMapper;

    public void save(){
        List<LotteryResVo> list = LotteryStatisticsUtils.historyList("dlt");
        for (LotteryResVo lotteryResVo : list) {
            lottoMapper.insert(LotteryUtils.setAndReturnLotto(lotteryResVo));
        }
    }

    /**
     * 开奖信息
     * @return
     */
    @Override
    public List<LotteryInformationVo> lotteryInformation() {
        return lotteryMapper.lotteryInformationList().stream().map(item -> {
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
            List<HistoricalInformation> list = twoColorBallMapper.historicalInformation(minIssueNumber, pageSize);
            return list.stream().map(HistoricalInformationVo::new).collect(Collectors.toList());
        }
        if (LOTTO.getType().equals(enName)) {
            List<HistoricalInformation> list = lottoMapper.historicalInformation(minIssueNumber, pageSize);
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
        return lotteryTrendMapper.selectByExample(example)
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
