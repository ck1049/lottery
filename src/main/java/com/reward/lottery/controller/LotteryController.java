package com.reward.lottery.controller;

import com.reward.lottery.domain.LotteryTrend;
import com.reward.lottery.service.ILotteryService;
import com.reward.lottery.vo.HistoricalInformationVo;
import com.reward.lottery.vo.LotteryInformationVo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("lottery")
public class LotteryController {

    @Resource(name = "lotteryServiceImpl")
    private ILotteryService service;


    @GetMapping("lotteryInformation")
    public ResponseEntity<List<LotteryInformationVo>> lotteryInformation() {
        return ResponseEntity.ok(service.lotteryInformation());
    }

    @GetMapping("historicalInformation/{enName}/{minIssueNumber}/{pageSize}")
    public ResponseEntity<List<HistoricalInformationVo>> historicalInformation(
            @PathVariable(value = "enName") String enName,
            @PathVariable(value = "minIssueNumber") String minIssueNumber,
            @PathVariable(value = "pageSize", required = false) Integer pageSize) {
        return ResponseEntity.ok(service.historicalInformation(enName, minIssueNumber, pageSize));
    }

    @GetMapping("trend")
    public ResponseEntity<List<LotteryTrend>> trend(@RequestParam(name = "enName") String enName) {
        return ResponseEntity.ok(service.trend(enName));
    }
}
