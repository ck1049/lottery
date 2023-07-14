package com.reward.lottery.timerTask;

import com.reward.lottery.service.impl.LottoServiceImpl;
import com.reward.lottery.service.impl.TwoColorBallServiceImpl;
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
    private LottoServiceImpl lottoServiceImpl;

    @Autowired
    private TwoColorBallServiceImpl twoColorBallServiceImpl;

    @Scheduled(cron = "0 0 0,1,3,5,20,21,22,23 * * ?")
    void dayTask(){
        lottoServiceImpl.saveLast();
        log.info("乐透开奖信息保存任务执行完成！");
        twoColorBallServiceImpl.saveLast();
        log.info("双色球开奖信息保存任务执行完成！");
    }
}
