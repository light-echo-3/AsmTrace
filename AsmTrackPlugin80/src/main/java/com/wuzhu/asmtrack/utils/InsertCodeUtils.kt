package com.wuzhu.asmtrack.utils

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.ALOAD
import org.objectweb.asm.Opcodes.ASTORE
import org.objectweb.asm.Opcodes.INVOKESTATIC

object InsertCodeUtils {

    /***
     * Trace限制名称最大长度127，
     * 保留7位，插桩时生成编号
     * 再保留7位，运行时生成编号
     * see android.os.Trace#MAX_SECTION_NAME_LEN
     * 保留7位生成数字编号
     */
    private const val maxSectionNameLength = 127 - 7 - 7
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
        if (num >= 999_999) {
            num = 1
        }
    }

    @JvmStatic
    fun insertBegin(traceName: String, methodVisitor: MethodVisitor, localVarSlot: Int) {
        println("------=== name = $traceName")
        methodVisitor.visitLdcInsn(traceName)
        methodVisitor.visitMethodInsn(
            INVOKESTATIC,
            "com/wuzhu/libasmtrack/AsmTraceStack",
            "beginTrace",
            "(Ljava/lang/String;)Ljava/lang/String;",
            false
        )
        //将返回值，存到新增的局部变量
        methodVisitor.visitVarInsn(ASTORE, localVarSlot)
    }

    @JvmStatic
    fun insertEnd(methodVisitor: MethodVisitor, localVarSlot: Int) {
        //读取新增的局部变量
        methodVisitor.visitVarInsn(ALOAD, localVarSlot)
        methodVisitor.visitMethodInsn(
            INVOKESTATIC, "com/wuzhu/libasmtrack/AsmTraceStack", "endTrace", "(Ljava/lang/String;)V", false
        )
    }


}