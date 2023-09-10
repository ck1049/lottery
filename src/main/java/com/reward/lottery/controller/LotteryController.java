package com.reward.lottery.controller;

import com.reward.lottery.model.LotteryTrend;
import com.reward.lottery.service.ILotteryService;
import com.reward.lottery.vo.HistoricalInformationVo;
import com.reward.lottery.vo.LotteryInformationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

@Api(tags = "彩票")
@RestController
@RequestMapping("lottery")
public class LotteryController {

    @Resource(name = "lotteryServiceImpl")
    private ILotteryService service;


    @ApiOperation(value = "保存双色球全部历史开奖信息开奖信息")
    @GetMapping("lotteryInformation")
    public ResponseEntity<List<LotteryInformationVo>> lotteryInformation() {
        return ResponseEntity.ok(service.lotteryInformation());
    }

    @ApiOperation(value = "保存双色球全部历史开奖信息开奖信息")
    @GetMapping({"historicalInformation/{enName}/{minIssueNumber}",
            "historicalInformation/{enName}/{minIssueNumber}/{pageSize}"})
    public ResponseEntity<List<HistoricalInformationVo>> historicalInformation(
            @ApiParam(value = "彩票类型", allowableValues = "TWO_COLOR_BALL,LOTTO", required = true) @PathVariable(value = "enName") String enName,
            @ApiParam(value = "最小期号(本页数据不含该期号)", required = true) @PathVariable(value = "minIssueNumber") String minIssueNumber,
            @ApiParam(value = "本页条目数") @PathVariable(value = "pageSize", required = false) Integer pageSize) {
        return ResponseEntity.ok(service.historicalInformation(enName, minIssueNumber, pageSize == null ? 20 : pageSize));
    }

    @ApiOperation(value = "保存双色球全部历史开奖信息开奖信息")
    @GetMapping({"trend/{enName}", "trend/{enName}/{pageSize}"})
    public ResponseEntity<List<LotteryTrend>> trend(
            @ApiParam(value = "彩票类型", allowableValues = "TWO_COLOR_BALL,LOTTO", required = true) @PathVariable(name = "enName") String enName,
            @ApiParam(value = "本页条目数") @PathVariable(name = "pageSize", required = false) Integer pageSize) {
        return ResponseEntity.ok(service.trend(enName, pageSize == null ? 20 : pageSize));
    }
}
