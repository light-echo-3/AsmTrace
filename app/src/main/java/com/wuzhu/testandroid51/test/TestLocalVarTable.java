package com.wuzhu.testandroid51.test;

import com.wuzhu.libasmtrack.NotTrack;

@NotTrack
public class TestLocalVarTable {

    public int add(){
        int a = 11;
        int b = 22;
        int c = a + b;
        return c;
    }

}
