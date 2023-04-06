package com.example.testandroid51;

import android.app.Activity;
import android.app.Application;
import android.os.Trace;

import com.example.testandroid51.utils.StethoUtils;
import com.squareup.leakcanary.LeakCanary;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hdq on 2022/11/21.
 */
public class MyApplication extends Application {
    public static List<Activity> activities = new ArrayList<>();
    @Override
    public void onCreate() {
        Trace.beginSection("MyApplication.onCreate");
        super.onCreate();
        // LeakCanary内存泄露监测
        LeakCanary.install(this);

        if (isDebugBuild()) {
            StethoUtils.init(this);
        }
        Trace.endSection();
    }

    public static boolean isDebugBuild() { //debug
        return "debug".equals(BuildConfig.BUILD_TYPE);
    }
}
