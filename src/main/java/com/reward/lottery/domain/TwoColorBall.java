package com.reward.lottery.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Table;

/**
 * 双色球
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "two_color_ball")
public class TwoColorBall extends Lottery {

    private String number;
    private String red1;
    private String red2;
    private String red3;
    private String red4;
    private String red5;
    private String red6;
    private String blue1;

    public TwoColorBall(String number, String red1, String red2, String red3, String red4, String red5, String red6, String blue1, String type, Integer sort){
        super(type, sort);
        this.number = number;
        this.red1 = red1;
        this.red2 = red2;
        this.red3 = red3;
        this.red4 = red4;
        this.red5 = red5;
        this.red6 = red6;
        this.blue1 = blue1;
    }

    public TwoColorBall(String number, String red1, String red2, String red3, String red4, String red5, String red6, String blue1, String type, Integer issueNumber, String awardDate, Integer sort){
        super(type, issueNumber, awardDate, sort);
        this.number = number;
        this.red1 = red1;
        this.red2 = red2;
        this.red3 = red3;
        this.red4 = red4;
        this.red5 = red5;
        this.red6 = red6;
        this.blue1 = blue1;
    }

}
