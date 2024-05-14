package com.wuzhu.asmtrack;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;

/**
 * @author Hdq on 2022/12/6.
 */
public class ScanClassVisitor extends ClassVisitor {
    private final ClassNode classNode;
    private boolean isInterface;
    private String className;


    public ScanClassVisitor(ClassNode classNode, int api, ClassVisitor cv) {
        super(api, cv);
        this.classNode = classNode;
    }

    @Override
    public void visit(int version, int access, String name, String signature,
                      String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        isInterface = (access & Opcodes.ACC_INTERFACE) != 0;
        className = name.substring(name.lastIndexOf(File.separator) + 1);//name = com/example/testandroid51/MainActivity
    }

    @Override
    public MethodVisitor visitMethod(int access,
                                     String name,
                                     String descriptor,
                                     String signature,
                                     String[] exceptions) {
        boolean isConstructor = "<init>".equals(name) || "<clinit>".equals(name);
        boolean isAbstractMethod = (access & Opcodes.ACC_ABSTRACT) != 0;
        boolean isNativeMethod = (access & Opcodes.ACC_NATIVE) != 0;
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (isInterface || isAbstractMethod || isNativeMethod || isConstructor) {
            return mv;
        } else {
//            return new MethodEnterAndExitAdapter(api, mv, className, name, descriptor, classNode);
            return new MethodEnterAndExitAdapter(api, mv, access, name, descriptor, className);
        }
    }


}