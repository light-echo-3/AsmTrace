package com.wuzhu.libasmtrack;


import android.os.Build;
import android.os.Looper;
import android.os.Trace;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

@NotTrack
public class AsmTraceQueue {

    private static final String TAG = "AsmTraceQueue";
    private static final ThreadLocal<Stack<String>> threadLocalStack = new ThreadLocal<>();
    private static final List<String> logTags = new ArrayList<String>() {{
        add("Application#onCreate");
    }};

    /**
     * 是否支持多线程，默认只支持主线程
     */
    public static boolean isSupportMultiThread = false;

    public static void beginTrace(String name) {
        if (!isTrace()) {
            return;
        }
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
        printLogByTags("beginTrace", name);
        Trace.beginSection(name);
    }

    public static void endTrace(String name) {
        if (!isTrace()) {
            return;
        }
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
            printLogByTags("endTrace", name);
            Trace.endSection();
        } catch (Exception e) {
            Log.e(TAG, "endTrace: 栈已经空了: thread=" + Thread.currentThread());
            e.printStackTrace();
        }
    }

    private static boolean isTrace() {
        if (isMainThread()) {
            return true;
        }
        return isSupportMultiThread;
    }

    private static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    private static void printLogByTags(String prefix, String name) {
        if (logTags.isEmpty()) return;
        for (String tag : logTags) {
            if (tag != null && !tag.trim().isEmpty() && name.contains(tag)) {
                Log.e(TAG, "!!!!!!" + prefix + ":" + name);
            }
        }
    }

}
