package com.wuzhu.asmtrack;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;


/**
 * @author Hdq on 2022/12/6.
 */
public class MethodEnterAndExitAdapter2 extends AdviceAdapter {

    private static int num = 0;

    private final String className;
    private final String methodName;

    private String traceName;

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
        generateNum();
        traceName = generatorName();
        System.out.println("------=== name = " + traceName);
        mv.visitLdcInsn(traceName);
        mv.visitMethodInsn(
                INVOKESTATIC,
                "com/wuzhu/libasmtrack/AsmTrackQueue",
                "beginSection",
                "(Ljava/lang/String;)V",
                false);
    }

    private static final int maxSectionNameLength = 127;

    private String generatorName() {
        String sectionName = "=" + className + "#" + methodName + "-" + num;
        int length = sectionName.length();
        if (length > maxSectionNameLength) {
            sectionName = sectionName.substring(0, maxSectionNameLength);
        }
        return sectionName;
    }


    private void generateNum() {
        num++;
        if (num >= 999999) {
            num = 1;
        }
    }


    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
        insertEndSection();
    }

    private void insertEndSection() {
        mv.visitLdcInsn(traceName);
        mv.visitMethodInsn(
                INVOKESTATIC,
                "com/wuzhu/libasmtrack/AsmTrackQueue",
                "endSection",
                "(Ljava/lang/String;)V",
                false);
    }

}
