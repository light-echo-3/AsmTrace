package com.example.testandroid51.algorithm.sort;

import org.junit.Test;


/**
 * @author Hdq on 2022/12/1.
 */
public class BubblingSort {

    @Test
    public void test(){
        int[] array = {3,2,1};
        sort(array);
        printArray(array);
    }

    private void printArray(int[] array) {
        System.out.println("-------------");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
        }
        System.out.println("");
        System.out.println("-------------");
    }

    public void sort(int[] nums){
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = 0; j < nums.length - i - 1; j++) {
                if (nums[j] > nums[j + 1]) {
                    int temp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = temp;
                }
            }
        }
    }

}
