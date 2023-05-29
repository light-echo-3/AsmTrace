package com.wuzhu.testandroid51.test;


import com.wuzhu.libasmtrack.AsmTraceQueue;
import com.wuzhu.libasmtrack.NotTrack;

@NotTrack
public class TestAsmTraceQueue {

    public void testBegin(){
        String name = "testName333";
        AsmTraceQueue.beginTrace(name);
        int i = 123;
        int j = i + 1;
        AsmTraceQueue.endTrace(name);
    }

    public void testEnd(){

    }

}
