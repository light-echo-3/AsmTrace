package com.wuzhu.asmtrack

/**
 * 为TraceMan自定义的配置项extension
 */
class AsmTrackPluginConfig {
    boolean open = true
    boolean isTraceJar = true

    AsmTrackPluginConfig() {
    }

    PluginConfig toPluginConfig() {
        return new PluginConfig(open, isTraceJar)
    }

}