package com.wuzhu.asmtrack.utils

import org.objectweb.asm.ClassReader
import org.objectweb.asm.tree.ClassNode

object NotTrackUtils {

    /**
     * 用注解 com.wuzhu.libasmtrack.NotTrack 注释的类，不插桩
     *
     * @return true:不插桩
     */
    fun isNotTrack(reader: ClassReader): Boolean {
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


}