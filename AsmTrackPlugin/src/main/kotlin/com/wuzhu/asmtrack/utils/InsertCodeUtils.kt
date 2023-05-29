package com.wuzhu.asmtrack.utils

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

object InsertCodeUtils {

    private const val maxSectionNameLength = 120
    private var num = 0

    @JvmStatic
    fun generatorName(className: String, methodName: String): String {
        var sectionName = "=$className#$methodName"
        val length = sectionName.length
        if (length > maxSectionNameLength) {
            sectionName = sectionName.substring(0, maxSectionNameLength)
        }
        generateNum()
        return "$sectionName-$num"
    }


    private fun generateNum() {
        num++
        if (num >= 999999) {
            num = 1
        }
    }

    @JvmStatic
    fun insertBegin(traceName: String, methodVisitor: MethodVisitor) {
        println("------=== name = $traceName")
        methodVisitor.visitLdcInsn(traceName)
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            "com/wuzhu/libasmtrack/AsmTrackQueue",
            "beginTrace",
            "(Ljava/lang/String;)V",
            false
        )
    }

    @JvmStatic
    fun insertEnd(traceName: String, methodVisitor: MethodVisitor) {
        methodVisitor.visitLdcInsn(traceName)
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            "com/wuzhu/libasmtrack/AsmTrackQueue",
            "endTrace",
            "(Ljava/lang/String;)V",
            false
        )
    }


}