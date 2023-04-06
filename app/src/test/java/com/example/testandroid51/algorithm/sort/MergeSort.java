package com.example.testandroid51.algorithm.sort;

import com.example.testandroid51.algorithm.Utils;

import org.junit.Test;

/**
 * @author Hdq on 2022/12/2.
 */
public class MergeSort {

    @Test
    public void test(){
        int[] nums = {2,1,0,9,8,7};
        sort(nums);
        Utils.println(nums);
    }

    private void sort(int[] nums) {
        int[] temp = new int[nums.length];
        sort(nums,temp,0,nums.length - 1);
    }

    private void sort(int[] nums,int[] temp,int low,int height) {
        if (low == height){//叶子结点
            return;
        }
        int middle = (low + height) / 2;
        sort(nums,temp,low,middle);//先把左半边数组排好序,
        sort(nums,temp,middle + 1,height);//再把右半边数组排好序,
        merge(nums,temp,low,middle,height);//然后把两半数组合并。
    }

    private void merge(int[] nums, int[] temp, int low, int middle, int height) {
        for (int i = low; i <= height; i++) {
            temp[i] = nums[i];
        }

        int leftP = low,rightP = middle + 1;
        for (int i = low; i <= height; i++) {
            if (leftP == middle + 1){//左数组结束
                nums[i] = temp[rightP++];
            } else if (rightP == height + 1) {//右数组结束
                nums[i] = temp[leftP++];
            } else if (temp[leftP] < temp[rightP]) {
                nums[i] = temp[leftP++];
            } else {
                nums[i] = temp[rightP++];
            }
        }
    }

}
