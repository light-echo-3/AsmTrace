package com.example.asmtrack

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin;
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * A simple 'hello world' plugin.
 */
class HelloPlugin implements Plugin<Project> {
    void apply(Project project) {
        println("-----======asm HelloPlugin2")

        def isApp = project.plugins.hasPlugin(AppPlugin)
        //only application module needs this plugin to generate register code
        if (isApp) {
            Logger.make(project)

            Logger.i('Project enable arouter-register plugin')

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
