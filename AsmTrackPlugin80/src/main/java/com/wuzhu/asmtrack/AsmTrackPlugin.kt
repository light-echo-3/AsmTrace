package com.wuzhu.asmtrack

import com.android.build.api.artifact.ScopedArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ScopedArtifacts
import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.wuzhu.asmtrack.task.AsmTraceTask
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * A simple 'hello world' plugin.
 */
@Suppress("unused")
class AsmTrackPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        Logger.make(project)
        project.extensions.create("asmTrackPluginConfig", AsmTrackPluginConfig::class.java)
        Logger.w("apply begin 12")

        val isApp = project.plugins.hasPlugin(AppPlugin::class.java)
        Logger.w("isApp=$isApp")

        // Only application module needs this plugin to generate register code
        if (isApp) {
            val android = project.extensions.getByType(AppExtension::class.java)
            val asmTrackPluginConfig = project.extensions.getByName("asmTrackPluginConfig") as AsmTrackPluginConfig
            Logger.w("asmTrackPluginConfig.toPluginConfig() = " + asmTrackPluginConfig.toPluginConfig())

            /**
             * todo
             * registerTransform api 已经弃用
             */
//            val transformImpl = ASMTransform(project)
//            android.registerTransform(transformImpl)
//            Logger.w("registerTransform")

            val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
            androidComponents.onVariants { variant ->
                Logger.e("variant.name=${variant.name},variant.buildType=${variant.buildType},variant.productFlavors=${variant.productFlavors},variant.flavorName=${variant.flavorName},variant=$variant, ")
                val taskProvider = project.tasks.register(//注册AsmTraceTask任务
                    "${variant.name}AsmTraceTask", AsmTraceTask::class.java
                )
                variant.artifacts.forScope(ScopedArtifacts.Scope.ALL) //扫描所有class
                    .use(taskProvider)
                    .toTransform(
                        type = ScopedArtifact.CLASSES,
                        inputJars = AsmTraceTask::allJars,
                        inputDirectories = AsmTraceTask::allDirectories,
                        into = AsmTraceTask::output
                    )
            }
        }

        // Register a task
        project.tasks.register("testPluginPublishSuccess") {
            it.doLast {
                Logger.w("AsmTrackPlugin publish success 2")
//                Logger.w("project.asmTrackPluginConfig.isTraceJar = " + project.extensions.getByName("asmTrackPluginConfig").isTraceJar)
            }
        }
    }
}
