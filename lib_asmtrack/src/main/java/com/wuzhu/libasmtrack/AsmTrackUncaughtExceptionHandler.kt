package com.wuzhu.libasmtrack

/***
 */
class AsmTrackUncaughtExceptionHandler(private val previousHandler: Thread.UncaughtExceptionHandler?) :
        Thread.UncaughtExceptionHandler {

    override fun uncaughtException(t: Thread, e: Throwable) {
        AsmTraceQueue.clear()
        previousHandler?.uncaughtException(t, e)
    }


}