package com.wuzhu.asmtrack.business

import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.TransformOutputProvider
import com.wuzhu.asmtrack.Config
import com.wuzhu.asmtrack.ScanClassVisitor
import com.wuzhu.asmtrack.utils.NotTrackUtils
import org.gradle.internal.impldep.org.apache.commons.codec.digest.DigestUtils
import org.gradle.internal.impldep.org.apache.commons.io.FileUtils
import org.gradle.internal.impldep.org.apache.commons.io.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

@Deprecated("use:com.wuzhu.asmtrack.business.ClassHandler")
object HandleJarInputBusiness {

    @JvmStatic
    @Throws(IOException::class)
    fun traceJarFiles(
        classLoader: ClassLoader, jarInput: JarInput, outputProvider: TransformOutputProvider, traceConfig: Config
    ) {
        if (jarInput.file.absolutePath.endsWith(".jar")) {
            handleJarInput(classLoader, jarInput, outputProvider, traceConfig)
        }
    }

    private fun handleJarInput(
        classLoader: ClassLoader, jarInput: JarInput, outputProvider: TransformOutputProvider, traceConfig: Config
    ) {
        val jarFile = JarFile(jarInput.file)
        val enumeration = jarFile.entries()

        val tmpFile = File(jarInput.file.parent + File.separator + "classes_temp.jar")
        if (tmpFile.exists()) {
            tmpFile.delete()
        }

        val jarOutputStream = JarOutputStream(FileOutputStream(tmpFile))

        //循环jar包里的文件
        while (enumeration.hasMoreElements()) {
            val jarEntry: JarEntry = enumeration.nextElement()
            val entryName: String = jarEntry.name
            val zipEntry = ZipEntry(entryName)
            val inputStream: InputStream = jarFile.getInputStream(jarEntry)
            if (!NotTrackUtils.isNotTrackByConfig(entryName, traceConfig)) {
                jarOutputStream.putNextEntry(zipEntry)
                val classReader = ClassReader(IOUtils.toByteArray(inputStream))
                val classNode = ClassNode() //创建ClassNode,读取的信息会封装到这个类里面
                classReader.accept(classNode, 0) //开始读取
                if (NotTrackUtils.isNotTrackByAnnotation(classNode)) {
                    jarOutputStream.write(classReader.b)
                    continue
                }
                val classWriter = object : ClassWriter(classReader, COMPUTE_FRAMES) {
                    override fun getClassLoader(): ClassLoader {
                        return classLoader
                    }
                }
                try {
                    val cv: ClassVisitor = ScanClassVisitor(classNode, Opcodes.ASM7, classWriter)
                    classReader.accept(cv, ClassReader.EXPAND_FRAMES)
                    val code = classWriter.toByteArray()
                    jarOutputStream.write(code)
                } catch (e: Throwable) {
                    println("---error---插桩失败：entryName = $entryName")
                    e.printStackTrace()
                    jarOutputStream.write(classReader.b)
                }
            } else {
                jarOutputStream.putNextEntry(zipEntry)//1.put节点
                jarOutputStream.write(IOUtils.toByteArray(inputStream))//2.写入数据
            }
            jarOutputStream.closeEntry()
            inputStream.close()
        }

        jarOutputStream.close()
        jarFile.close()

        FileUtils.copyFile(tmpFile, getDestFile(jarInput, outputProvider))
        tmpFile.delete()

    }

    private fun getDestFile(jarInput: JarInput, outputProvider: TransformOutputProvider): File? {
        var jarName = jarInput.name
        if (jarName.endsWith(".jar")) {
            jarName = jarName.substring(0, jarName.length - 4)
        }
        val md5Name = DigestUtils.md5Hex(jarInput.file.absolutePath)
        //处理完输出给下一任务作为输入
        return outputProvider.getContentLocation(
            jarName + md5Name, jarInput.contentTypes, jarInput.scopes, Format.JAR
        )
    }
}