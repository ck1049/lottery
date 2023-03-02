package com.reward.lottery.utils;
import java.util.ArrayList;
import java.util.List;

public class Combination {

    public static void main(String[] args) {
        int num = 5;
        List<List<Integer>> result = getCombinations(num);
        for (List<Integer> combination : result) {
            System.out.println(combination);
        }
    }

    public static List<List<Integer>> getCombinations(int num) {
        List<List<Integer>> result = new ArrayList<>();
        int[] nums = new int[num];
        for (int i = 0; i < num; i++) {
            nums[i] = i + 1;
        }
        getCombinationsHelper(result, new ArrayList<>(), nums, 0);
        return result;
    }

    private static void getCombinationsHelper(List<List<Integer>> result, List<Integer> combination, int[] nums, int index) {
        if (combination.size() == nums.length) {
            result.add(new ArrayList<>(combination));
            return;
        }
        for (int i = index; i < nums.length; i++) {
            combination.add(nums[i]);
            getCombinationsHelper(result, combination, nums, i + 1);
            combination.remove(combination.size() - 1);
        }
    }
}
