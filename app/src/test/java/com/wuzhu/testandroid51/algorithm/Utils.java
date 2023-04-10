package com.wuzhu.testandroid51.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Hdq on 2022/12/2.
 */
public class Utils {



    public static  void printIndent(int indentCount){
        for (int i = 0; i < indentCount; i++) {
            System.out.print(" - ");
        }
//        System.out.println("indentCount = " + indentCount);
    }

    public static  void println(int[] array){
        StringBuffer buffer = new StringBuffer("[");
        for (int t : array) {
            buffer.append(t).append(",");
        }
        System.out.println(buffer.deleteCharAt(buffer.length() - 1).append("]").toString());
    }

    public static <T> void println(T[] array){
        println(Arrays.asList(array));
    }

    public static <T> void println(List<T> list){
        StringBuffer buffer = new StringBuffer();
        for (T t : list) {
            buffer.append(t).append(",");
        }
        System.out.println(buffer.deleteCharAt(buffer.length() - 1).toString());
    }
}
