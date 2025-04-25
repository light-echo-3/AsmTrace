//package com.wuzhu.asmtrack
//
//import com.android.build.api.transform.*
//import com.android.build.gradle.internal.pipeline.TransformManager
//import com.wuzhu.asmtrack.business.HandleDirectoryInputBusiness
//import com.wuzhu.asmtrack.business.HandleJarInputBusiness
//import com.wuzhu.asmtrack.business.TraceClassLoader
//import org.gradle.api.Project
//import java.io.File
//import java.io.IOException
//
///**
// * @author Hdq on 2022/12/6.
// */
//class ASMTransform(private val project: Project) : Transform() {
//
//    // transform 名称
//    override fun getName(): String {
//        return this::class.simpleName!!
//    }
//
//    // 输入源，class文件
//    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
//        return TransformManager.CONTENT_CLASS
//    }
//
//    // 文件范围，整个工程
//    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
//        return TransformManager.SCOPE_FULL_PROJECT
//    }
//
//    // 是否增量编译，可用于编译优化
//    override fun isIncremental(): Boolean {
//        return false
//    }
//
//    // 核心方法
//    @Throws(TransformException::class, InterruptedException::class, IOException::class)
//    override fun transform(transformInvocation: TransformInvocation) {
//        super.transform(transformInvocation)
//        val pluginConfig = project.extensions.getByName("asmTrackPluginConfig") as AsmTrackPluginConfig
//
//        Logger.w("pluginConfig = $pluginConfig")
//
//        if (!transformInvocation.isIncremental) {
//            // 不是增量编译删除所有的outputProvider
//            transformInvocation.outputProvider.deleteAll()
//        }
//
//        // 获取输入源
//        val inputs = transformInvocation.inputs
//        val classLoader = createClassLoader(project, inputs)
//
//        inputs.forEach { transformInput ->
//            // 处理所有文件夹中的类
//            transformInput.directoryInputs.forEach { directoryInput ->
//                try {
//                    println("------directoryInput = $directoryInput")
//                    HandleDirectoryInputBusiness.traceDirectory(
//                        classLoader,
//                        directoryInput,
//                        transformInvocation.outputProvider,
//                        Config()
//                    )
//                } catch (e: IOException) {
//                    println("------HandleDirectoryInputBusiness.traceDirectory error: $e")
//                    e.printStackTrace()
//                }
//            }
//
//            // 处理所有jar包中的类
//            transformInput.jarInputs.forEach { jarInput ->
//                try {
//                    println("------jarInput = $jarInput")
//                    HandleJarInputBusiness.traceJarFiles(
//                        classLoader,
//                        jarInput,
//                        transformInvocation.outputProvider,
//                        Config()
//                    )
//                } catch (e: IOException) {
//                    println("------ HandleJarInputBusiness.traceJarFiles error: $e")
//                    e.printStackTrace()
//                }
//            }
//        }
//    }
//
//    private fun createClassLoader(project: Project, inputs: Collection<TransformInput>): ClassLoader {
//        val inputFiles = mutableListOf<File>()
//        inputs.forEach { transformInput ->
//            transformInput.directoryInputs.forEach { directoryInput ->
//                inputFiles.add(directoryInput.file)
//            }
//            transformInput.jarInputs.forEach { jarInput ->
//                inputFiles.add(jarInput.file)
//            }
//        }
//        return TraceClassLoader.getClassLoader(project, inputFiles)
//    }
//}