package com.wuzhu.asmtrack.business

import com.wuzhu.asmtrack.Config
import com.wuzhu.asmtrack.ScanClassVisitor
import com.wuzhu.asmtrack.utils.NotTrackUtils
import org.gradle.internal.impldep.org.apache.commons.io.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile

object ClassHandler {

    /***
     * [com.wuzhu.asmtrack.business.HandleDirectoryInputBusiness.scanClass]
     */
    fun handleClassInDirectory(inFile: File, traceConfig: Config) {
        val inputStream: InputStream = FileInputStream(inFile)
        val classReader = ClassReader(inputStream)
        val classNode = ClassNode()
        classReader.accept(classNode, 0)
        if (NotTrackUtils.isNotTrackByAnnotation(classNode) || NotTrackUtils.isNotTrackByConfig(
                classNode, traceConfig
            )
        ) {
            inputStream.close()
            return
        }
        val classWriter = object : ClassWriter(classReader, COMPUTE_FRAMES) {
//            override fun getClassLoader(): ClassLoader {
//                return classLoader
//            }
        }
        try {
            val classVisitor = ScanClassVisitor(classNode, Opcodes.ASM7, classWriter)
            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
            //覆盖原来的class文件
            val code = classWriter.toByteArray()
            val fos =
                FileOutputStream(inFile.parentFile.absolutePath + File.separator + inFile.name)
            fos.write(code)
            fos.close()
        } catch (e: Throwable) {
            println("---error---插桩失败：inFile = $inFile")
            e.printStackTrace()
        } finally {
            inputStream.close()
        }
    }

    /**
     * [com.wuzhu.asmtrack.business.HandleJarInputBusiness.handleJarInput]
     */
    fun handleClassInJar(jarFile: JarFile, jarEntry: JarEntry, traceConfig: Config): ByteArray {

        val inputStream = jarFile.getInputStream(jarEntry)
        val entryName = jarEntry.name

        val classReader = ClassReader(IOUtils.toByteArray(inputStream))
        val classNode = ClassNode() //创建ClassNode,读取的信息会封装到这个类里面
        classReader.accept(classNode, 0) //开始读取

        when {
            NotTrackUtils.isNotTrackByConfig(entryName, traceConfig) -> {
                return IOUtils.toByteArray(inputStream).also { inputStream.close() }
            }

            NotTrackUtils.isNotTrackByAnnotation(classNode) -> {
                return IOUtils.toByteArray(inputStream).also { inputStream.close() }
            }

            else -> {
                val classWriter = object : ClassWriter(classReader, COMPUTE_FRAMES) {}
                try {
                    val cv: ClassVisitor = ScanClassVisitor(classNode, Opcodes.ASM7, classWriter)
                    classReader.accept(cv, ClassReader.EXPAND_FRAMES)
                    val code = classWriter.toByteArray()
                    return code.also {
                        inputStream.close()
                    }
                } catch (e: Throwable) {
                    println("---error---插桩失败：entryName = $entryName")
                    e.printStackTrace()
                    return IOUtils.toByteArray(inputStream).also { inputStream.close() }
                }
            }
        }
    }

}