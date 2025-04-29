package com.wuzhu.testandroid51;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.os.Trace;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wuzhu.testandroid51.aidl.Book;
import com.wuzhu.testandroid51.aidl.BookManagerService;
import com.wuzhu.testandroid51.aidl.IBookManager;
import com.wuzhu.testandroid51.aidl.IOnNewBookArrivedListener;
import com.wuzhu.testandroid51.dynamic.DLPlugin;
import com.wuzhu.testandroid51.kotlin.TestKClass;
import com.wuzhu.testandroid51.kotlin.TestObject;

import java.io.File;
import java.util.List;

public class MainActivity extends Activity implements DLPlugin {
    private static final String TAG = "hdq---MainActivity";
    private IBookManager bookManager;
    Handler handler = new Handler();
    Button button;
    File file = new File("test/test");
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(TAG, "onServiceConnected: binder---client = " + service + ",process = " + Process.myPid());
            bookManager = IBookManager.Stub.asInterface(service);
            try {
                List<Book> list = bookManager.getBookList();
                Log.i(TAG, "query book list,list type:" + list.getClass().getCanonicalName());
                Log.i(TAG, "query book list:" + list.toString());

                bookManager.registerListener(new IOnNewBookArrivedListener.Stub() {
                    @Override
                    public void onNewBookArrived(Book newBook) throws RemoteException {
                        Log.d(TAG, "4 client 被回调 onNewBookArrived: " + newBook);
                    }
                });
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.d(TAG, "onServiceDisconnected: ");
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Trace.beginSection("xxx MainActivity.onCreate");

        TestObject.test();
        new TestKClass().test();

        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate" + ",process = " + Process.myPid());
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        testBarrier(button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), SecondActivity.class));
            }
        });

        findViewById(R.id.bindservice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BookManagerService.class);
                boolean result = bindService(intent,mConnection,Context.BIND_AUTO_CREATE);
                Log.d(TAG, "bindservice: result = " + result);
            }
        });
        findViewById(R.id.addBook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookManager != null) {
                    try {
                        Log.d(TAG, "1 client addBook: result = ");
                        bookManager.addBook(new Book(123,"三体"));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Trace.endSection();

        new Thread(){
            @Override
            public void run() {
                super.run();
                System.out.println("test thread");
                try {
                    Thread.sleep(6*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }



    private void testBarrier(final Button button) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "handler.post:getWidth: " + button.getWidth());
            }
        });
        button.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "view.post:getWidth: " + button.getWidth());
            }
        });

    }

    @Override
    public void setProxy(Activity proxyActivity, String dexPath) {
//        GLUtils.
//        GLES20.glDrawArrays();

    }

    @Override
    public AssetManager getAssets() {
        return super.getAssets();
    }

    @Override
    public Resources getResources() {
        return super.getResources();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

    }

    @Override
    public void onRestart() {
        super.onRestart();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onResume() {
        super.onResume();
        final View view = findViewById(R.id.MyCanvasView);
        Log.d(TAG, "onResume " + view.getWidth() + "--" + view.getMeasuredWidth());
        view.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onResume post " + view.getWidth() + "--" + view.getMeasuredWidth());
            }
        });

        testBarrier(button);

    }



    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onWindowAttributesChanged(ViewGroup.LayoutParams params) {

    }
}