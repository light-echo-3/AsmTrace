package com.wuzhu.asmtrack;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;

/**
 * @author Hdq on 2022/12/6.
 */
public class ScanClassVisitor2 extends ClassVisitor {
    private boolean isInterface;
    private String className;


    public ScanClassVisitor2(int api, ClassVisitor cv) {
        super(api, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature,
                      String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        isInterface = (access & Opcodes.ACC_INTERFACE) > 0 || (access & Opcodes.ACC_ABSTRACT) > 0;
        className = name.substring(name.lastIndexOf(File.separator) + 1);//name = com/example/testandroid51/MainActivity
    }

    @Override
    public MethodVisitor visitMethod(int access,
                                     String name,
                                     String descriptor,
                                     String signature,
                                     String[] exceptions) {
        boolean isConstructor = name.contains("<init>") || name.contains("<clinit>");
         if (isInterface || isConstructor) {
             return super.visitMethod(access, name, descriptor, signature, exceptions);
        } else {
            MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
             return new MethodEnterAndExitAdapter2(api, mv,access,  name,descriptor,className);
        }

    }


}