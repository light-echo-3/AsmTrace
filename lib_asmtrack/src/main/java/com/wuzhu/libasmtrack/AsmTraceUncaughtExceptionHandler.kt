package com.wuzhu.libasmtrack

/***
 * 异常处理
 * 发生uncaughtException时，清空AsmTraceStack
 */
@Deprecated("改用try catch方案")
@NotTrace
class AsmTraceUncaughtExceptionHandler(private val previousHandler: Thread.UncaughtExceptionHandler?) :
        Thread.UncaughtExceptionHandler {

    override fun uncaughtException(t: Thread, e: Throwable) {
        AsmTraceStack.clear()
        previousHandler?.uncaughtException(t, e)
    }


}