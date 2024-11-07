package com.wuzhu.asmtrack

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import java.io.File

/**
 * @author Hdq on 2022/12/6.
 */
class ScanClassVisitor(
    private val classNode: ClassNode,
    api: Int,
    cv: ClassVisitor
) : ClassVisitor(api, cv) {

    private var isInterface: Boolean = false
    private lateinit var className: String

    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        isInterface = (access and Opcodes.ACC_INTERFACE) != 0
        className = name.substring(name.lastIndexOf(File.separator) + 1) // Extract class name from the full path
    }

    override fun visitMethod(
        access: Int,
        name: String,
        descriptor: String,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor? {
        val isConstructor = "<init>" == name || "<clinit>" == name
        val isAbstractMethod = (access and Opcodes.ACC_ABSTRACT) != 0
        val isNativeMethod = (access and Opcodes.ACC_NATIVE) != 0

        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        return if (isInterface || isAbstractMethod || isNativeMethod || isConstructor) {
            mv
        } else {
            // Returning the custom MethodEnterAndExitAdapter
            MethodEnterAndExitAdapter(api, mv, access, name, descriptor, className)
        }
    }
}
