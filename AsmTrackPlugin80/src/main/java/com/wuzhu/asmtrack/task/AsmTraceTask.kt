package com.wuzhu.asmtrack.task

import com.wuzhu.asmtrack.Config
import com.wuzhu.asmtrack.Logger
import com.wuzhu.asmtrack.business.ClassHandler
import com.wuzhu.asmtrack.business.TraceClassLoader
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

/**
 * Created by znh on 10/09/2023
 * <p>
 * Description: HuiRouterTask
 */
abstract class AsmTraceTask : DefaultTask() {

    //所有的jar文件输入信息
    @get:InputFiles
    abstract val allJars: ListProperty<RegularFile>

    //所有的class文件输入信息
    @get:InputFiles
    abstract val allDirectories: ListProperty<Directory>

    //经过插桩修改后的输出信息
    @get:OutputFile
    abstract val output: RegularFileProperty

    //注册带有HuiRouterPath注解的类
    private val annotationPathMap = HashMap<String?, String?>()

    //HuiRouterApi的class对应Jar包文件
    private var routerApiJarFile: File? = null

    @TaskAction
    fun taskAction() {

        Logger.e("AsmTraceTask-taskAction")

        val classLoader = createClassLoader(project,allDirectories,allJars)

        //输出到output的流
        val jarOutput = JarOutputStream(
            BufferedOutputStream(FileOutputStream(output.get().asFile))
        )
        val config = Config()

        //遍历扫描class
        allDirectories.get().forEach { directory ->
            directory.asFile.walk().forEach { file ->
                if (file.isFile) {
                    if (file.absolutePath.endsWith(".class")) {
                        ClassHandler.handleClassInDirectory(classLoader,file, config)
                    }
                    val relativePath = directory.asFile.toURI().relativize(file.toURI()).path
                    //1.putNextEntry
                    jarOutput.putNextEntry(JarEntry(relativePath.replace(File.separatorChar, '/')))
                    //2.写入字节
                    jarOutput.write(file.inputStream().readBytes())
                    //3.关闭
                    jarOutput.closeEntry()
                }
            }
        }

        //遍历扫描jar
        allJars.get().forEach { jarInputFile ->
            val jarFile = JarFile(jarInputFile.asFile)
            jarFile.entries().iterator().forEach { jarEntry ->
                //过滤掉非class文件，并去除重复无效的META-INF文件
                if (jarEntry.name.endsWith(".class") && !jarEntry.name.contains("META-INF")) {
                    val outByteArray = ClassHandler.handleClassInJar(classLoader,jarFile,jarEntry,config)
                    //1.putNextEntry
                    jarOutput.putNextEntry(JarEntry(jarEntry.name))
                    //2.写入字节
                    jarOutput.write(outByteArray)
                    //3.关闭
                    jarOutput.closeEntry()
                } else {
                    // META-INF 不用写入，否则会duplicate error
//                    //1.putNextEntry
//                    jarOutput.putNextEntry(JarEntry(jarEntry.name))
//                    //2.写入字节
//                    jarOutput.write(jarFile.getInputStream(jarEntry).readBytes())
//                    //3.关闭
//                    jarOutput.closeEntry()
                }
            }
            jarFile.close()
        }

        //关闭输出流
        jarOutput.close()
    }


    private fun createClassLoader(project: Project, allDirectories: ListProperty<Directory>, allJars: ListProperty<RegularFile>): ClassLoader {
        val inputFiles = mutableListOf<File>()
        //1.所有类文件夹
        allDirectories.get().forEach { directoryInput ->
            inputFiles.add(directoryInput.asFile)
        }
        //2.所有jar
        allJars.get().forEach { jarInput ->
            inputFiles.add(jarInput.asFile)
        }
        return TraceClassLoader.getClassLoader(project, inputFiles)
    }


}