package com.wuzhu.testandroid51.test;

import android.util.Log;

import com.wuzhu.libasmtrack.AsmTraceQueue;
import com.wuzhu.libasmtrack.NotTrack;

@NotTrack
public class TestAsmNotTrack {

    /**
     * 插桩前
     * MAXSTACK = 2
     *     MAXLOCALS = 2
     */
    private void test1() {
        int a = 111;
        System.out.println(a);
    }

    /**
     * 插桩后
     * MAXSTACK = 2
     *     MAXLOCALS = 3
     */
    private void test2() {
        String newName = AsmTraceQueue.beginTrace("test2__");
        int a = 111;
        System.out.println(a);
        AsmTraceQueue.endTrace(newName);
    }

}
