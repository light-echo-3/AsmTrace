package com.wuzhu.testandroid51.algorithm.backtrack;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 93. 复原 IP 地址
 * https://leetcode.cn/problems/restore-ip-addresses/
 * 图解：https://github.com/youngyangyang04/leetcode-master/blob/master/problems/0093.%E5%A4%8D%E5%8E%9FIP%E5%9C%B0%E5%9D%80.md
 * @author Hdq on 2022/11/30.
 */
public class IP {

    List<String> results = new ArrayList<>();
    int depth;

    @Test
    public void test() {
//        String s = "25525511135";
        String s = "123456";
        System.out.println("length = " + s.length());
        backtrack(s,0,new LinkedList<Integer>());
        System.out.println(results);
    }

    private void backtrack(String ipString,int startIndex,LinkedList<Integer> track){
        depth ++ ;
        printIndent(depth);
        System.out.println("enter ipString = " + ipString + ", startIndex = " + startIndex + ", track = " + track);
        if (track.size() == 4) {//四条树枝，加上root，总共5层node
            if (startIndex == ipString.length()) {//叶子节点，数据全挂在树枝上了（四条树枝），不能再有数据了才是合法ip
                printIndent(depth);
                System.out.println("得到结果：" + track);
                results.add(track.toString());
            }
            printIndent(depth);
            System.out.println("return depth = " + depth);

            depth -- ;
            return;
        }

//        printIndent(depth);
//        System.out.println("==递归前：for之外");

        for (int i = 0; i < 3; i++) {
            int endIndex = startIndex + i + 1;

            int ipSegment = getIpSegment(ipString,startIndex,endIndex);
            if (ipSegment == -1){
                continue;//剪枝
            }
            track.addLast(ipSegment);
//            printIndent(depth);
//            System.out.println("====递归-前：for之内");
            backtrack(ipString,endIndex,track);
//            printIndent(depth);
//            System.out.println("====递归-后：for之内");
            track.removeLast();
        }

//        printIndent(depth);
//        System.out.println("==递归后：for之外");

        printIndent(depth);
        System.out.println("return depth = " + depth);

        depth -- ;
    }

    private void printIndent(int i) {
        for (int j = 0; j < i; j++) {
            System.out.printf(" - ");
        }
    }

    private int getIpSegment(String ipString,int startIndex, int endIndex) {
        if (endIndex > ipString.length()) {
            return -1;
        }
        String ipSegment = ipString.substring(startIndex,endIndex);
        if (ipSegment.length() > 1 && ipSegment.startsWith("0")){
            return -1;
        }
        int ipSegmentInt = Integer.parseInt(ipSegment);
        return ipSegmentInt > 255 ? -1 : ipSegmentInt;
    }

}

