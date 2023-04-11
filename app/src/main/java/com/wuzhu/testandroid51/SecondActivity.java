package com.wuzhu.testandroid51;

import android.app.Activity;
import android.os.Bundle;
import android.os.Trace;
import android.util.Log;


/**
 * @author Hdq on 2022/8/1.
 */
public class SecondActivity extends Activity {
    private static final String TAG = "---SecondActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Trace.beginSection("SecondActivity.onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Log.d(TAG, "onCreate");
        MyApplication.activities.add(this);
        Trace.endSection();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}