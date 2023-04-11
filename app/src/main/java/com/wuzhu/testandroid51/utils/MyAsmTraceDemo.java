package com.wuzhu.testandroid51.utils;

import android.os.Trace;

import com.wuzhu.libasmtrack.NotTrack;

/**
 * @author Hdq on 2022/12/6.
 *
 */
@NotTrack
public class MyAsmTraceDemo {

    public static void begin(){
        Trace.beginSection("class#method");
    }

    public static void end(){
        Trace.endSection();
    }

}
