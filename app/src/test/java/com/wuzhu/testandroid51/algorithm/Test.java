package com.wuzhu.testandroid51.algorithm;

/**
 * @author Hdq on 2022/12/13.
 */
public class Test {

    @org.junit.Test
    public void test(){
        int[] array = {10,10,9,9,8,7,5,6,4,3,4,2};
        int k = 3;
        int result = sort(array,0, array.length, k);
        System.out.println("-------" + result);
    }

    private int sort(int[] array,int start,int end,int k) {
        if (start >= end) {
            return - 1;
        }

        int mid = partition(array,start,end);
        if (mid == end - 1 - k) {
            return array[mid];
        }

        int result;
        if (mid < k) {
            result = sort(array,mid + 1,end,k);
        } else {
            result = sort(array,start,mid -1,k);
        }
        return result;
    }

    private int partition(int[] array, int start, int end) {
        int cur = array[start];
        int leftIndex = start + 1;
        int rightIndex = end - 1;
        while (start < end) {
            while (array[leftIndex] <= cur) {
                leftIndex ++ ;
            }
            while (array[rightIndex] >= cur) {
                rightIndex -- ;
            }
            if (start > end){
                break;
            }
            //交换
            swap(array,leftIndex,rightIndex);

        }
        swap(array,start,rightIndex);

        return rightIndex;
    }

    private void swap(int[] array, int leftIndex, int rightIndex) {
        int temp = array[leftIndex];
        array[leftIndex] = array[rightIndex];
        array[rightIndex] = temp;
    }


}
