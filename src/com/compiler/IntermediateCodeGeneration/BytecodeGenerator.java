package com.compiler.IntermediateCodeGeneration;

import com.compiler.parser.Quaternary;
import com.compiler.parser.Tables;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeGenerator implements Opcodes {
    public byte[] generateBytecode() throws Exception {

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        MethodVisitor mv;
        //版本 ,类型, 名字, signature,父类, 接口
        //创建一个共有类MyClass
        cw.visit(52, ACC_PUBLIC + ACC_SUPER, "lexer", null, "java/lang/Object", null);
        {
            //声明 static void main
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
            //遍历四元式
            System.out.println(Tables.quaternaryList + "!!!");
            for (Quaternary quaternary : Tables.quaternaryList) {
                ComputClass c = new ComputClass(new Quaternary());
                c.comput(mv,quaternary);
                System.out.println("循环四元式完成");
            }
            mv.visitInsn(RETURN); //add return instruction
            mv.visitEnd();
        }
        cw.visitEnd();
        return cw.toByteArray();
    }
}
