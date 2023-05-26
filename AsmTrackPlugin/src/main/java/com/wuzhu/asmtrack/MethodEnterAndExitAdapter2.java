package com.wuzhu.asmtrack;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;


/**
 * @author Hdq on 2022/12/6.
 */
public class MethodEnterAndExitAdapter2 extends AdviceAdapter {

    private final String className;
    private final String methodName;

    public MethodEnterAndExitAdapter2(final int api, final MethodVisitor mv,
                                      final int access, final String methodName, final String desc,
                                      final String className) {
        super(api,mv,access,methodName,desc);
        this.className = className;
        this.methodName = methodName;
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        insertBeginSection();
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

    private static final int maxSectionNameLength = 127;

    private String generatorName() {
        String sectionName = "==" + className + "#" + methodName;
        int length = sectionName.length();
        if (length > maxSectionNameLength) {
            sectionName = sectionName.substring(0, maxSectionNameLength);
        }
        return sectionName;
    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
        insertEndSection();
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
