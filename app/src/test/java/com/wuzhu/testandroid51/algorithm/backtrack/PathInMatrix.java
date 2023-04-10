package com.wuzhu.testandroid51.algorithm.backtrack;

import org.junit.Test;

import java.util.Arrays;

/**
 * @author Hdq on 2022/12/10.
 */
public class PathInMatrix {

    @Test
    public void test(){
        char[][] matrix = {
                {'a','b','t','g'},
                {'c','f','c','s'},
                {'j','d','e','h'}};//ABTG CFCS JDEH
        boolean hasFullPath = hasFullPath(matrix,"bfce");
        System.out.println("结果：" + hasFullPath);
    }

    private int length;
    private boolean[][] visited;

    public boolean hasFullPath(char[][] matrix,String target){
        visited = new boolean[matrix.length][matrix[0].length];
        for (int row = 0; row < matrix.length; row++) {
            for (int column = 0; column < matrix[row].length; column++) {
                if (hasPath(matrix,row,column,target)){
                    return true;
                }
            }
        }
        return false;
    }



    private boolean hasPath(char[][] matrix, int row, int column, String target) {
        if (row >= matrix.length || column >= matrix[0].length){
            return false;
        }
        if (length == target.length()){//全找到了
            return true;
        }
        //判断当前节点
        char cur = matrix[row][column];
        if (cur != target.charAt(length) || visited[row][column]) {//不用找子节点了
            return false;
        }
        printIndent(length);
        System.out.println("cur = " + cur + ", row = " + row + ", column = " + column + ", target = " + target);

        length++;
        visited[row][column] = true;
        boolean childHasPath = hasPath(matrix,row+1,column,target) ||
                hasPath(matrix,row-1,column,target) ||
                hasPath(matrix,row,column+1,target) ||
                hasPath(matrix,row,column-1,target);
//        if (!childHasPath){//不用这种判断
//            visited[row][column] = false;
//        }
        visited[row][column] = false;
        length--;
        return childHasPath;
    }

    private void printIndent(int length) {
        for (int i = 0; i < length; i++) {
            System.out.printf(" - ");
        }
    }

}
