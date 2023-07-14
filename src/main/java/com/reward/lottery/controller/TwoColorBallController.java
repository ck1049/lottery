package com.reward.lottery.controller;

import com.reward.lottery.service.ITwoColorBallService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/twoColorBall")
public class TwoColorBallController {

    @Resource(name = "twoColorBallServiceImpl")
    private ITwoColorBallService service;

    @RequestMapping("/saveTwoColorBallStatistics")
    public String saveTwoColorBallStatistics(){
        service.save();
        return "保存成功！";
    }

    @RequestMapping("saveLast")
    public String saveLastLottoInfo(){
        service.saveLast();
        return "保存成功！";
    }
}
