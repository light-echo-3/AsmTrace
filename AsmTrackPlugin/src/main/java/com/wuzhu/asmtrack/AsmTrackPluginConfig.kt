package com.wuzhu.asmtrack

/**
 * Custom configuration extension for TraceMan
 */
open class AsmTrackPluginConfig {
    open var open: Boolean = true
    open var traceJar: Boolean = true

    // Default constructor is optional in Kotlin as it is implied
    fun toPluginConfig(): PluginConfig {
        return PluginConfig(open, traceJar)
    }
}
