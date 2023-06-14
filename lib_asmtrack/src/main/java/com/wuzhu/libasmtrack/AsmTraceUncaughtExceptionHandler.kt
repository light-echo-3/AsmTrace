package com.wuzhu.libasmtrack

/***
 * 异常处理
 * 发生uncaughtException时，清空AsmTraceQueue
 */
@NotTrace
class AsmTraceUncaughtExceptionHandler(private val previousHandler: Thread.UncaughtExceptionHandler?) :
        Thread.UncaughtExceptionHandler {

    override fun uncaughtException(t: Thread, e: Throwable) {
        AsmTraceQueue.clear()
        previousHandler?.uncaughtException(t, e)
    }


}