package com.wuzhu.testandroid51.test;


import com.wuzhu.libasmtrack.NotTrack;

@NotTrack
public class TestAsm2 {

    public void testAddTryCatch(){
        System.out.println("------");
    }

    public void testAddTryCatch2(){
        try {
            System.out.println("------");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
