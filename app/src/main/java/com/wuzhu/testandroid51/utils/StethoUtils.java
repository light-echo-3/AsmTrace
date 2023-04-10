package com.wuzhu.testandroid51.utils;

import android.content.Context;

import com.wuzhu.testandroid51.MyApplication;

import java.lang.reflect.Method;


/**
 * Created by wanghb on 17/8/11.
 */

public class StethoUtils {

    public static void init(Context context) {
        if (isSkip()) {
            return;
        }
        try {
            Class<?> stethoClass = Class.forName("com.facebook.stetho.Stetho");
            Method initializeWithDefaults = stethoClass.getMethod("initializeWithDefaults",
                    Context.class);
            initializeWithDefaults.invoke(null, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static OkHttpClient.Builder configureInterceptor(OkHttpClient.Builder builder) {
//        if (isSkip()) {
//            return builder;
//        }
//        try {
//            Class<?> aClass = Class.forName("com.facebook.stetho.okhttp3.StethoInterceptor");
//            return builder.addNetworkInterceptor((Interceptor) aClass.newInstance());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return builder;
//    }

    private static boolean isSkip() {
        return !MyApplication.isDebugBuild();
    }
}
