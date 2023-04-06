package com.example.testandroid51.utils;

import android.os.Trace;

/**
 * @author Hdq on 2022/12/6.
 */
public class TraceUtils {

    public static void begin(){
        String name = "class#method";
        Trace.beginSection(name);
        System.out.println("-----");
        Trace.endSection();
    }

    public static void end(){
        Trace.endSection();
    }

}
