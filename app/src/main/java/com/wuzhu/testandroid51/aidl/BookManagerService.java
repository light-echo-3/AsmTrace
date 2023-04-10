package com.wuzhu.testandroid51.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Hdq on 2022/8/3.
 */
public class BookManagerService extends Service {
    private static final String TAG = "hdq---BMS";
    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<Book>();
    private Binder mBinder = new IBookManager.Stub() {
        private IOnNewBookArrivedListener listener;
        @Override
        public List<Book> getBookList(){
            return mBookList;
        }

        @Override
        public void addBook(Book book){
            mBookList.add(book);
            Log.d(TAG, "2 service addBook: " + book);
            if (listener != null) {
                try {
                    Log.d(TAG, "3 service 回调 onNewBookArrived: " + book);
                    listener.onNewBookArrived(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            this.listener = listener;
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            this.listener = null;
        }


    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        mBookList.add(new Book(1, "Android"));
        mBookList.add(new Book(2, "Ios"));
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: " + ",process = " + Process.myPid()  + ",binder---service = " + mBinder );
        return mBinder;
    }
}