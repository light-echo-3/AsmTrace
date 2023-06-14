package com.wuzhu.libasmtrack

import android.os.Build
import android.os.Looper
import android.os.Trace
import android.util.Log

@NotTrace
object AsmTraceQueue {
    private const val TAG = "AsmTraceQueue"
    private val threadLocalStack = ThreadLocal<Stack<String>>()
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
        var stack = threadLocalStack.get()
        if (stack == null) {
            stack = Stack()
            threadLocalStack.set(stack)
        }
        val newName = "${name}_${genCount()}"
        stack.push(newName)
        printLogByTags("beginTrace", newName)
        traceBeginSection(newName)
        return newName
    }

    private fun traceBeginSection(name: String){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Log.e(TAG, "beginTrace: sdk版本太低：$name")
        } else {
            Trace.beginSection(name)
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
        val stack = threadLocalStack.get()
        if (stack == null || name == null || name.trim().isEmpty()) {
            Log.e(TAG, "endTrace:stack是空 或 name是空：$name")
            return
        }
        try {
            var popName: String
            while (name != stack.pop().also { popName = it }) {
                Log.e(TAG, "endTrace: 1 = $popName")
                traceEndSection()
            }
            printLogByTags("endTrace", name)
            traceEndSection()
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

    private fun genCount():Int {
        count ++
        if (count > 999_999) {
            count = 1//重新计数
        }
        return count
    }

    fun clear(){
        try {
            val stack = threadLocalStack.get() ?: return
            while (!stack.isEmpty) {
                stack.pop()
                traceEndSection()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}