package com.example.testandroid51.test;

import android.util.Log;

import com.taobao.android.dexposed.DexposedBridge;
import com.taobao.android.dexposed.XC_MethodHook;

/**
 * @author Hdq on 2022/12/15.
 */
public class TestEpic {
    private static final String TAG = "epic";

    public static void hookConstructor(){
        try {
            DexposedBridge.hookAllConstructors(Thread.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Thread thread = (Thread) param.thisObject;
                    Class<?> clazz = thread.getClass();
                    if (clazz != Thread.class) {
                        Log.d(TAG, "found class extend Thread:" + clazz);
                        DexposedBridge.findAndHookMethod(clazz, "run", new SubThreadMethodHook());
                    }
                    Log.d(TAG, "-----Thread:" + thread.getName() + " class:" + thread.getClass() +  " is created.");
                }
            });
            DexposedBridge.findAndHookMethod(Thread.class, "run", new ThreadMethodHook());
        } catch (Error e) {
            e.printStackTrace();
        }
    }


    static class ThreadMethodHook extends XC_MethodHook{
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            super.beforeHookedMethod(param);
            Thread t = (Thread) param.thisObject;
            Log.i(TAG, "-----Thread:" + t + ", started..");
        }

        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            super.afterHookedMethod(param);
            Thread t = (Thread) param.thisObject;
            Log.i(TAG, "-----Thread:" + t + ", exit..");
        }
    }
    static class SubThreadMethodHook extends XC_MethodHook{
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            super.beforeHookedMethod(param);
            Thread t = (Thread) param.thisObject;
            Log.i(TAG, "-----Thread sub:" + t + ", started..");
        }

        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            super.afterHookedMethod(param);
            Thread t = (Thread) param.thisObject;
            Log.i(TAG, "-----Thread sub:" + t + ", exit..");
        }
    }


}
