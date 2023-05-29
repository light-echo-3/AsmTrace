package com.wuzhu.libasmtrack

import android.os.Build
import android.os.Looper
import android.os.Trace
import android.util.Log

@NotTrack
object AsmTraceQueue {
    private const val TAG = "AsmTraceQueue"
    private val threadLocalStack = ThreadLocal<Stack<String>>()
    private val logTags: List<String> = mutableListOf("Application#onCreate")

    /**
     * 是否支持多线程，默认只支持主线程
     */
    var isSupportMultiThread = false

    @JvmStatic
    fun beginTrace(name: String?) {
        if (!isTrace) {
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Log.e(TAG, "beginTrace: sdk版本太低：$name")
            return
        }
        if (name == null || name.trim().isEmpty()) {
            Log.e(TAG, "beginTrace: name是空：$name")
            return
        }
        var stack = threadLocalStack.get()
        if (stack == null) {
            stack = Stack()
            threadLocalStack.set(stack)
        }
        stack.push(name)
        printLogByTags("beginTrace", name)
        Trace.beginSection(name)
    }

    @JvmStatic
    fun endTrace(name: String?) {
        if (!isTrace) {
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Log.e(TAG, "endTrace: sdk版本太低：$name")
            return
        }
        val stack = threadLocalStack.get()
        if (stack == null || name == null || name.trim().isEmpty()) {
            Log.e(TAG, "endTrace:stack是空 或 name是空：$name")
            return
        }
        try {
            var popName: String
            while (name != stack.pop().also { popName = it }) {
                Log.e(TAG, "endTrace: 1 = $popName")
                Trace.endSection()
            }
            printLogByTags("endTrace", name)
            Trace.endSection()
        } catch (e: Exception) {
            Log.e(TAG, "endTrace: 栈已经空了: thread=" + Thread.currentThread())
            e.printStackTrace()
        }
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
}