package com.wuzhu.testandroid51.utils;

import android.os.Trace;

import com.wuzhu.libasmtrack.NotTrace;

/**
 * @author Hdq on 2022/12/6.
 *
 */
@NotTrace
public class MyAsmTraceDemo {

    public static void begin(){
        Trace.beginSection("class#method");
    }

    public static void end(){
        Trace.endSection();
    }

}
