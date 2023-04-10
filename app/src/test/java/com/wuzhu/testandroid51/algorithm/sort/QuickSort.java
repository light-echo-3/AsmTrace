package com.wuzhu.testandroid51.algorithm.sort;

import com.wuzhu.testandroid51.algorithm.Utils;

import org.junit.Test;

import java.util.Arrays;

/**
 * 快速排序是先将一个元素排好序，然后再将剩下的元素排好序。
 * @author Hdq on 2022/12/2.
 */
public class QuickSort {

    int count = 0;

    @Test
    public void test(){
        int[] array = {3,1,2,5,4};
        sort(array);
        Utils.println(array);
    }


    private void sort(int[] nums){
        sort(nums,0,nums.length - 1);
    }

    private void sort(int[] nums,int low,int height){
        Utils.printIndent(count++);
        System.out.println("enter nums = " + Arrays.toString(nums) + ", low = " + low + ", height = " + height);

        if (low >= height) {//叶子节点，不用排序
            Utils.printIndent(--count);
            System.out.println("return " + count);
            return;
        }
        //先将一个元素排好序，然后拆分成左右两块
        int pivotIndex = partition(nums,low,height);
        //然后再将剩下的元素排好序。
        sort(nums,low,pivotIndex - 1);//排左边
        sort(nums,pivotIndex + 1,height);//排右边

        Utils.printIndent(--count);
        System.out.println("return " + count);
    }

    private int partition(int[] nums, int low, int height) {
        int pivot = nums[low];
        int leftIndex = low + 1,rightIndex = height;

        while (true){
            while (leftIndex <= height && nums[leftIndex] < pivot){
                leftIndex ++;
            }
            while (rightIndex >= low && nums[rightIndex] > pivot){
                rightIndex --;
            }
            if (leftIndex >= rightIndex){
                break;
            }
            swap(nums,leftIndex,rightIndex);
        }
        swap(nums,low,rightIndex);
        return rightIndex;
    }

    private void swap(int[] nums, int startIndex, int endIndex) {
        int temp = nums[startIndex];
        nums[startIndex] = nums[endIndex];
        nums[endIndex] = temp;
    }

}
