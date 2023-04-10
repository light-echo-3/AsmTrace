package com.wuzhu.testandroid51.models;

import android.os.Debug;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hdq on 2022/10/23.
 */
public class Fruit {
    public int i;


    public static String method(List<String> list) {
        System.out.println("invoke method(List<String> list)");
        return "";
    }

    public static int method1(List<Integer> list0) {
        System.out.println("invoke method(List<Integer> list)");
        return 1;
    }

    public static void main(String[] args) {
        method(new ArrayList<String>());

        Debug.startMethodTracing();
    }

}
