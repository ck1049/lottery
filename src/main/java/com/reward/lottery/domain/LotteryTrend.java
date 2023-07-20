package com.reward.lottery.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Table;

/**
 * <p>
 * 彩票走势表
 * </p>
 *
 * @author loafer
 * @since 2023-07-20
 */
@Getter
@Setter
@Table(name = "lottery_trend")
@NoArgsConstructor
@AllArgsConstructor
public class LotteryTrend implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID **/
    private Integer id;

    /** 彩票类型 **/
    private String lotteryType;

    /** 期次 **/
    private Integer issue;

    /** 颜色 **/
    private String color;

    /** 号码 **/
    private String number;

    /** 间隔数 **/
    private Integer intervalCount;

    /** 命中 **/
    private Boolean hit;

    public LotteryTrend(String lotteryType) {
        this.lotteryType = lotteryType;
    }

}
