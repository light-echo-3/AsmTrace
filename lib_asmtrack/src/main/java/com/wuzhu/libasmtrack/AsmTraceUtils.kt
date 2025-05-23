package com.wuzhu.libasmtrack

import android.os.Build
import android.os.Looper
import android.os.Trace
import android.util.Log

@NotTrace
object AsmTraceUtils {
    private const val TAG = "AsmTraceStack"

    /**
     * 是否支持多线程，默认只支持主线程
     */
    var isSupportMultiThread = false

    @JvmStatic
    fun beginTrace(name: String?):String? {
        if (!isTrace) {
            return null
        }
        if (name == null) {
            Log.e(TAG, "beginTrace: name是空：$name")
            return null
        }
        traceBeginSection(name)
        return name
    }

    private fun traceBeginSection(name: String){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Log.e(TAG, "beginTrace: sdk版本太低，暂不支持trace，最低支持Android 4.3：$name")
        } else {
            Trace.beginSection(name)
        }
    }

    private fun traceEndSection(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Log.e(TAG, "beginTrace: sdk版本太低，暂不支持trace，最低支持Android 4.3")
        } else {
            Trace.endSection()
        }
    }

    @JvmStatic
    fun endTrace(name: String?) {
        if (!isTrace) {
            return
        }
        if (name == null) {
            Log.e(TAG, "endTrace:stack是空 或 name是空：$name")
            return
        }
        traceEndSection()
    }

    private val isTrace: Boolean
        get() = if (isMainThread) {
            true
        } else isSupportMultiThread
    private val isMainThread: Boolean
        get() = Looper.getMainLooper() == Looper.myLooper()


}