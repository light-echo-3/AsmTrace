package com.wuzhu.asmtrack.business

import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.TransformOutputProvider
import com.android.utils.FileUtils
import com.wuzhu.asmtrack.Config
import com.wuzhu.asmtrack.ScanClassVisitor
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.compress.utils.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

object HandleJarInputBusiness {


    @JvmStatic
    @Throws(IOException::class)
    fun traceJarFiles(jarInput: JarInput, outputProvider: TransformOutputProvider, traceConfig: Config) {
        if (jarInput.file.absolutePath.endsWith(".jar")) {
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
                if (traceConfig.isNeedTraceClass(entryName)) {
                    jarOutputStream.putNextEntry(zipEntry)
                    val classReader = ClassReader(IOUtils.toByteArray(inputStream))
                    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                    val cv: ClassVisitor = ScanClassVisitor(Opcodes.ASM5, classWriter)
                    try {
                        classReader.accept(cv, ClassReader.EXPAND_FRAMES)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    val code = classWriter.toByteArray()
                    jarOutputStream.write(code)
                } else {
                    jarOutputStream.putNextEntry(zipEntry)
                    jarOutputStream.write(IOUtils.toByteArray(inputStream))
                }
                jarOutputStream.closeEntry()
            }

            jarOutputStream.close()
            jarFile.close()


            var jarName = jarInput.name
            if (jarName.endsWith(".jar")) {
                jarName = jarName.substring(0, jarName.length - 4)
            }
            val md5Name = DigestUtils.md5Hex(jarInput.file.absolutePath)
            //处理完输出给下一任务作为输入
            val dest = outputProvider.getContentLocation(
                jarName + md5Name, jarInput.contentTypes, jarInput.scopes, Format.JAR
            )
            FileUtils.copyFile(tmpFile, dest)
            tmpFile.delete()
        }
    }
}