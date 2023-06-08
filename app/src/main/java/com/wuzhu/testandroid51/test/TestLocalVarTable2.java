package com.wuzhu.testandroid51.test;

import com.wuzhu.libasmtrack.NotTrack;

@NotTrack
public class TestLocalVarTable2 {

    public int add(){
        String s1 = getString();
        int a = 11;
        int b = 22;
        int c = a + b;
        print(s1);
        return c;
    }

    private void print(String s1) {
        System.out.println(s1);
    }

    private String getString() {
        return "testStr";
    }

}
