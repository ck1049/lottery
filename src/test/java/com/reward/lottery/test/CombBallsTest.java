package com.reward.lottery.test;

import com.reward.lottery.utils.LotteryCombinationsUtils;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class CombBallsTest {

    public static void main(String[] args) {
        long startTime = new Date().getTime();
        Integer ballsTotalNum = 35;
        int num = 6;
        List<String> arrayList = new ArrayList<>();
        Integer[] item = new Integer[num];
        for (int i = 0; i < num; i++) {
            item[i] = i + 1;
        }
        while (true) {
            int index = num - 1;
            while (true) {
                // 内层循环只做最后一位加一
                arrayList.add(String.join(",",
                        Arrays.stream(item)
                                .map(it -> String.format("%02d", it))
                                .collect(Collectors.joining(","))));
                item[index]++;
                if (item[index] > ballsTotalNum + index - num + 1) {
                    break;
                }
            }

            for (int idx = index; idx > 0; idx--) {
                if (item[idx] >= ballsTotalNum + index - num + 1) {
                    item[idx-1]++;
                    item[idx] = item[idx-1] >= ballsTotalNum + index - num + 1 ? num : item[idx-1] + 1;
                }
            }

            if (item[0] == ballsTotalNum - num + 1) {
                for (int i = 1; i < num; i++) {
                    item[i] = item[i-1] + 1;
                }
                arrayList.add(String.join(",",
                        Arrays.stream(item)
                                .map(it -> String.format("%02d", it))
                                .collect(Collectors.joining(","))));
                break;
            }
        }

        Set<String> stringSet = new LinkedHashSet<>();
        for (String str : arrayList) {
            List<String> strList = Arrays.asList(str.split(","));
            Set<String> set = new HashSet<>();
            set.addAll(strList);
            if (set.size() == num) {
                strList.sort(((o1, o2) -> Integer.parseInt(o1) - Integer.parseInt(o2)));
                stringSet.add(String.join(",", strList));
            }
        }
        ArrayList<String> list = new ArrayList<>(stringSet);
        long endTime = new Date().getTime();
        BigInteger combinations = LotteryCombinationsUtils.combinations(ballsTotalNum, num);
        Map<String, List<String>> listMap = list.stream().collect(Collectors.groupingBy(it -> it.split(",")[0]));
        Set<Map.Entry<String, List<String>>> entries = listMap.entrySet();
        Map<String, Integer> sizeMap = entries.stream().collect(Collectors.toMap(Map.Entry::getKey, it -> it.getValue().size()));
        System.out.println(list);
        System.out.println("应有：" + combinations + "种组合，实际组合数：" + list.size() + "，计算花费时间：" + (endTime - startTime) + "毫秒。");
        System.out.println(sizeMap);
    }
}
