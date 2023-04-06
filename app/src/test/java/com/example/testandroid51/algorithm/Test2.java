package com.example.testandroid51.algorithm;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 合并k个排序数组
 * @author Hdq on 2022/12/19.
 */
public class Test2 {

    @Test
    public void test(){
        Integer[] array1 = {1,3,8};
        Integer[] array2 = {2,4,7};
        Integer[] array3 = {5,9};

        List<Integer[]> list = new ArrayList<>();
        list.add(array1);
        list.add(array2);
        list.add(array3);
        int[] temp = new int[array1.length + array2.length + array3.length];
        mergeArray(temp,list);
    }

    private void mergeArray(int[] temp, List<Integer[]> list) {
        int[] indexs = new int[list.size()];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = getMinValue(list,indexs);
        }
        System.out.println("temp = " + Arrays.toString(temp) + ", list = " + list);
    }

    private int getMinValue(List<Integer[]> list,int[] indexs) {
        int minValue = Integer.MAX_VALUE;
        int minIndex = Integer.MAX_VALUE;

        for (int i = 0; i < list.size(); i++) {
            Integer[] array = list.get(i);
            int curIndex = indexs[i];
            int curValue;
            if (curIndex < array.length && (curValue = array[curIndex]) < minValue){
                minValue = curValue;
                minIndex = i;
            }
        }

        indexs[minIndex] ++;
        return minValue;
    }


}
