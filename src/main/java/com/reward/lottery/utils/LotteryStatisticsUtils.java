package com.reward.lottery.utils;

import com.reward.lottery.vo.LotteryResVo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LotteryStatisticsUtils {
    /**
     *
     * @param url 访问路径
     * @return
     */
    public static Document getDocument(String url){
        //5000是设置连接超时时间，单位ms
        try {
            return Jsoup.connect(url).timeout(10000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<LotteryResVo> historyList(){
        return historyList(null);
    }

    public static List<LotteryResVo> historyList(String type){
        return historyList( type, null, null);
    }

    public static List<LotteryResVo> historyList(String type, String start, String end){
        String typeStr = StringUtils.isBlank(type) ? "ssq" : type;
        String startNumber = StringUtils.isNotBlank(start) ? start : "03001";//03001代表03年第一期彩票
        List<LotteryResVo> list = new ArrayList<>();
        String url = "https://datachart.500.com/" + typeStr + "/history/newinc/history.php?start=" + startNumber;
        if (StringUtils.isNotBlank(end)){
            url += "&end=" + end;
        }
        Document doc = getDocument(url);
        //获取目标HTML代码
        Elements trs = doc.select("tbody[id=tdata]").select("tr");
        trs.forEach(e -> {
            LotteryResVo lotteryResVo = new LotteryResVo();
            Elements tds = e.select("td");
            lotteryResVo.setNumber(tds.get(1).text()+tds.get(2).text()+tds.get(3).text()+tds.get(4).text()+tds.get(5).text()+tds.get(6).text()+tds.get(7).text());
            lotteryResVo.setIssueNumber(Integer.parseInt(tds.get(0).text()));
            if ("ssq".equals(typeStr)){
                lotteryResVo.setAwardDate(tds.get(15).text());
                lotteryResVo.setFirstPrizeNumber(tds.get(10).text());
                lotteryResVo.setFirstPrizeAmount(tds.get(11).text().replaceAll(",", ""));
                lotteryResVo.setSecondPrizeNumber(tds.get(12).text());
                lotteryResVo.setSecondPrizeAmount(tds.get(13).text().replaceAll(",", ""));
                lotteryResVo.setBonusPool(tds.get(9).text().replaceAll(",", ""));
                lotteryResVo.setTotalBets(tds.get(14).text().replaceAll(",", ""));
            }else if ("dlt".equals(typeStr)){
                lotteryResVo.setAwardDate(tds.get(14).text());
                lotteryResVo.setFirstPrizeNumber(tds.get(9).text());
                lotteryResVo.setFirstPrizeAmount(tds.get(10).text().replaceAll(",", ""));
                lotteryResVo.setSecondPrizeNumber(tds.get(11).text());
                lotteryResVo.setSecondPrizeAmount(tds.get(12).text().replaceAll(",", ""));
                lotteryResVo.setBonusPool(tds.get(8).text().replaceAll(",", ""));
                lotteryResVo.setTotalBets(tds.get(13).text().replaceAll(",", ""));
            }
            list.add(lotteryResVo);
        });
        return list;
    }

    public static LotteryResVo getLastLotteryInfo() {
        return historyList().get(0);
    }

    public static LotteryResVo getLastLotteryInfo(String type) {
        return historyList(type).get(0);
    }
}
