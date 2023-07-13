package com.reward.lottery.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 星期
 */
@Getter
@AllArgsConstructor
public enum Week {

    SUNDAY(0, "星期日",  "周日", "Sunday"),
    MONDAY(1, "星期一",  "周一", "Monday"),
    TUESDAY(2, "星期二",  "周二", "Tuesday"),
    WEDNESDAY(3, "星期三",  "周三", "Wednesday"),
    THURSDAY(4, "星期四",  "周四", "Thursday"),
    FRIDAY(5, "星期五",  "周五", "Friday"),
    SATURDAY(6, "星期六",  "周六", "Saturday");

    private final int code;
    private final String name;
    private final String simpleName;
    private final String enName;

    public static Week getByCode(int code) {
        for (Week week : Week.values()) {
            if (week.getCode() == code) {
                return week;
            }
        }
        return null;
    }
}
