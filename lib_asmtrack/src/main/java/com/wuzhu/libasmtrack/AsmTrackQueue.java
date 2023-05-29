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
            Log.e(TAG, "beginTrace: sdk版本太低：" + name);
            return;
        }
        if (name == null || name.trim().isEmpty()) {
            Log.e(TAG, "beginTrace: name是空：" + name);
            return;
        }
        Stack<String> stack = threadLocalStack.get();
        if (stack == null) {
            stack = new Stack<>();
            threadLocalStack.set(stack);
        }
        stack.push(name);
//        Log.e(TAG, "beginTrace: " + name);
        Trace.beginSection(name);
    }

    public static void endTrace(String name) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Log.e(TAG, "endTrace: sdk版本太低：" + name);
            return;
        }
        Stack<String> stack = threadLocalStack.get();
        if (stack == null || name == null || name.trim().isEmpty()) {
            Log.e(TAG, "endTrace:stack是空 或 name是空：" + name);
            return;
        }
        try {
            String popName;
            while (!name.equals(popName = stack.pop())) {
                Log.e(TAG, "endTrace: 1 = " + popName);
                Trace.endSection();
            }
//        Log.e(TAG, "endTrace: 2 = " + popName);
            Trace.endSection();
        } catch (Exception e) {
            Log.e(TAG, "endTrace: 栈已经空了: thread=" + Thread.currentThread());
            e.printStackTrace();
        }
    }


}
