package com.wuzhu.asmtrack.business

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.TransformOutputProvider
import com.android.utils.FileUtils
import com.wuzhu.asmtrack.Config
import com.wuzhu.asmtrack.ScanClassVisitor
import com.wuzhu.asmtrack.utils.NotTrackUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import java.io.*

object HandleDirectoryInputBusiness {


    @JvmStatic
    @Throws(IOException::class)
    fun traceDirectory(
        classLoader: ClassLoader,
        directoryInput: DirectoryInput,
        outputProvider: TransformOutputProvider,
        traceConfig: Config
    ) {
        handleDirectoryInput(classLoader, directoryInput, traceConfig)

        // 获取output目录
        val dest: File = outputProvider.getContentLocation(
            directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY
        )
        //这里执行字节码的注入，不操作字节码的话也要将输入路径拷贝到输出路径
        FileUtils.copyDirectory(directoryInput.file, dest)
    }


    /**
     * 处理文件目录下的class文件
     */
    @Throws(IOException::class)
    private fun handleDirectoryInput(
        classLoader: ClassLoader, directoryInput: DirectoryInput, traceConfig: Config
    ) {
        println("------" + directoryInput.file)
        val files: MutableList<File> = ArrayList()
        //列出目录所有文件（包含子文件夹，子文件夹内文件）
        listFiles(files, directoryInput.file)
        for (file in files) {
            scanClass(classLoader, file, traceConfig)
        }
    }

    private fun scanClass(classLoader: ClassLoader, inFile: File, traceConfig: Config) {
        try {
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
                override fun getClassLoader(): ClassLoader {
                    return classLoader
                }
            }
            try {
                val classVisitor = ScanClassVisitor(classNode, Opcodes.ASM7, classWriter)
                classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            //覆盖原来的class文件
            val code = classWriter.toByteArray()
            val fos = FileOutputStream(
                inFile.parentFile.absolutePath + File.separator + inFile.name
            )
            fos.write(code)
            fos.close()
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun listFiles(files: MutableList<File>, file: File) {
        if (file.isFile && file.name.endsWith(".class")) {
            println("------file = $file")
            files.add(file)
            return
        }
        val subFiles = file.listFiles()
        if (subFiles != null) {
            for (subFile in subFiles) {
                listFiles(files, subFile)
            }
        }
    }


}