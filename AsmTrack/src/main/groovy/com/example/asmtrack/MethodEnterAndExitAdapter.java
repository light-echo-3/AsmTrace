package com.example.asmtrack;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author Hdq on 2022/12/6.
 */
public class MethodEnterAndExitAdapter  extends MethodVisitor {

    private String className;
    private String methodName;

    public MethodEnterAndExitAdapter(int api, MethodVisitor methodVisitor, String className,String methodName) {
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
        String name = generatorName();
        System.out.println("------=== name = " + name);
        mv.visitLdcInsn(name);
        mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                "android/os/Trace",
                "beginSection",
                "(Ljava/lang/String;)V",
                false
        );
    }

    private static final int  maxSectionNameLength = 127;
    private String generatorName() {
        String sectionName = "-" + className + "#" + methodName;
        int length = sectionName.length();
        if (length > maxSectionNameLength) {
            sectionName = sectionName.substring(0,maxSectionNameLength);
        }
        return sectionName;
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
        //invokestatic: 调用静态方法System.currentTimeMillis()，返回值为基础类型long
        //第二个参数代表类的全限定名，第三个参数代表方法名，第四个参数代表函数签名，()J的意思是不接受参数，返回值为J (J在字节码里代表基础类型long)
        mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                "android/os/Trace",
                "endSection",
                "()V",
                false);
    }

}
