package com.wuzhu.asmtrack;

import com.wuzhu.asmtrack.utils.InsertCodeUtils;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * @author Hdq on 2022/12/6.
 */
public class MethodEnterAndExitAdapter3 extends MethodVisitor {

    private final int maxLocals;
    private final String className;
    private final String methodName;

    public MethodEnterAndExitAdapter3(int api,
                                      MethodVisitor methodVisitor,
                                      String className,
                                      String methodName,
                                      String descriptor,
                                      ClassNode classNode) {
        super(api, methodVisitor);
        this.className = className;
        this.methodName = methodName;
        this.maxLocals = getMaxLocals(classNode, methodName, descriptor);
    }

    /**
     * 获取局部变量表大小
     * 查找指定方法的MethodNode,获取maxLocals
     */
    private int getMaxLocals(ClassNode classNode, String methodName, String descriptor) {
        MethodNode methodNode = null;
        for (MethodNode method : classNode.methods) {
            if (method.name.equals(methodName) && method.desc.equals(descriptor)) {
                methodNode = method;
                break;
            }
        }
        assert methodNode != null;
        return methodNode.maxLocals;
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
        String traceName = InsertCodeUtils.generatorName(className, methodName);
        InsertCodeUtils.insertBegin(traceName, mv, maxLocals);
    }

    @Override
    public void visitInsn(int opcode) {
        // 1.首先处理自己的代码逻辑
        if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {//opcode == Opcodes.ATHROW || //throw 不要插桩（自己抛出异常又自己捕获，插桩两次，导致出错）
            // MethodExit...
            insertEndSection();
        }
        // 2.然后调用父类的方法实现
        super.visitInsn(opcode);
    }

    private void insertEndSection() {
        InsertCodeUtils.insertEnd(mv, maxLocals);
    }

}
