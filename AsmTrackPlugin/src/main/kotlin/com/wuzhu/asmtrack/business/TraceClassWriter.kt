package com.wuzhu.asmtrack.business

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter

/**
 * 此类参考 Matrix
 * Created by habbyge on 2019/4/25.
 *
 * fix:
 * java.lang.TypeNotPresentException: Type android/content/res/TypedArray not present
 * at org.objectweb.asm.ClassWriter.getCommonSuperClass(ClassWriter.java:1025)
 * at org.objectweb.asm.SymbolTable.addMergedType(SymbolTable.java:1202)
 * at org.objectweb.asm.Frame.merge(Frame.java:1299)
 * at org.objectweb.asm.Frame.merge(Frame.java:1197)
 * at org.objectweb.asm.MethodWriter.computeAllFrames(MethodWriter.java:1610)
 * at org.objectweb.asm.MethodWriter.visitMaxs(MethodWriter.java:1546)
 * at org.objectweb.asm.tree.MethodNode.accept(MethodNode.java:769)
 * at org.objectweb.asm.util.CheckMethodAdapter$1.visitEnd(CheckMethodAdapter.java:465)
 * at org.objectweb.asm.MethodVisitor.visitEnd(MethodVisitor.java:783)
 * at org.objectweb.asm.util.CheckMethodAdapter.visitEnd(CheckMethodAdapter.java:1036)
 * at org.objectweb.asm.ClassReader.readMethod(ClassReader.java:1495)
 * at org.objectweb.asm.ClassReader.accept(ClassReader.java:721)
 *
 */
class TraceClassWriter : ClassWriter {
    private var mClassLoader: ClassLoader?

    internal constructor(flags: Int, classLoader: ClassLoader) : super(flags) {
        mClassLoader = classLoader
    }

    internal constructor(classReader: ClassReader?, flags: Int, classLoader: ClassLoader?) : super(classReader, flags) {
        mClassLoader = classLoader
    }

    override fun getCommonSuperClass(type1: String, type2: String): String {
        return try {
            super.getCommonSuperClass(type1, type2)
        } catch (e: Exception) {
            println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
            e.printStackTrace()
            "java/lang/Object"
            //            try {
//                return getCommonSuperClassV1(type1, type2);
//            } catch (Exception e1) {
//                try {
//                    return getCommonSuperClassV2(type1, type2);
//                } catch (Exception e2) {
//                    return getCommonSuperClassV3(type1, type2);
//                }
//            }
        }
    }

    private fun getCommonSuperClassV1(type1: String, type2: String): String {
        val curClassLoader = javaClass.classLoader
        var clazz1: Class<*>
        val clazz2: Class<*>
        try {
            clazz1 = Class.forName(type1.replace('/', '.'), false, mClassLoader)
            clazz2 = Class.forName(type2.replace('/', '.'), false, mClassLoader)
        } catch (e: Exception) {/*throw new RuntimeException(e.toString());*//*e.printStackTrace();*/
            return "java/lang/Object"
        } catch (error: LinkageError) {
            return "java/lang/Object"
        }
        if (clazz1.isAssignableFrom(clazz2)) {
            return type1
        }
        if (clazz2.isAssignableFrom(clazz1)) {
            return type2
        }
        return if (clazz1.isInterface || clazz2.isInterface) {
            "java/lang/Object"
        } else {
            do {
                clazz1 = clazz1.superclass
            } while (!clazz1.isAssignableFrom(clazz2))
            clazz1.name.replace('.', '/')
        }
    }

    private fun getCommonSuperClassV2(type1: String, type2: String): String {
        val curClassLoader = javaClass.classLoader
        var clazz1: Class<*>
        val clazz2: Class<*>
        try {
            clazz1 = Class.forName(type1.replace('/', '.'), false, curClassLoader)
            clazz2 = Class.forName(type2.replace('/', '.'), false, mClassLoader)
        } catch (e: Exception) {/*throw new RuntimeException(e.toString());*//*e.printStackTrace();*/
            return "java/lang/Object"
        } catch (error: LinkageError) {
            return "java/lang/Object"
        }
        if (clazz1.isAssignableFrom(clazz2)) {
            return type1
        }
        if (clazz2.isAssignableFrom(clazz1)) {
            return type2
        }
        return if (clazz1.isInterface || clazz2.isInterface) {
            "java/lang/Object"
        } else {
            do {
                clazz1 = clazz1.superclass
            } while (!clazz1.isAssignableFrom(clazz2))
            clazz1.name.replace('.', '/')
        }
    }

    private fun getCommonSuperClassV3(type1: String, type2: String): String {
        val curClassLoader = javaClass.classLoader
        var clazz1: Class<*>
        val clazz2: Class<*>
        try {
            clazz1 = Class.forName(type1.replace('/', '.'), false, mClassLoader)
            clazz2 = Class.forName(type2.replace('/', '.'), false, curClassLoader)
        } catch (e: Exception) {/*e.printStackTrace();*//*throw new RuntimeException(e.toString());*/
            return "java/lang/Object"
        } catch (error: LinkageError) {
            return "java/lang/Object"
        }
        if (clazz1.isAssignableFrom(clazz2)) {
            return type1
        }
        if (clazz2.isAssignableFrom(clazz1)) {
            return type2
        }
        return if (clazz1.isInterface || clazz2.isInterface) {
            "java/lang/Object"
        } else {
            do {
                clazz1 = clazz1.superclass
            } while (!clazz1.isAssignableFrom(clazz2))
            clazz1.name.replace('.', '/')
        }
    }
}