package com.wuzhu.asmtrack

/**
 * Create by cxzheng on 2019/6/4
 */
class Config {

    companion object {
        //一些默认无需插桩的类
        private val UN_NEED_TRACE_CLASS = arrayOf(
            "/R.class",
            "/R$",
            "/Manifest.class",
            "/BuildConfig.class",
            "androidx/core/os/TraceCompat.class",
            "androidx/core/os/TraceCompat\$Api18Impl.class",
            "androidx/core/os/TraceCompat\$Api29Impl.class",
            "androidx/tracing/Trace.class",
            "androidx/tracing/TraceApi18Impl.class",
            "androidx/tracing/TraceApi29Impl.class",
        )

        /**
         * 不需要插桩的包
         * 配置格式：例：package = kotlin/jvm/internal/
         */
        private val UN_NEED_TRACE_PACKAGE = arrayOf("kotlin/","kotlinx/")
    }



    /**
     * @param fileName 格式：
     * 例：fileName = kotlin/jvm/internal/Intrinsics.class
     */
    fun isNeedTraceClass(fileName: String): Boolean {
        var isNeed = true
        if (fileName.endsWith(".class")) {
            for (unTraceCls in UN_NEED_TRACE_CLASS) {
                if (fileName.contains(unTraceCls)) {
                    isNeed = false
                    break
                }
            }
            for (packageName in UN_NEED_TRACE_PACKAGE) {
                if (fileName.startsWith(packageName)) {
                    isNeed = false
                    break
                }
            }
        } else {
            isNeed = false
        }
        return isNeed
    }

}


