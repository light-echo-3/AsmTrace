package com.wuzhu.asmtrack.utils

import com.wuzhu.asmtrack.Config
import org.objectweb.asm.tree.ClassNode

object NotTrackUtils {


    /**
     * @return true:不插桩
     */
    fun isNotTrackByConfig(classNode: ClassNode, traceConfig: Config): Boolean {
        return isNotTrackByConfig("${classNode.name}.class", traceConfig)
    }

    /**
     * @param fileName 格式：
     * 例：fileName = kotlin/jvm/internal/Intrinsics.class
     * @return true:不插桩
     */
    fun isNotTrackByConfig(fileName: String, traceConfig: Config): Boolean {
        return if (traceConfig.isNeedTraceClass(fileName)) {//例：classNode.name = com/wuzhu/testandroid51/kotlin/TestObject
            false
        } else {
            println("------class文件不插桩：$fileName")
            true
        }
    }

    /**
     * 用注解 com.wuzhu.libasmtrack.NotTrack 注释的类，不插桩
     * @return true:不插桩
     */
    fun isNotTrackByAnnotation(classNode: ClassNode): Boolean {
        //处理注解
        val annotations = classNode.invisibleAnnotations //获取声明的所有注解
        if (annotations != null) { //遍历注解
            for (annotationNode in annotations) {
                //获取注解的描述信息
                if ("Lcom/wuzhu/libasmtrack/NotTrace;" == annotationNode.desc) {
                    return true
                }
            }
        }
        return false
    }


}