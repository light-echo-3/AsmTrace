//package com.wuzhu.asmtrack
//
//import com.android.build.api.transform.Format
//import com.android.build.api.transform.JarInput
//import com.android.build.api.transform.TransformOutputProvider
//import com.android.utils.FileUtils
//import org.apache.commons.codec.digest.DigestUtils
//import org.apache.commons.compress.utils.IOUtils
//import org.objectweb.asm.ClassReader
//import org.objectweb.asm.ClassVisitor
//import org.objectweb.asm.ClassWriter
//import org.objectweb.asm.Opcodes
//
//import java.util.jar.JarEntry
//import java.util.jar.JarFile
//import java.util.jar.JarOutputStream
//import java.util.zip.ZipEntry
//
//class ASMTransformBusiness {
//
//
//    static void traceJarFiles(JarInput jarInput, TransformOutputProvider outputProvider, Config traceConfig) {
//        if (jarInput.file.getAbsolutePath().endsWith(".jar")) {
//            //重命名输出文件,因为可能同名,会覆盖
//            def jarName = jarInput.name
//            def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
//            if (jarName.endsWith(".jar")) {
//                jarName = jarName.substring(0, jarName.length() - 4)
//            }
//            JarFile jarFile = new JarFile(jarInput.file)
//            Enumeration enumeration = jarFile.entries()
//
//            File tmpFile = new File(jarInput.file.getParent() + File.separator + "classes_temp.jar")
//            if (tmpFile.exists()) {
//                tmpFile.delete()
//            }
//
//            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(tmpFile))
//
//            //循环jar包里的文件
//            while (enumeration.hasMoreElements()) {
//                JarEntry jarEntry = (JarEntry) enumeration.nextElement()
//                String entryName = jarEntry.getName()
//                ZipEntry zipEntry = new ZipEntry(entryName)
//                InputStream inputStream = jarFile.getInputStream(jarEntry)
//                if (traceConfig.isNeedTraceClass(entryName)) {
//                    jarOutputStream.putNextEntry(zipEntry)
//                    ClassReader classReader = new ClassReader(IOUtils.toByteArray(inputStream))
//                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
//                    ClassVisitor cv = new ScanClassVisitor(Opcodes.ASM5, classWriter)
//                    classReader.accept(cv, ClassReader.EXPAND_FRAMES)
//                    byte[] code = classWriter.toByteArray()
//                    jarOutputStream.write(code)
//                } else {
//                    jarOutputStream.putNextEntry(zipEntry)
//                    jarOutputStream.write(IOUtils.toByteArray(inputStream))
//                }
//                jarOutputStream.closeEntry()
//            }
//
//            jarOutputStream.close()
//            jarFile.close()
//
//            //处理完输出给下一任务作为输入
//            def dest = outputProvider.getContentLocation(jarName + md5Name,
//                    jarInput.contentTypes, jarInput.scopes, Format.JAR)
//            FileUtils.copyFile(tmpFile, dest)
//
//            tmpFile.delete()
//        }
//    }
//}