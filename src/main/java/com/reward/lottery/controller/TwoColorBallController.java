package com.reward.lottery.controller;

import com.reward.lottery.service.TwoColorBallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twoColorBall")
public class TwoColorBallController {

    @Autowired
    private TwoColorBallService twoColorBallService;

    @RequestMapping("/saveTwoColorBallStatistics")
    public String saveTwoColorBallStatistics(){
        twoColorBallService.save();
        return "保存成功！";
    }

    @RequestMapping("saveLast")
    public String saveLastLottoInfo(){
        twoColorBallService.saveLast();
        return "保存成功！";
    }
}
