package com.wuzhu.asmtrack

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
class AsmTrackPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        Logger.make(project)
        project.extensions.create("asmTrackPluginConfig", AsmTrackPluginConfig::class.java)
        Logger.w("----apply begin 12")

        val isApp = project.plugins.hasPlugin(AppPlugin::class.java)
        // only application module needs this plugin to generate register code
        if (isApp) {
            val android = project.extensions.getByType(AppExtension::class.java)
            val asmTrackPluginConfig = project.extensions.getByName("asmTrackPluginConfig") as AsmTrackPluginConfig
            Logger.w("asmTrackPluginConfig.toPluginConfig() = ${asmTrackPluginConfig.toPluginConfig()}")

            /**
             * todo support gradle-7.2
             * registerTransform api 已经弃用
             */
//            val transformImpl = ASMTransform(project)
//            android.registerTransform(transformImpl)
            Logger.w("registerTransform")
        }

        // Register a task
        project.tasks.register("testPluginPublishSuccess") {
            it.doLast {
                Logger.w("AsmTrackPlugin publish success 2")
                val config = project.extensions.getByName("asmTrackPluginConfig") as AsmTrackPluginConfig
                Logger.w("project.asmTrackPluginConfig.isTraceJar = ${config.traceJar}")
            }
        }
    }
}
