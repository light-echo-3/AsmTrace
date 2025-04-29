package com.wuzhu.asmtrack

import org.gradle.api.Project
import org.gradle.api.logging.Logger

object Logger {
    lateinit var logger: Logger

    @JvmStatic
    fun make(project: Project) {
        logger = project.logger
    }

    @JvmStatic
    @Suppress("unused")
    fun i(info: String) {
        logger.info("AsmTrackPlugin:: i >>> --- $info")
    }

    @JvmStatic
    fun lifecycle(warning: String) {
        logger.lifecycle("AsmTrackPlugin:: w >>> ------ $warning")
    }

    @JvmStatic
    fun w(warning: String) {
        logger.warn("AsmTrackPlugin:: w >>> ------ $warning")
    }

    @JvmStatic
    @Suppress("unused")
    fun e(error: String) {
        logger.error("AsmTrackPlugin:: e >>> --------- $error")
    }

}