package com.wuzhu.asmtrack

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * A simple 'hello world' plugin.
 */
class AsmTrackPlugin implements Plugin<Project> {
    void apply(Project project) {
        println("-----======asm AsmTrackPlugin 1")

        def isApp = project.plugins.hasPlugin(AppPlugin)
        //only application module needs this plugin to generate register code
        if (isApp) {
            Logger.make(project)

            Logger.i('Project enable AsmTrackPlugin')

            def android = project.extensions.getByType(AppExtension)
            def transformImpl = new ASMTransform()

            //register this plugin
            android.registerTransform(transformImpl)
        }


        // Register a task
        project.tasks.register("greeting") {
            doLast {
                println("Hello from plugin 'test.plugin.asm.greeting'")
            }
        }
    }
}
