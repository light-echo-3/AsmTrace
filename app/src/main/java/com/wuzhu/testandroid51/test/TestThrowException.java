package com.wuzhu.testandroid51.test;

import com.wuzhu.libasmtrack.NotTrace;

@NotTrace
public class TestThrowException {

    public int testThrowException(int a, int b) throws Exception {
        if (a > 100) {
            throw new Exception("test throw");
        }
        return a + b;
    }
}
