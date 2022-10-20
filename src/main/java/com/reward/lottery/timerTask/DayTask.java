package com.reward.lottery.timerTask;

import com.reward.lottery.service.LottoService;
import com.reward.lottery.service.TwoColorBallService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
public class DayTask {

    @Autowired
    private LottoService lottoService;

    @Autowired
    private TwoColorBallService twoColorBallService;

    @Scheduled(cron = "0 0 0,1,3,5,20,21,22,23 * * ?")
    void dayTask(){
        lottoService.saveLast();
        log.info("乐透开奖信息保存任务执行完成！");
        twoColorBallService.saveLast();
        log.info("双色球开奖信息保存任务执行完成！");
    }
}
