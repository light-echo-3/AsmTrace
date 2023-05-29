package com.wuzhu.asmtrack;

import com.wuzhu.asmtrack.utils.InsertCodeUtils;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;


/**
 * @author Hdq on 2022/12/6.
 */
public class MethodEnterAndExitAdapter2 extends AdviceAdapter {


    private final String className;
    private final String methodName;

    private String traceName;

    public MethodEnterAndExitAdapter2(final int api, final MethodVisitor mv,
                                      final int access, final String methodName, final String desc,
                                      final String className) {
        super(api, mv, access, methodName, desc);
        this.className = className;
        this.methodName = methodName;
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        insertBeginSection();
    }

    private void insertBeginSection() {
        traceName = InsertCodeUtils.generatorName(className, methodName);
        InsertCodeUtils.insertBegin(traceName, mv);
    }


    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
        insertEndSection();
    }

    private void insertEndSection() {
        InsertCodeUtils.insertEnd(traceName, mv);
    }

}
