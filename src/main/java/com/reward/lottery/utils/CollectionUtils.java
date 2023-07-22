package com.reward.lottery.utils;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtils extends org.springframework.util.CollectionUtils {

    public static <T> List<List<T>> subList(List<T> list, int size) {
        List<List<T>> result = new ArrayList<>();
        if (list.size() <= size) {
            result.add(list);
            return result;
        }

        int startIndex = 0;
        while(startIndex + size <= list.size()) {
            result.add(list.subList(startIndex, startIndex += size));
        }
        return result;
    }
}
