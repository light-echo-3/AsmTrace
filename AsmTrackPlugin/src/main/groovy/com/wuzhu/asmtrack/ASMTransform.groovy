package com.wuzhu.asmtrack


import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.wuzhu.asmtrack.business.HandleDirectoryInputBusiness
import com.wuzhu.asmtrack.business.HandleJarInputBusiness

import org.gradle.api.Project


/**
 * @author Hdq on 2022/12/6.
 */
class ASMTransform extends Transform {

    Project project

    ASMTransform(Project project) {
        this.project = project
    }

    // transform 名称
    @Override
    String getName() {
        return this.getClass().getSimpleName()
    }

    // 输入源，class文件
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    // 文件范围，整个工程
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    // 是否增量编译，可用于编译优化
    @Override
    boolean isIncremental() {
        return false
    }

    // 核心方法
    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        PluginConfig pluginConfig = project.asmTrackPluginConfig.toPluginConfig()
        Logger.w("pluginConfig = " + pluginConfig)
        if (!transformInvocation.isIncremental()) {
            //不是增量编译删除所有的outputProvider
            transformInvocation.getOutputProvider().deleteAll()
        }
        // 获取输入源
        Collection<TransformInput> inputs = transformInvocation.getInputs()
        inputs.forEach(transformInput -> {
            transformInput.getDirectoryInputs().forEach(directoryInput -> {
                try {
                    System.out.println("------directoryInput = " + directoryInput)
                    // 处理输入源
                    HandleDirectoryInputBusiness.traceDirectory(directoryInput,
                            transformInvocation.getOutputProvider(),
                            new Config())
                } catch (IOException e) {
                    System.out.println("------HandleDirectoryInputBusiness.traceDirectory error:" + e)
                    e.printStackTrace()
                }
            })

            transformInput.getJarInputs().forEach(jarInput -> {
                try {
                    System.out.println("------jarInput = " + jarInput)
                    HandleJarInputBusiness.traceJarFiles(jarInput,
                            transformInvocation.getOutputProvider(),
                            new Config())
                } catch (IOException e) {
                    System.out.println("------ HandleJarInputBusiness.traceJarFiles error:" + e)
                    e.printStackTrace()
                }
            })

        })
    }


}
