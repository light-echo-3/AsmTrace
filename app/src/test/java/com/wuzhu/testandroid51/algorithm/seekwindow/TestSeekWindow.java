package com.wuzhu.testandroid51.algorithm.seekwindow;

import org.junit.Test;

/**
 * @author Hdq on 2022/12/1.
 */
public class TestSeekWindow {

    @Test
    public void test(){
        String s = "ADOBECODEBANC";//C ODEBA N C
        System.out.println();
        System.out.println("length = " + s.length());
        System.out.println(minWindow(s,"ABC"));
        System.out.println();
        System.out.println();
    }


    public String minWindow(String s, String t) {
        int left = 0,right = 1;
        String subString;
        String minString = null;
        int minStringLength = Integer.MAX_VALUE;

        while (right < s.length()) {
            //优化代码
//            subString = s.substring(left,right);
//            if (!isValidSubString(subString,t)){//不满足条件,右移窗口
//                right ++;
//            }
            right ++;

            subString = s.substring(left,right);
            while (isValidSubString(subString,t)) {//满足条件，左移窗口
                if (right - left < minStringLength) {
                    minString = subString;
                    minStringLength = minString.length();
                }
                left ++;
                System.out.println("left = " + left + ",right = " + right + ",minStringLength = " + minStringLength);
                subString = s.substring(left,right);
            }
        }
        return minString;
    }

//    /* 滑动窗口算法框架 */
//    void slidingWindow(String s, String t) {
//        int left = 0, right = 0;
//        while (right < s.length()) {
//            // 右移（增大）窗口
//            right++;
//            // 进行窗口内数据的一系列更新
//
//            while (left window needs shrink) {
//                // 左移（缩小）窗口
//                left++;
//                // 进行窗口内数据的一系列更新
//            }
//        }
//    }

//    public String minWindow(String s, String t) {
//        int left = 0,right = 1;
//        String subString;
//        String minString = null;
//        int minStringLength = Integer.MAX_VALUE;
//
//        while (right < s.length()) {
//            subString = s.substring(left,right);
//            if (!isValidSubString(subString,t)){//不满足条件,右移窗口
//                right ++;
//            }
//
//            subString = s.substring(left,right);
//            while (isValidSubString(subString,t)) {//满足条件，左移窗口
//                if (right - left < minStringLength) {
//                    minString = subString;
//                    minStringLength = minString.length();
//                }
//                left ++;
//                System.out.println("left = " + left + ",right = " + right + ",minStringLength = " + minStringLength);
//                subString = s.substring(left,right);
//            }
//        }
//        return minString;
//    }

    private boolean isValidSubString(String subString, String t) {
        for (int i = 0; i < t.length(); i++) {
            if (!subString.contains(t.charAt(i) + "")) {
                return false;
            }
        }
        return true;
    }


}
