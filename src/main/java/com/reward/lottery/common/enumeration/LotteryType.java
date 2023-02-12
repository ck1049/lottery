package com.reward.lottery.common.enumeration;

public enum LotteryType {

    TWO_COLOR_BALL(1, "TWO_COLOR_BALL"),
    LOTTO(2, "LOTTO");

    private final Integer code;
    private final String type;

    LotteryType(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public Integer getCode() {
        return code;
    }

    public String getType() {
        return type;
    }
}
