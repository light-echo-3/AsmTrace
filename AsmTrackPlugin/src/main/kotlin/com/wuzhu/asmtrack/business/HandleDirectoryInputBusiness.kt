package com.wuzhu.asmtrack.business

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.TransformOutputProvider
import com.android.utils.FileUtils
import com.wuzhu.asmtrack.Config
import com.wuzhu.asmtrack.ScanClassVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import java.io.*

object HandleDirectoryInputBusiness {


    @JvmStatic
    @Throws(IOException::class)
    fun traceDirectory(directoryInput: DirectoryInput, outputProvider: TransformOutputProvider, @Suppress("UNUSED_PARAMETER") traceConfig: Config) {
        handleDirectoryInput(directoryInput)

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
    private fun handleDirectoryInput(directoryInput: DirectoryInput) {
        println("------" + directoryInput.file)
        val files: MutableList<File> = ArrayList()
        //列出目录所有文件（包含子文件夹，子文件夹内文件）
        listFiles(files, directoryInput.file)
        for (file in files) {
            scanClass(file)
        }
    }

    private fun scanClass(inFile: File) {
        try {
            val inputStream: InputStream = FileInputStream(inFile)
            val classReader = ClassReader(inputStream)
            if (isNotTrack(classReader)) {
                return
            }
            val classWriter = ClassWriter(classReader, 0)
            val classVisitor = ScanClassVisitor(Opcodes.ASM7, classWriter)
            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)

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

    /**
     * 用注解 com.wuzhu.libasmtrack.NotTrack 注释的类，不插桩
     *
     * @return true:不插桩
     */
    private fun isNotTrack(reader: ClassReader): Boolean {
        val classNode = ClassNode() //创建ClassNode,读取的信息会封装到这个类里面
        reader.accept(classNode, 0) //开始读取
        val annotations = classNode.invisibleAnnotations //获取声明的所有注解
        if (annotations != null) { //遍历注解
            for (annotationNode in annotations) {
                //获取注解的描述信息
                if ("Lcom/wuzhu/libasmtrack/NotTrack;" == annotationNode.desc) {
                    return true
                }
            }
        }
        return false
    }

    private fun listFiles(files: MutableList<File>, file: File) {
        if (file.isFile) {
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