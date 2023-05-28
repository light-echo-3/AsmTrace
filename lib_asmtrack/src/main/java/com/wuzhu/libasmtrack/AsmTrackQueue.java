package com.wuzhu.libasmtrack;


import android.os.Build;
import android.os.Trace;

@NotTrack
public class AsmTrackQueue {

    private static final ThreadLocal<Stack<String>> threadLocalStack = new ThreadLocal<>();
    private static final ThreadLocal<Integer> threadLocalNum = new ThreadLocal<>();

    public static String beginSection(String name) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return null;
        }
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        Stack<String> stack = threadLocalStack.get();
        if (stack == null) {
            stack = new Stack<>();
            threadLocalStack.set(stack);
        }
        Integer i = threadLocalNum.get();
        if (i == null) {
            i = 0;
            threadLocalNum.set(i);
        }
        name += generateNum(i);
        stack.push(name);
        Trace.beginSection(name);
        return name;
    }

    private static int generateNum(Integer i) {
        i++;
        if (i >= 999999) {
            i = 1;
        }
        return i;
    }


    public static void endSection(String name) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return;
        }
        Stack<String> stack = threadLocalStack.get();
        if (stack == null || name == null || name.trim().isEmpty()) {
            return;
        }
        while (!name.equals(stack.pop())) {
            Trace.endSection();
        }
        Trace.endSection();
    }


}
