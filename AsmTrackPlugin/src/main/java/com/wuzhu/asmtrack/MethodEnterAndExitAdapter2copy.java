package com.wuzhu.asmtrack;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;


/**
 * @author Hdq on 2022/12/6.
 */
public class MethodEnterAndExitAdapter2copy extends AdviceAdapter {

    private final String className;
    private final String methodName;

    public MethodEnterAndExitAdapter2copy(final int api, final MethodVisitor mv,
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
                INVOKESTATIC,
                "com/wuzhu/libasmtrack/AsmTrackQueue",
                "beginSection",
                "(Ljava/lang/String;)Ljava/lang/String;",
                false);
        //处理返回值
        mv.visitVarInsn(ASTORE, 2);
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
        mv.visitVarInsn(ALOAD, 2);

//        String name = generatorName();
//        mv.visitLdcInsn(name);

        mv.visitMethodInsn(
                INVOKESTATIC,
                "com/wuzhu/libasmtrack/AsmTrackQueue",
                "endSection",
                "(Ljava/lang/String;)V",
                false);
    }

}
