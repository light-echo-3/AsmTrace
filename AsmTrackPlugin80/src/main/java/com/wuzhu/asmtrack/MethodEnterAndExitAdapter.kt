package com.wuzhu.asmtrack

import com.wuzhu.asmtrack.utils.InsertCodeUtils.generatorName
import com.wuzhu.asmtrack.utils.InsertCodeUtils.insertBegin
import com.wuzhu.asmtrack.utils.InsertCodeUtils.insertEnd
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.LocalVariablesSorter

/**
 * @author Hdq on 2022/12/6.
 */
class MethodEnterAndExitAdapter(
    api: Int, mv: MethodVisitor?,
    access: Int, private val methodName: String, private val methodDesc: String,
    private val className: String
) : LocalVariablesSorter(
    api, access,
    methodDesc, mv
) {
    // 用于存储 traceId 的局部变量槽
    private val traceIdSlot: Int = newLocal(Type.getType(String::class.java))
    // 用于存储异常的局部变量槽
    private val exceptionSlot: Int = newLocal(Type.getType("Ljava/lang/Throwable;"))

    private val tryStart = Label()
    private val tryEnd = Label()
    private val catchStart = Label()

    /**
     * 方法开始访问时
     */
    override fun visitCode() {
        // 1. 首先插入 beginTrace 代码
        insertBeginSection()

        // 2. 开始 try 块
        mv.visitLabel(tryStart)

        // 3. 调用父类的方法实现
        super.visitCode()
    }

    private fun insertBeginSection() {
        val traceName = generatorName(className, methodName)
        insertBegin(traceName, mv, traceIdSlot)
    }

    override fun visitMaxs(maxStack: Int, maxLocals: Int) {
        // try 块结束
        mv.visitLabel(tryEnd)

        // catch 块开始
        mv.visitLabel(catchStart)
        mv.visitVarInsn(Opcodes.ASTORE, exceptionSlot)
        // 异常路径 - 调用 endTrace
        insertEndSection()
        // 重新抛出异常
        mv.visitVarInsn(Opcodes.ALOAD, exceptionSlot)
        mv.visitInsn(Opcodes.ATHROW)

        // 设置 try-catch 块
        mv.visitTryCatchBlock(tryStart, tryEnd, catchStart, "java/lang/Throwable")

        // 计算最大栈大小
        val newMaxStack = maxOf(maxStack, 3) // 确保有足够的栈空间

        super.visitMaxs(newMaxStack, maxLocals)
    }

    override fun visitInsn(opcode: Int) {
        when (opcode) {
            Opcodes.IRETURN,
            Opcodes.LRETURN,
            Opcodes.FRETURN,
            Opcodes.DRETURN,
            Opcodes.ARETURN,
            Opcodes.RETURN,
//            Opcodes.ATHROW //不在这里处理 throw，已在 catch handler 处理
                -> {
                // 在任何 return 之前插入 endTrace
                insertEndSection()
            }
        }
        super.visitInsn(opcode)
    }

    private fun insertEndSection() {
        insertEnd(mv, traceIdSlot)
    }
}