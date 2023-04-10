package com.wuzhu.testandroid51.algorithm.bfs;


import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * https://labuladong.github.io/algo/1/10/
 * @author Hdq on 2022/12/8.
 */
public class OpenLock {

    @Test
    public void test(){
        String[] deadens = {"0009"};
        System.out.println("开锁次数：" + openLock("0006",deadens));
    }

    private String plusOne(String password,int index){
        char[] chars = password.toCharArray();
        if (chars[index] == '9'){
            chars[index] = '0';
        } else {
            chars[index] += 1;
        }
        return new String(chars);
    }

    private String minusOne(String password,int index){
        char[] chars = password.toCharArray();
        if (chars[index] == '0'){
            chars[index] = '9';
        } else {
            chars[index] -= 1;
        }
        return new String(chars);
    }

    private int openLock(String target,String[] deadends){
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        int depth = 0;

        queue.offer("0000");
        for (String deadend : deadends) {
            visited.add(deadend);
        }

        while (!queue.isEmpty()) {
            int layerSize = queue.size();
            for (int i = 0; i < layerSize; i++) {
                String cur = queue.poll();
                if (target.equals(cur)){
                    return depth;
                }
                for (int j = 0; j < target.length(); j++) {
                    String plus = plusOne(cur,j);
                    if (!visited.contains(plus)) {
                        queue.offer(plus);
                        visited.add(plus);
                    }
                    String minus = minusOne(cur,j);
                    if (!visited.contains(minus)){
                        queue.offer(minus);
                        visited.add(minus);
                    }
                }
            }
            depth ++;
        }
        return -1;
    }
}
