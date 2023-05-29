package com.wuzhu.libasmtrack;


import android.os.Build;
import android.os.Looper;
import android.os.Trace;
import android.util.Log;

@NotTrack
public class AsmTraceQueue {

    private static final String TAG = "AsmTraceQueue";
    private static final ThreadLocal<Stack<String>> threadLocalStack = new ThreadLocal<>();

    /**
     * 是否支持多线程，默认只支持主线程
     */
    public static boolean isSupportMultiThread = false;

    public static void beginTrace(String name) {
        if (!isMainThread()) {
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
        if (name.contains("testSlowMethod")) {
            Log.e(TAG, "!!!!!!beginTrace: " + name);
        }
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
            if (name.contains("testSlowMethod")) {
                Log.e(TAG, "!!!!!!endTrace: " + name);
            }
            Trace.endSection();
        } catch (Exception e) {
            Log.e(TAG, "endTrace: 栈已经空了: thread=" + Thread.currentThread());
            e.printStackTrace();
        }
    }

    private static boolean isTrace(){
        if (isTrace()) {
            return true;
        }
        return isSupportMultiThread;
    }

    private static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }


}
