package com.reward.lottery.controller;

import com.github.pagehelper.PageInfo;
import com.reward.lottery.domain.Lotto;
import com.reward.lottery.service.LottoService;
import com.reward.lottery.utils.LotteryStatisticsUtils;
import com.reward.lottery.utils.LotteryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("lotto")
public class LottoController {

    @Autowired
    private LottoService lottoService;

    @RequestMapping("saveLottoStatistics")
    public String saveLottoStatistics(){
        lottoService.save();
        return "保存成功！";
    }

    @RequestMapping("saveLast")
    public String saveLastLottoInfo(){
        lottoService.saveLast();
        return "保存成功！";
    }

    @RequestMapping("randomNumber.do")
    public ResponseEntity<List<String>> randomNumber(){
        return ResponseEntity.ok(LotteryUtils.randomLottery("dlt"));
    }

    @RequestMapping("index/{start}")
    public ResponseEntity<PageInfo<Lotto>> index(@PathVariable(value = "start") Integer start){
        List<Lotto> lottos = lottoService.queryAll(start);
        PageInfo<Lotto> pageInfo = new PageInfo<>(lottos);
        return ResponseEntity.ok(pageInfo);
    }

    @RequestMapping("index/{start}.html")
    public String index(@PathVariable(value = "start") Integer start, Model model){
        List<Lotto> lottos = lottoService.queryAll(start);
        PageInfo<Lotto> pageInfo = new PageInfo<>(lottos);
        model.addAttribute("pageInfo", pageInfo);
        return "index";
    }

    @GetMapping("lastLotto")
    public ResponseEntity<Lotto> queryLastLottoInfo(){
        Lotto lotto = LotteryUtils.setAndReturnLotto(LotteryStatisticsUtils.getLastLotteryInfo("dlt"));
        return ResponseEntity.ok(lotto);
    }

    @GetMapping("{id}")
    public ResponseEntity<Lotto> queryById(@PathVariable("id") String id){
        return ResponseEntity.ok(lottoService.queryById(id));
    }

    @GetMapping("queryByIssueNumber/{issueNumber}")
    public ResponseEntity<Lotto> queryByIssueNumber(@PathVariable("issueNumber") String issueNumber){
        return ResponseEntity.ok(lottoService.queryByIssueNumber(issueNumber));
    }

    @GetMapping("queryByDate/{date}")
    public ResponseEntity<Lotto> queryByDate(@PathVariable("date") String date){
        return ResponseEntity.ok(lottoService.queryByAwardDate(date));
    }

    @GetMapping(value = {"costCalculationByNumber/{redBalls}/{blueBalls}/{additionalMultiple}","costCalculationByNumber/{redBalls}/{blueBalls}"})
    public ResponseEntity<Long> costCalculationByNumber(
            @PathVariable("redBalls") String redBalls,
            @PathVariable("blueBalls") String blueBalls,
            @PathVariable(value = "additionalMultiple", required = false) Integer additionalMultiple
            ) {
        return ResponseEntity.ok(lottoService.costCalculationByNumber(redBalls, blueBalls, additionalMultiple == null ? 0 : additionalMultiple));
    }

    @GetMapping(value = {"costCalculationByMultipleType/{multipleType}/{additionalMultiple}", "costCalculationByMultipleType/{multipleType}"})
    public ResponseEntity<Long> costCalculationByMultipleType(
            @PathVariable("multipleType") String multipleType,
            @PathVariable(value = "additionalMultiple", required = false) Integer additionalMultiple) {
        return ResponseEntity.ok(lottoService.costCalculationByMultipleType(multipleType, additionalMultiple == null ? 0 : additionalMultiple));
    }
}
