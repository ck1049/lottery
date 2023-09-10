package com.reward.lottery.controller;

import com.reward.lottery.service.ITwoColorBallService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

@Api(tags = "双色球")
@RestController
@RequestMapping("/twoColorBall")
public class TwoColorBallController {

    @Resource(name = "twoColorBallServiceImpl")
    private ITwoColorBallService service;

    @ApiOperation(value = "保存双色球全部历史开奖信息开奖信息")
    @GetMapping("/saveTwoColorBallStatistics")
    public String saveTwoColorBallStatistics(){
        service.save();
        return "保存成功！";
    }

    @ApiOperation(value = "保存双色球最近一期开奖信息")
    @GetMapping("saveLast")
    public String saveLastLottoInfo(){
        service.saveLast();
        return "保存成功！";
    }
}
