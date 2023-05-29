package com.wuzhu.libasmtrack;


import android.os.Build;
import android.os.Trace;
import android.util.Log;

@NotTrack
public class AsmTrackQueue {

    private static final String TAG = "AsmTrackQueue";
    private static final ThreadLocal<Stack<String>> threadLocalStack = new ThreadLocal<>();

    public static void beginTrace(String name) {
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
        Log.e(TAG, "beginSection: " + name);
        Trace.beginSection(name);
    }

    public static void endTrace(String name) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return;
        }
        Stack<String> stack = threadLocalStack.get();
        if (stack == null || name == null || name.trim().isEmpty()) {
            return;
        }
        String popName;
        while (!name.equals(popName = stack.pop())) {
            Log.e(TAG, "endSection: 1 = " + popName);
            Trace.endSection();
        }
        Log.e(TAG, "endSection: 2 = " + popName);
        Trace.endSection();
    }


}
