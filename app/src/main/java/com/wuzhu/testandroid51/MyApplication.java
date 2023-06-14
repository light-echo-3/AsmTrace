package com.wuzhu.testandroid51;

import android.app.Activity;
import android.app.Application;
import android.os.Trace;
import android.util.Log;

import com.wuzhu.libasmtrack.AsmTraceInitializer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hdq on 2022/11/21.
 */
public class MyApplication extends Application {
    public static List<Activity> activities = new ArrayList<>();

    @Override
    public void onCreate() {
        Trace.beginSection("手动加的---MyApplication.onCreate");
        super.onCreate();

        if (isDebugBuild()) {
            Log.d("TAG", "onCreate: ");
        }
        AsmTraceInitializer.init();
        Trace.endSection();
    }

    public static boolean isDebugBuild() { //debug
        return "debug".equals(BuildConfig.BUILD_TYPE);
    }
}
