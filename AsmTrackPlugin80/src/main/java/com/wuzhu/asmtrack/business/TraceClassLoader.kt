package com.wuzhu.asmtrack.business

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project
import java.io.File
import java.net.MalformedURLException
import java.net.URL
import java.net.URLClassLoader

/**
 * Created by habbyge on 2019/4/24.
 */
object TraceClassLoader {
    @JvmStatic
    @Throws(MalformedURLException::class)
    fun getClassLoader(project: Project, inputFiles: Collection<File>): URLClassLoader {
        val urls = mutableListOf<URL>()
        val androidJar = getAndroidJar(project)
        if (androidJar != null) {
            urls.add(androidJar.toURI().toURL())
        }
        for (inputFile in inputFiles) {
            urls.add(inputFile.toURI().toURL())
        }

//        for (TransformInput inputs : Iterables.concat(invocation.getInputs(), invocation.getReferencedInputs())) {
//            for (DirectoryInput directoryInput : inputs.getDirectoryInputs()) {
//                if (directoryInput.getFile().isDirectory()) {
//                    urls.add(directoryInput.getFile().toURI().toURL());
//                }
//            }
//            for (JarInput jarInput : inputs.getJarInputs()) {
//                if (jarInput.getFile().isFile()) {
//                    urls.add(jarInput.getFile().toURI().toURL());
//                }
//            }
//        }
        val classLoaderUrls = urls.toTypedArray()
        return URLClassLoader(classLoaderUrls)
    }

    private fun getAndroidJar(project: Project): File? {
        var extension: BaseExtension? = null
        if (project.plugins.hasPlugin("com.android.application")) {
            extension = project.extensions.findByType(AppExtension::class.java)
        } else if (project.plugins.hasPlugin("com.android.library")) {
            extension = project.extensions.findByType(LibraryExtension::class.java)
        }
        if (extension == null) {
            return null
        }
        var sdkDirectory = extension.sdkDirectory.absolutePath
        val compileSdkVersion = extension.compileSdkVersion
        sdkDirectory = sdkDirectory + File.separator + "platforms" + File.separator
        val androidJarPath = sdkDirectory + compileSdkVersion + File.separator + "android.jar"
        val androidJar = File(androidJarPath)
        return if (androidJar.exists()) androidJar else null
    }
}