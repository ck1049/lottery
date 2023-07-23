package com.reward.lottery.service.impl;

import com.reward.lottery.common.enumeration.LotteryType;
import com.reward.lottery.domain.LotteryTrend;
import com.reward.lottery.mapper.LotteryTrendDao;
import com.reward.lottery.service.ILotteryTrendService;
import com.reward.lottery.utils.CollectionUtils;
import com.reward.lottery.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 彩票走势表 服务实现类
 * </p>
 *
 * @author loafer
 * @since 2023-07-20
 */
@Service
public class LotteryTrendServiceImpl implements ILotteryTrendService {

    @Resource(name = "transactionTemplate")
    private TransactionTemplate transactionTemplate;

    @Resource
    private LotteryTrendDao dao;

    /**
     * 递归调用更新方法，issue==null时全部更新完成
      */
    @Transactional
    public void updateIntervalCount(LotteryType lotteryType, Integer issue, Integer limit) {
        if (issue == null) {
            return;
        }
        limit *= (lotteryType.getRedBallsNum() + lotteryType.getBlueBallsNum());
        Example example = new Example(LotteryTrend.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("lotteryType", lotteryType.getType());
        criteria.andGreaterThanOrEqualTo("issue", issue);
        example.setOrderByClause("issue asc limit " + limit);
        List<LotteryTrend> lotteryTrends = dao.selectByExample(example);

        // 一期彩票的所有号码数量
        int singleIssueBallsNum = lotteryType.getRedBallsNum() + lotteryType.getBlueBallsNum();
        int groupNum = 2;
        // 两期号码一组
        List<List<LotteryTrend>> lists = CollectionUtils.subList(lotteryTrends, singleIssueBallsNum * groupNum);
        // 第一组直接调用updateIntervalCount更新方法
        updateIntervalCount(lists.get(0));
        int size = lists.size();
        for (int i = 1; i < size; i++) {
            // 第二组开始，需要添加上一组的最后一期号码（一整期的所有红球与蓝球号码）
            int startSubIndex = (groupNum - 1) * singleIssueBallsNum;
            int endSubIndex = groupNum * singleIssueBallsNum;
            List<LotteryTrend> lastGroup = lists.get(i-1);
            List<LotteryTrend> thisList = new ArrayList<>(lastGroup.subList(startSubIndex, endSubIndex));
            thisList.addAll(lists.get(i));
            updateIntervalCount(thisList);
        }
        // 这一页是最后一页时返回null，停止递归，否则返回最后一期已更新间隔数的期号
        updateIntervalCount(lotteryType, lotteryTrends.size() < limit ? null : lotteryTrends.get(lotteryTrends.size() - 1).getIssue(), limit);
    }

    /**
     * 根据查询出来的list列表，更新号码中奖间隔数，此方法通常手动调用，定时任务不使用
     * (需要按照期号排好顺序，并且保证只有一种彩票类型，为防止OOM及保证系统性能，list不宜过大)
     * @param list
     * @return 返回更新的最后一个元素，供下次更新时使用
     */
    @Transactional
    public LotteryTrend updateIntervalCount(List<LotteryTrend> list) {
        List<LotteryTrend> waitUpdateList = new ArrayList<>();
        // LinkedHashMap<期号, Map<颜色+";"+号码, LotteryTrend>>
        LinkedHashMap<Integer, Map<String, LotteryTrend>> issueMap = list.stream()
                .filter(item -> StringUtils.isNotBlank(item.getLotteryType()))
                .collect(Collectors.groupingBy(LotteryTrend::getIssue, LinkedHashMap::new,
                Collectors.toMap(item -> item.getColor() + ";" + item.getNumber(), item -> item)));
        // 期号有序，切默认认为中间无缺失数据
        Integer[] integers = issueMap.keySet().toArray(new Integer[]{});
        for (int i = 1; i < integers.length; i++) {
            // 从第二个元素开始计算间隔数
            Map<String, LotteryTrend> colorNumerMap = issueMap.get(integers[i]); // 本期中奖情况
            Map<String, LotteryTrend> lastTrendData = issueMap.get(integers[i - 1]); // 上期中奖情况
            for (String key : colorNumerMap.keySet()) {
                LotteryTrend entity = colorNumerMap.get(key);
                waitUpdateList.add(entity);
                if (entity.getHit()) {
                    // 本期该号码中奖了，则间隔数重置为0
                    entity.setIntervalCount(0);
                    continue;
                }
                // 本期该号码未中奖，则本期该号码间隔数为上一期该号码的间隔数+1
                entity.setIntervalCount(lastTrendData.get(key).getIntervalCount() + 1);
            }
        }
        // 批量更新修改后的数据
        dao.batchDelete(waitUpdateList);
        dao.batchInsert(waitUpdateList);
        return waitUpdateList.get(waitUpdateList.size() - 1);
    }
}
