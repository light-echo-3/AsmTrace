package com.wuzhu.asmtrack

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * A simple 'hello world' plugin.
 */
@SuppressWarnings("unused")
class AsmTrackPlugin implements Plugin<Project> {
    void apply(Project project) {
        Logger.make(project)
        Logger.w("apply begin 12")
        def isApp = project.plugins.hasPlugin(AppPlugin)
        //only application module needs this plugin to generate register code
        if (isApp) {
            def android = project.extensions.getByType(AppExtension)
            def transformImpl = new ASMTransform()
            //register this plugin
            android.registerTransform(transformImpl)
            Logger.w('registerTransform')
        }
        // Register a task
        project.tasks.register("testPluginPublishSuccess") {
            doLast {
                Logger.w("AsmTrackPlugin publish success 1")
            }
        }
    }
}
