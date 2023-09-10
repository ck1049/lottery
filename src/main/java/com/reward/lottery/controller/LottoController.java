package com.reward.lottery.controller;

import com.github.pagehelper.PageInfo;
import com.reward.lottery.common.enumeration.LotteryType;
import com.reward.lottery.model.Lotto;
import com.reward.lottery.model.WiningRate;
import com.reward.lottery.service.ILottoService;
import com.reward.lottery.utils.LotteryCombinationsUtils;
import com.reward.lottery.utils.LotteryStatisticsUtils;
import com.reward.lottery.utils.LotteryUtils;
import com.reward.lottery.utils.MathUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Api(tags = "大乐透")
@Controller
@RequestMapping("lotto")
public class LottoController {

    @Resource(name = "lottoServiceImpl")
    private ILottoService lottoService;

    @ApiOperation(value = "保存大乐透全部历史开奖信息")
    @GetMapping("saveLottoStatistics")
    @ResponseBody
    public String saveLottoStatistics(){
        lottoService.save();
        return "保存成功！";
    }

    @ApiOperation(value = "保存大乐透最近一期开奖信息")
    @GetMapping("saveLast")
    @ResponseBody
    public String saveLastLottoInfo(){
        lottoService.saveLast();
        return "保存成功！";
    }

    @ApiOperation(value = "根据期号保存大乐透开奖信息")
    @GetMapping("saveIssueNumbers")
    @ResponseBody
    public String saveByIssueNumbers(
            @ApiParam(value = "开始期号") @RequestParam(name = "start", required = false) String start,
            @ApiParam(value = "结束期号") @RequestParam(name = "end", required = false) String end) {
        lottoService.saveByIssueNumbers(start, end);
        return "保存成功！";
    }

    @ApiOperation(value = "最近一期大乐透号码(实时)")
    @GetMapping("lastLotto")
    @ResponseBody
    public ResponseEntity<Lotto> queryLastLottoInfo(){
        Lotto lotto = LotteryUtils.setAndReturnLotto(LotteryStatisticsUtils.getLastLotteryInfo("dlt"));
        return ResponseEntity.ok(lotto);
    }

    @ApiOperation(value = "根据期号查询开奖信息(非实时)")
    @GetMapping("queryByIssueNumber/{issueNumber}")
    @ResponseBody
    public ResponseEntity<Lotto> queryByIssueNumber(
            @ApiParam(value = "期号", required = true) @PathVariable("issueNumber") Integer issueNumber){
        return ResponseEntity.ok(lottoService.queryByIssueNumber(issueNumber));
    }

    @ApiOperation(value = "根据期号查询大乐透开奖信息(实时)")
    @GetMapping(value = {"getByIssueNumbersRealTime/{start}/{end}", "getByIssueNumbersRealTime/{start}"})
    @ResponseBody
    public ResponseEntity<List<Lotto>> getByIssueNumbersRealTime(
            @ApiParam(value = "开始期号", required = true) @PathVariable(value = "start") String start,
            @ApiParam(value = "结束期号") @PathVariable(value = "end", required = false) String end){
        return ResponseEntity.ok(lottoService.getByIssueNumbers(start, end));
    }

    @ApiOperation(value = "随机生成号码(支持复式)")
    @GetMapping(value={"randomNumber", "randomNumber/{multipleType}"})
    @ResponseBody
    public ResponseEntity<Map<String, List<String>>> randomNumber(
            @ApiParam(value = "复式类型") @PathVariable(value = "multipleType", required = false) String multipleType
    ){
        return ResponseEntity.ok(LotteryUtils.randomLottery(LotteryType.LOTTO.getType(), multipleType));
    }

    @ApiOperation(value = "分页查询")
    @GetMapping(value = {"index/{pageNum}", "index/{pageNum}/{pageSize}"})
    @ResponseBody
    public ResponseEntity<PageInfo<Lotto>> select(
            @ApiParam(value = "页码", required = true) @PathVariable(value = "pageNum") Integer pageNum,
            @ApiParam(value = "每页条目数") @PathVariable(value = "pageSize", required = false) Integer pageSize){
        List<Lotto> lottos = lottoService.queryAll(pageNum, pageSize == null ? 20 : pageSize);
        PageInfo<Lotto> pageInfo = new PageInfo<>(lottos);
        return ResponseEntity.ok(pageInfo);
    }

