package com.wuzhu.testandroid51.test;


import com.wuzhu.libasmtrack.AsmTrackQueue;
import com.wuzhu.libasmtrack.NotTrack;

@NotTrack
public class TestAsmTrackQueue {

    public void testBegin(){
        String name = "testName333";
        String genName = AsmTrackQueue.beginSection(name);
        int i = 123;
        int j = i + 1;
        AsmTrackQueue.endSection(genName);
    }

    public void testEnd(){

    }

}
