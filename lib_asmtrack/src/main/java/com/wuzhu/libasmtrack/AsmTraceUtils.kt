package com.wuzhu.libasmtrack

import android.os.Build
import android.os.Looper
import android.os.Trace
import android.util.Log
import androidx.annotation.Keep

@NotTrace
@Keep
object AsmTraceUtils {
    private const val TAG = "AsmTraceStack"
    private val logTags: List<String> = mutableListOf("Application#onCreate")

    private var count = 0

    /**
     * 是否支持多线程，默认只支持主线程
     */
    var isSupportMultiThread = false

    @JvmStatic
    fun beginTrace(name: String?):String? {
        if (!isTrace) {
            return null
        }
        if (name == null || name.trim().isEmpty()) {
            Log.e(TAG, "beginTrace: name是空：$name")
            return null
        }
        val newName = "${name}_${genCount()}"
        printLogByTags("beginTrace", newName)
        traceBeginSection(newName)
        return newName
    }

    private fun traceBeginSection(name: String){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Log.e(TAG, "beginTrace: sdk版本太低：$name")
        } else {
            val newName = if (name.length > 127) {
                Log.e(TAG, "name length超长,name=$name")
                val length = name.length
                name.substring(length - 127,length)
            } else name
            Trace.beginSection(newName)
        }
    }

    private fun traceEndSection(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Log.e(TAG, "beginTrace: sdk版本太低：")
        } else {
            Trace.endSection()
        }
    }

    @JvmStatic
    fun endTrace(name: String?) {
        if (!isTrace) {
            return
        }
        if (name == null || name.trim().isEmpty()) {
            Log.e(TAG, "endTrace:stack是空 或 name是空：$name")
            return
        }
        printLogByTags("endTrace", name)
        traceEndSection()
    }

    private val isTrace: Boolean
        get() = if (isMainThread) {
            true
        } else isSupportMultiThread
    private val isMainThread: Boolean
        get() = Looper.getMainLooper() == Looper.myLooper()

    private fun printLogByTags(prefix: String, name: String) {
        if (logTags.isEmpty()) return
        for (tag in logTags) {
            if (tag.trim().isNotEmpty() && name.contains(tag)) {
                Log.e(TAG, "!!!!!!$prefix:$name")
            }
        }
    }

    private fun genCount():Int {
        count ++
        if (count > 999_999) {
            count = 1//重新计数
        }
        return count
    }

}