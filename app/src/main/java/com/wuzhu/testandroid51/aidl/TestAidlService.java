package com.wuzhu.testandroid51.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * @author Hdq on 2022/8/3.
 */
public class TestAidlService extends Service {
    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        TestAidlService getService() {
            // Return this instance of LocalService so clients can call public methods
            return TestAidlService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
