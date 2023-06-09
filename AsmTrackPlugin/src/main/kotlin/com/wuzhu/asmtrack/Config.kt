package com.wuzhu.asmtrack

/**
 * Create by cxzheng on 2019/6/4
 */
class Config {

    //一些默认无需插桩的类
    private val UNNEED_TRACE_CLASS = arrayOf("R.class", "R$", "Manifest", "BuildConfig")

    /**
     * 不需要插桩的包
     * 配置格式：例：package = kotlin/jvm/internal/
     */
    private val UNNEED_TRACE_PACKAGE = arrayOf("kotlin/")


    /**
     * @param fileName 格式：
     * 例：fileName = kotlin/jvm/internal/Intrinsics.class
     */
    fun isNeedTraceClass(fileName: String): Boolean {
        var isNeed = true
        if (fileName.endsWith(".class")) {
            for (unTraceCls in UNNEED_TRACE_CLASS) {
                if (fileName.contains(unTraceCls)) {
                    isNeed = false
                    break
                }
            }
            for (packageName in UNNEED_TRACE_PACKAGE) {
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


