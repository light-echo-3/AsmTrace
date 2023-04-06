package com.example.testandroid51.algorithm.backtrack;

import org.junit.Test;

import java.util.LinkedList;

/**
 * 46. 全排列
 * https://leetcode.cn/problems/permutations/
 * @author Hdq on 2022/11/30.
 */
public class AllPermutation {

    @Test
    public void test(){
        int[] array = {1,2,3};
        backtrack(array,new LinkedList<Integer>());
    }

    private void backtrack(int[] array, LinkedList<Integer> track) {
        if (track.size() == array.length) {
            System.out.println("" + track);
            return;
        }

        for (int i = 0; i < array.length; i++) {
            int value = array[i];
            if (track.contains(value)) {
                continue;//剪枝
            }
            track.addLast(value);
            backtrack(array,track);
            track.removeLast();
        }
    }

}
