package com.wuzhu.asmtrack;

import com.wuzhu.asmtrack.utils.InsertCodeUtils;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author Hdq on 2022/12/6.
 */
public class MethodEnterAndExitAdapter extends MethodVisitor {

    private final String className;
    private final String methodName;

    private String traceName;

    public MethodEnterAndExitAdapter(int api,
                                     MethodVisitor methodVisitor,
                                     String className,
                                     String methodName) {
        super(api, methodVisitor);
        this.className = className;
        this.methodName = methodName;
    }

    /**
     * 方法开始访问时
     */
    @Override
    public void visitCode() {
        // 1.首先处理自己的代码逻辑
        // MethodEnter...

        insertBeginSection();

        // 2.然后调用父类的方法实现
        super.visitCode();
    }

    private void insertBeginSection() {
        traceName = InsertCodeUtils.generatorName(className, methodName);
        InsertCodeUtils.insertBegin(traceName, mv);
    }

    @Override
    public void visitInsn(int opcode) {
        // 1.首先处理自己的代码逻辑
        if (opcode == Opcodes.ATHROW || (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
            // MethodExit...
            insertEndSection();
        }
        // 2.然后调用父类的方法实现
        super.visitInsn(opcode);
    }

    private void insertEndSection() {
        InsertCodeUtils.insertEnd(traceName, mv);
    }


}
