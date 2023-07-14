package com.reward.lottery.service;

import com.reward.lottery.domain.Lotto;
import java.util.List;

public interface ILottoService {

    void save();

    void saveLast();

    /**
     * 根据期号保存大乐透开奖信息
     * @param start
     * @param end
     */
    void saveByIssueNumbers(String start, String end);

    /**
     * 根据期号范围查询大乐透开奖信息
     * @param start
     * @param end
     * @return
     */
    List<Lotto> getByIssueNumbers(String start, String end);

    List<Lotto> queryAll(Integer start, Integer pageSize);

    Lotto queryById(String id);

    /**
     * 获取最近一期大乐透
     * @return
     */
    Lotto getLastlotto();

    Lotto queryByIssueNumber(Integer issueNumber);


    Lotto queryByAwardDate(String date);

    /**
     * 根据给出的大乐透号码计算成本
     * @param redBalls 红球
     * @param blueBalls 蓝球
     * @param additionalMultiple 追加倍数
     * @return
     */
    Long costCalculationByNumber(String redBalls, String blueBalls, Integer additionalMultiple);

    /**
     * 根据给出的大乐透号码计算成本
     * @param multipleType 复式类型 例：6,3 或 6，3 或 6+3
     * @param additionalMultiple 追加倍数
     * @return
     */
    Long costCalculationByMultipleType(String multipleType, Integer additionalMultiple);

    /**
     * 根据给出的大乐透号码计算组合数
     * @param redBalls 红球
     * @param blueBalls 蓝球
     * @return
     */
    Long getCombinationsByNumber(String redBalls, String blueBalls);

    /**
     * 根据复式类型计算组合数
     * @param multipleType 复式类型 例：6,3 或 6，3 或 6+3
     * @return
     */
    Long getCombinationsByMultipleType(String multipleType);

    /**
     * 根据期号查询或更新大乐透开奖数据
     */
    void updateLottoByIssueNumber(Lotto lotto);
}
