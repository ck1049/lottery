package com.reward.lottery.controller;

import com.reward.lottery.service.LotteryService;
import com.reward.lottery.vo.LotteryInformationVo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("lottery")
public class LotteryController {

    @Resource(name = "lotteryService")
    private LotteryService lotteryService;


    @GetMapping("lotteryInformation")
    public ResponseEntity<List<LotteryInformationVo>> lotteryInformation() {
        return ResponseEntity.ok(lotteryService.lotteryInformation());
    }
}
