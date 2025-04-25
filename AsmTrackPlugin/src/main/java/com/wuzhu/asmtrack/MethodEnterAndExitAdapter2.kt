package com.wuzhu.asmtrack

import com.wuzhu.asmtrack.utils.InsertCodeUtils
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter

/**
 * @author Hdq on 2022/12/6.
 */
class MethodEnterAndExitAdapter2(
    api: Int, mv: MethodVisitor,
    access: Int, methodName: String, desc: String,
    className: String
) : AdviceAdapter(api, mv, access, methodName, desc) {

    // Class name and method name are stored for later use
    private val className: String = className
    private val methodName: String = methodName

    // Creating a local variable slot for storing trace data
    private val localVarSlot: Int = newLocal(Type.getType(String::class.java))

    // Called when the method is entered
    override fun onMethodEnter() {
        super.onMethodEnter()
        // Insert trace start logic
        insertBeginSection()
    }

    private fun insertBeginSection() {
        // Generate the trace name and insert the begin trace code
        val traceName = InsertCodeUtils.generatorName(className, methodName)
        InsertCodeUtils.insertBegin(traceName, mv, localVarSlot)
    }

    // Called when the method is exited
    override fun onMethodExit(opcode: Int) {
        super.onMethodExit(opcode)
        // Insert trace end logic
        insertEndSection()
    }

    private fun insertEndSection() {
        // Insert the end trace code
        InsertCodeUtils.insertEnd(mv, localVarSlot)
    }
}
