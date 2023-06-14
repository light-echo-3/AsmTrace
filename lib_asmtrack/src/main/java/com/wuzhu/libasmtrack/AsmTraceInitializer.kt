package com.wuzhu.libasmtrack

@NotTrack
object AsmTraceInitializer {

    @JvmStatic
    fun init() {
        Thread.setDefaultUncaughtExceptionHandler(AsmTraceUncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler()))
    }

}