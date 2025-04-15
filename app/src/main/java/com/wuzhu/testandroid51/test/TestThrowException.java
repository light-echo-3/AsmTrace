package com.wuzhu.testandroid51.test;

import android.util.Log;

import com.wuzhu.libasmtrack.NotTrace;

@NotTrace
public class TestThrowException {

    public int testThrowException(int a, int b) throws Exception {
        if (a > 100) {
            throw new Exception("test throw");
        }
        return a + b;
    }


    private int testTryFinally() {
        try {
            test();
            return 1;
        } finally {
            Log.d("TAG---1", "testTryFinally: finally---1");
        }
    }

    private void testTryCatchFinally() {
        try {
            test();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            Log.d("TAG---2", "testTryFinally: finally---2");
        }
    }

    private void testThrow() {
        test();
    }

    private void test() throws RuntimeException {
        Log.d("TAG", "test: test");
    }

}
