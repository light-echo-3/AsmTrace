package com.wuzhu.asmtrack.task

import com.wuzhu.asmtrack.Config
import com.wuzhu.asmtrack.business.ClassHandler
import org.gradle.api.DefaultTask
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
//                        scanAnnotationClass(file.inputStream())
                        ClassHandler.handleClassInDirectory(file, config)
                    }

                    val relativePath = directory.asFile.toURI().relativize(file.toURI()).path
                    jarOutput.putNextEntry(
                        JarEntry(relativePath.replace(File.separatorChar, '/'))
                    )
                    file.inputStream().use { inputStream ->
                        inputStream.copyTo(jarOutput)
                    }
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
//                    scanAnnotationClass(jarFile.getInputStream(jarEntry))
                    val outByteArray = ClassHandler.handleClassInJar(jarFile,jarEntry,config)

                    //1.putNextEntry
                    jarOutput.putNextEntry(JarEntry(jarEntry.name))
                    //2.写入字节
                    jarOutput.write(outByteArray)

                    jarOutput.closeEntry()
                }
            }
            jarFile.close()
        }

        //关闭输出流
        jarOutput.close()
    }

//    /**
//     * 扫描有目标注解的类
//     *
//     * @param inputStream
//     */
//    private fun scanAnnotationClass(inputStream: InputStream) {
//        val classReader = ClassReader(inputStream)
//        val classNode = ClassNode()
//        classReader.accept(classNode, ClassReader.EXPAND_FRAMES)
//        val annotations = classNode.invisibleAnnotations //获取声明的所有注解
//        if (annotations != null && annotations.isNotEmpty()) {
//            annotations.forEach { aNode ->
//                if ("Lcom/znh/aop/annotation/HuiRouterPath;" == aNode.desc) {
//                    var pathKey = classNode.name
//                    if (aNode.values != null && aNode.values.size > 1) {
//                        pathKey = aNode.values[1] as? String
//                    }
//                    annotationPathMap[pathKey] = classNode.name.replace("/", ".")
//                }
//            }
//        }
//        inputStream.close()
//    }
//
//    /**
//     * 遍历并修改目标class
//     */
//    private fun transformJar(inputJar: File, jarOutput: JarOutputStream) {
//        val jarFile = JarFile(inputJar)
//        jarFile.entries().iterator().forEach { jarEntry ->
//            if (jarEntry.name.equals("com/znh/aop/api/HuiRouterApi.class")) {
//                jarOutput.putNextEntry(JarEntry(jarEntry.name))
//                asmTransform(jarFile.getInputStream(jarEntry)).inputStream().use {
//                    it.copyTo(jarOutput)
//                }
//                jarOutput.closeEntry()
//            }
//        }
//        jarFile.close()
//    }
//
//    /**
//     * 对目标class进行修改
//     */
//    private fun asmTransform(inputStream: InputStream): ByteArray {
//        val cr = ClassReader(inputStream)
//        val cw = ClassWriter(cr, ClassWriter.COMPUTE_MAXS)
//        val classVisitor = HuiRouterCVisitor(cw, annotationPathMap)
//        cr.accept(classVisitor, ClassReader.EXPAND_FRAMES)
//        return cw.toByteArray()
//    }
}