package com.reward.lottery.common.enumeration;

import lombok.Getter;

@Getter
public enum LotteryType {

    TWO_COLOR_BALL(1, "TWO_COLOR_BALL", 33, 16),
    LOTTO(2, "LOTTO", 35, 12);

    private final Integer code;
    private final String type;
    private final Integer redBallsNum;
    private final Integer blueBallsNum;

    LotteryType(Integer code, String type, Integer redBallsNum, Integer blueBallsNum) {
        this.code = code;
        this.type = type;
        this.redBallsNum = redBallsNum;
        this.blueBallsNum = blueBallsNum;
    }

}
