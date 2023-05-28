package com.wuzhu.libasmtrack;


import android.os.Build;
import android.os.Trace;

@NotTrack
public class AsmTrackQueue {

    private static final ThreadLocal<Stack<String>> threadLocalStack = new ThreadLocal<>();

    public static void beginSection(String name) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return;
        }
        if (name == null || name.trim().isEmpty()) {
            return;
        }
        Stack<String> stack = threadLocalStack.get();
        if (stack == null) {
            stack = new Stack<>();
            threadLocalStack.set(stack);
        }
        stack.push(name);
        Trace.beginSection(name);
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
