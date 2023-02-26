package com.reward.lottery.controller;

import com.github.pagehelper.PageInfo;
import com.reward.lottery.common.enumeration.LotteryType;
import com.reward.lottery.domain.Lotto;
import com.reward.lottery.service.LottoService;
import com.reward.lottery.utils.LotteryStatisticsUtils;
import com.reward.lottery.utils.LotteryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("lotto")
public class LottoController {

    @Autowired
    private LottoService lottoService;

    @RequestMapping("saveLottoStatistics")
    @ResponseBody
    public String saveLottoStatistics(){
        lottoService.save();
        return "保存成功！";
    }

    @RequestMapping("saveLast")
    @ResponseBody
    public String saveLastLottoInfo(){
        lottoService.saveLast();
        return "保存成功！";
    }

    /**
     * 根据期号保存大乐透开奖信息
     * @param start
     * @param end
     */
    @RequestMapping("saveIssueNumbers")
    @ResponseBody
    public String saveByIssueNumbers(String start, String end) {
        lottoService.saveByIssueNumbers(start, end);
        return "保存成功！";
    }

    /**
     * 根据期号实时查询大乐透开奖信息
     * @param start
     * @param end
     * @return
     */
    @RequestMapping(value = {"getByIssueNumbersRealTime/{start}/{end}", "getByIssueNumbersRealTime/{start}"})
    @ResponseBody
    public ResponseEntity<List<Lotto>> getByIssueNumbersRealTime(
            @PathVariable(value = "start") String start,
            @PathVariable(value = "end", required = false) String end){
        return ResponseEntity.ok(lottoService.getByIssueNumbers(start, end));
    }

    @RequestMapping(value={"randomNumber", "randomNumber/{multipleType}"})
    @ResponseBody
    public ResponseEntity<Map<String, List<String>>> randomNumber(
            @PathVariable(value = "multipleType", required = false) String multipleType
    ){
        return ResponseEntity.ok(LotteryUtils.randomLottery(LotteryType.LOTTO.getType(), multipleType));
    }

    @RequestMapping(value = {"index/{start}", "index/{start}/{pageSize}"})
    @ResponseBody
    public ResponseEntity<PageInfo<Lotto>> index(
            @PathVariable(value = "start") Integer start,
            @PathVariable(value = "pageSize", required = false) Integer pageSize){
        List<Lotto> lottos = lottoService.queryAll(start, pageSize == null ? 20 : pageSize);
        PageInfo<Lotto> pageInfo = new PageInfo<>(lottos);
        return ResponseEntity.ok(pageInfo);
    }

    @RequestMapping("index/{start}.html")
    public String index(@PathVariable(value = "start") Integer start, Model model){
        List<Lotto> lottos = lottoService.queryAll(start, 20);
        PageInfo<Lotto> pageInfo = new PageInfo<>(lottos);
        model.addAttribute("pageInfo", pageInfo);
        return "index";
    }

    @GetMapping("lastLotto")
    @ResponseBody
    public ResponseEntity<Lotto> queryLastLottoInfo(){
        Lotto lotto = LotteryUtils.setAndReturnLotto(LotteryStatisticsUtils.getLastLotteryInfo("dlt"));
        return ResponseEntity.ok(lotto);
    }

    @GetMapping("{id}")
    @ResponseBody
    public ResponseEntity<Lotto> queryById(@PathVariable("id") String id){
        return ResponseEntity.ok(lottoService.queryById(id));
    }

    @GetMapping("queryByIssueNumber/{issueNumber}")
    @ResponseBody
    public ResponseEntity<Lotto> queryByIssueNumber(@PathVariable("issueNumber") String issueNumber){
        return ResponseEntity.ok(lottoService.queryByIssueNumber(issueNumber));
    }

    @GetMapping("queryByDate/{date}")
    @ResponseBody
    public ResponseEntity<Lotto> queryByDate(@PathVariable("date") String date){
        return ResponseEntity.ok(lottoService.queryByAwardDate(date));
    }

    @GetMapping(value = {"costCalculationByNumber/{redBalls}/{blueBalls}/{additionalMultiple}","costCalculationByNumber/{redBalls}/{blueBalls}"})
    @ResponseBody
    public ResponseEntity<Long> costCalculationByNumber(
            @PathVariable("redBalls") String redBalls,
            @PathVariable("blueBalls") String blueBalls,
            @PathVariable(value = "additionalMultiple", required = false) Integer additionalMultiple
            ) {
        return ResponseEntity.ok(lottoService.costCalculationByNumber(redBalls, blueBalls, additionalMultiple == null ? 0 : additionalMultiple));
    }

    @GetMapping(value = {"costCalculationByMultipleType/{multipleType}/{additionalMultiple}", "costCalculationByMultipleType/{multipleType}"})
    @ResponseBody
    public ResponseEntity<Long> costCalculationByMultipleType(
            @PathVariable("multipleType") String multipleType,
            @PathVariable(value = "additionalMultiple", required = false) Integer additionalMultiple) {
        return ResponseEntity.ok(lottoService.costCalculationByMultipleType(multipleType, additionalMultiple == null ? 0 : additionalMultiple));
    }
}
