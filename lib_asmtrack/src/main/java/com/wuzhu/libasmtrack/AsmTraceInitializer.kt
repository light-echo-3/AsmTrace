package com.wuzhu.libasmtrack

@NotTrace
object AsmTraceInitializer {

    @JvmStatic
    fun init() {
        Thread.setDefaultUncaughtExceptionHandler(AsmTraceUncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler()))
    }

}