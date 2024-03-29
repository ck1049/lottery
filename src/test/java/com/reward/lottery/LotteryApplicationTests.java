package com.reward.lottery;

import com.reward.lottery.common.enumeration.LotteryType;
import com.reward.lottery.service.ILotteryTrendService;
import com.reward.lottery.service.ILottoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.annotation.Resource;

@SpringBootTest
class LotteryApplicationTests {

    @Autowired
    private ILottoService iLottoService;

    @Resource(name = "lotteryTrendServiceImpl")
    private ILotteryTrendService iLotteryTrendService;

    @Test
    void contextLoads() {
        System.out.println(iLottoService.getLastlotto());
    }

    @Test
    void testupdateIntervalCount() {
        int limit = 20; // 期数
        int twoColorBallStartIssue = 23034;
        int lottoBallStartIssue = 23034;
        iLotteryTrendService.updateIntervalCount(LotteryType.TWO_COLOR_BALL, twoColorBallStartIssue, limit);
        iLotteryTrendService.updateIntervalCount(LotteryType.LOTTO, lottoBallStartIssue, limit);
    }

}
