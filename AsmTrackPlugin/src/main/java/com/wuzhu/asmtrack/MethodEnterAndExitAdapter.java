package com.wuzhu.asmtrack;

import com.wuzhu.asmtrack.utils.InsertCodeUtils;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.LocalVariablesSorter;

public class MethodEnterAndExitAdapter extends LocalVariablesSorter {

    private final int traceIdSlot;
    private final int exceptionSlot;

    private final String methodName;
    private final String methodDesc;
    private final String className;

    private final Label tryStart = new Label();
    private final Label tryEnd = new Label();
    private final Label catchStart = new Label();

    public MethodEnterAndExitAdapter(
            int api, MethodVisitor mv,
            int access, String methodName, String methodDesc, String className
    ) {
        super(api, access, methodDesc, mv);
        this.methodName = methodName;
        this.methodDesc = methodDesc;
        this.className = className;

        // 分配局部变量槽
        this.traceIdSlot = newLocal(Type.getType(String.class));
        this.exceptionSlot = newLocal(Type.getType("Ljava/lang/Throwable;"));
    }

    @Override
    public void visitCode() {
        // 插入 beginTrace
        insertBeginSection();

        // try 块开始标签
        mv.visitLabel(tryStart);

        // 父类 visitCode
        super.visitCode();
    }

    private void insertBeginSection() {
        String traceName = InsertCodeUtils.generatorName(className, methodName);
        InsertCodeUtils.insertBegin(traceName, mv, traceIdSlot);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        // try 块结束
        mv.visitLabel(tryEnd);

        // catch 块开始
        mv.visitLabel(catchStart);
        mv.visitVarInsn(Opcodes.ASTORE, exceptionSlot);

        // 插入 endTrace
        insertEndSection();

        // 重新抛出异常
        mv.visitVarInsn(Opcodes.ALOAD, exceptionSlot);
        mv.visitInsn(Opcodes.ATHROW);

        // 设置 try-catch 块
        mv.visitTryCatchBlock(tryStart, tryEnd, catchStart, "java/lang/Throwable");

        // 计算新栈大小
        int newMaxStack = Math.max(maxStack, 3);
        super.visitMaxs(newMaxStack, maxLocals);
    }

    @Override
    public void visitInsn(int opcode) {
        switch (opcode) {
            case Opcodes.IRETURN:
            case Opcodes.LRETURN:
            case Opcodes.FRETURN:
            case Opcodes.DRETURN:
            case Opcodes.ARETURN:
            case Opcodes.RETURN:
                // 在 return 前插入 endTrace
                insertEndSection();
                break;
            // case Opcodes.ATHROW: 不在这里处理 throw，已在 catch handler 处理
        }

        super.visitInsn(opcode);
    }

    private void insertEndSection() {
        InsertCodeUtils.insertEnd(mv, traceIdSlot);
    }
}
