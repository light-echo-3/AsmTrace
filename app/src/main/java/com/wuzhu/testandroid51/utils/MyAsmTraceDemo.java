package com.wuzhu.testandroid51.utils;

import android.os.Trace;

/**
 * @author Hdq on 2022/12/6.
 *
 */
public class MyAsmTraceDemo {

    public static void begin(){
        String name = "class#method";
        Trace.beginSection(name);
    }

    public static void end(){
        Trace.endSection();
    }

}
