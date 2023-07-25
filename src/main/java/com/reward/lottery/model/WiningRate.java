package com.reward.lottery.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WiningRate {

    /** 中奖率分数 **/
    private String fraction;
    /** 中奖率小数 **/
    private Double rate;
}
