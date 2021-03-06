package com.compiler.IntermediateCodeGeneration;

import com.compiler.parser.Quaternary;
import com.compiler.parser.Tables;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;

public class BytecodeGenerator implements Opcodes {
    public Label labelif = new Label();
    public Label labelelse = new Label();
    public Label labelend = new Label();
    public Label labelwhile  = new Label();
    public Label labelEND_WHILE = new Label();
    public Label labelEQwhile = new Label();
    public Label labelEQEND_WHILE = new Label();

    static public ArrayList<Label> labelListWhile = new ArrayList<>();
    static public ArrayList<Label> labelListWhileEnd = new ArrayList<>();
    static public int labelIndex = -1;
    static public ArrayList<Label> labelListIF = new ArrayList<>();
    static public ArrayList<Label> lalelListELSE = new ArrayList<>();
    static public ArrayList<Label> lalelListEIFEND= new ArrayList<>();
    static public int labelIfIndex = 0;


    public byte[] generateBytecode() throws Exception {
        ComputClass c = new ComputClass(new Quaternary());
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        //ClassWriter cw = new ClassWriter(0);
        //ClassWriter cw = new (ClassWriter.COMPUTE_MAXS);
        MethodVisitor mv;
        //版本 ,类型, 名字, signature,父类, 接口
        //创建一个共有类MyClass
        cw.visit(52, ACC_PUBLIC + ACC_SUPER, "lexer", null, "java/lang/Object", null);
        {

            //声明 static void main
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
            //遍历四元式
            mv.visitCode();
            System.out.println(Tables.quaternaryList + "!!!");
            TraverSymbolTable a = new TraverSymbolTable();
            a.traverSYMBOL(mv);
            final int maxStack = 1000;

            for (Quaternary quaternary : Tables.quaternaryList) {
                int currentIndex = Tables.quaternaryList.indexOf(quaternary);
                Quaternary nextQuaternary;
                if(currentIndex!= Tables.quaternaryList.size()-1)
                    nextQuaternary = Tables.quaternaryList.get(Tables.quaternaryList.indexOf(quaternary)+1);
                else{
                    nextQuaternary = new Quaternary("","","","");
                }
                c.comput(mv,quaternary,nextQuaternary);
                System.out.println("循环四元式完成");
            }
            mv.visitInsn(RETURN); //add return instruction
            mv.visitMaxs(maxStack, 0);
            mv.visitEnd();
        }
        cw.visitEnd();
        return cw.toByteArray();
    }
}
