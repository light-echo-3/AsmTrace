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
        if (num >= 999999) {
            num = 1
        }
    }

    @JvmStatic
    fun insertBegin(traceName: String, methodVisitor: MethodVisitor, maxLocals: Int) {
        println("------=== name = $traceName")
        methodVisitor.visitLdcInsn(traceName)
        methodVisitor.visitMethodInsn(
            INVOKESTATIC,
            "com/wuzhu/libasmtrack/AsmTraceQueue",
            "beginTrace",
            "(Ljava/lang/String;)Ljava/lang/String;",
            false
        )
        //插入到本地变量表最后面，这样可以保证重新计算栈帧（重新计算本地变量表和操作数栈）的正确性
        methodVisitor.visitVarInsn(ASTORE, maxLocals)

    }

    @JvmStatic
    fun insertEnd(methodVisitor: MethodVisitor, maxLocals: Int) {
        methodVisitor.visitVarInsn(ALOAD, maxLocals)
        methodVisitor.visitMethodInsn(
            INVOKESTATIC, "com/wuzhu/libasmtrack/AsmTraceQueue", "endTrace", "(Ljava/lang/String;)V", false
        )
//        methodVisitor.visitInsn(RETURN)
//        methodVisitor.visitMaxs(1, 1)
//        methodVisitor.visitEnd()
    }


}