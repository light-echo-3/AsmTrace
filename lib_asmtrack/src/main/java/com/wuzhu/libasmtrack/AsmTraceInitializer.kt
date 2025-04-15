package com.wuzhu.libasmtrack

@Deprecated("改用try catch方案")
@NotTrace
object AsmTraceInitializer {

    @JvmStatic
    fun init() {
        Thread.setDefaultUncaughtExceptionHandler(AsmTraceUncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler()))
    }

}