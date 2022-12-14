package com.reward.lottery;

import com.reward.lottery.service.LottoService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class LotteryApplicationTests {

    @Autowired
    private LottoService lottoService;

    @Test
    void contextLoads() {
        System.out.println(lottoService.getLastlotto());
    }

}