    @ApiOperation(value = "thymeleaf页面")
    @GetMapping("index/{pageNum}.html")
    public String index(@ApiParam(value = "页码", required = true) @PathVariable(value = "pageNum") Integer pageNum, Model model){
        List<Lotto> lottos = lottoService.queryAll(pageNum, 20);
        PageInfo<Lotto> pageInfo = new PageInfo<>(lottos);
        model.addAttribute("pageInfo", pageInfo);
        return "index";
    }

    @ApiOperation(value = "根据开奖日期查询大乐透开奖信息")
    @GetMapping("queryByDate/{date}")
    @ResponseBody
    public ResponseEntity<Lotto> queryByDate(@ApiParam(value = "开奖日期", required = true) @PathVariable("date") String date){
        return ResponseEntity.ok(lottoService.queryByAwardDate(date));
    }

    @ApiOperation(value = "根据红球个数、蓝球个数[追加倍数]计算成本")
    @GetMapping(value = {"costCalculationByNumber/{redBalls}/{blueBalls}/{additionalMultiple}",
            "costCalculationByNumber/{redBalls}/{blueBalls}"})
    @ResponseBody
    public ResponseEntity<Long> costCalculationByNumber(
            @ApiParam(value = "红球个数", required = true) @PathVariable("redBalls") String redBalls,
            @ApiParam(value = "蓝球个数", required = true) @PathVariable("blueBalls") String blueBalls,
            @ApiParam(value = "追加倍数") @PathVariable(value = "additionalMultiple", required = false) Integer additionalMultiple
            ) {
        return ResponseEntity.ok(lottoService.costCalculationByNumber(redBalls, blueBalls, additionalMultiple == null ? 0 : additionalMultiple));
    }

    @ApiOperation(value = "根据复式类型[追加倍数]计算成本")
    @GetMapping(value = {"costCalculationByMultipleType/{multipleType}",
            "costCalculationByMultipleType/{multipleType}/{additionalMultiple}"})
    @ResponseBody
    public ResponseEntity<Long> costCalculationByMultipleType(
            @ApiParam(value = "复式类型", required = true) @PathVariable("multipleType") String multipleType,
            @ApiParam(value = "追加倍数") @PathVariable(value = "additionalMultiple", required = false) Integer additionalMultiple) {
        return ResponseEntity.ok(lottoService.costCalculationByMultipleType(multipleType, additionalMultiple == null ? 0 : additionalMultiple));
    }

    @ApiOperation(value = "根据复式类型计算一等奖中奖概率")
    @GetMapping("winingRateByMultipleType/{multipleType}")
    @ResponseBody
    public ResponseEntity<WiningRate> winingRateByMultipleType(
            @ApiParam(value = "复式类型", required = true) @PathVariable("multipleType") String multipleType) {
        Long numberCombinations = lottoService.getCombinationsByMultipleType(multipleType);
        BigInteger totalCombinations = LotteryCombinationsUtils.getLottoCombinations();
        Long[] fractionArr = MathUtils.reductionFraction(numberCombinations, totalCombinations.longValue());
        return ResponseEntity.ok(new WiningRate(fractionArr[0] + "/" + fractionArr[1], numberCombinations.doubleValue() / totalCombinations.doubleValue()));
    }

    @ApiOperation(value = "根据红球个数、蓝球个数计算一等奖中奖概率")
    @GetMapping("winingRateByNumbers/{redBalls}/{blueBalls}")
    @ResponseBody
    public ResponseEntity<WiningRate> winingRateByNumbers(
            @ApiParam(value = "红球个数", required = true) @PathVariable("redBalls") String redBalls,
            @ApiParam(value = "蓝球个数", required = true) @PathVariable("blueBalls") String blueBalls) {
        Long numberCombinations = lottoService.getCombinationsByNumber(redBalls, blueBalls);
        BigInteger totalCombinations = LotteryCombinationsUtils.getLottoCombinations();
        Long[] fractionArr = MathUtils.reductionFraction(numberCombinations, totalCombinations.longValue());
        return ResponseEntity.ok(new WiningRate(fractionArr[0] + "/" + fractionArr[1], numberCombinations.doubleValue() / totalCombinations.doubleValue()));
    }
}
