package com.compiler.IntermediateCodeGeneration;

import com.compiler.parser.Quaternary;
import com.compiler.parser.Tables;
import com.compiler.parser.TypeLabelItem;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


public class ComputClass implements Opcodes {

    Quaternary quaternary;

    public ComputClass(Quaternary quaternary) {
        this.quaternary = quaternary;
    }

    public void comput(MethodVisitor mv , Quaternary quaternary){
        final String opt = new String(quaternary.getOpt());//判断运算符
        if (opt.equals("=")) {
            //System.out.println("等于完成");
            switch (Tables.getType(quaternary.getOpds().get(0))){

                case INTEGER:
                    //System.out.println("等于完成");
                    int val0 = Tables.getIntValue(quaternary.getOpds().get(0));
                    //System.out.println("val0" + val0);
                    //int val0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.INTEGER);
                    //System.out.println(val0);
                    int val2 = Tables.getSymbolCount(quaternary.getOpds().get(2), TypeLabelItem.type.INTEGER);
                    //mv.visitCode();
                    //mv.visitIntInsn(SIPUSH, 10);
                    //mv.visitVarInsn(ISTORE, 0);
                    mv.visitIntInsn(SIPUSH, val0);
                    mv.visitVarInsn(ISTORE, val2);
                    System.out.println("等于完成");
                    break;
                case FLOAT:
                    Double dou0 = Tables.getDoubleValue(quaternary.getOpds().get(0));
                    //int dou0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.FLOAT);
                    int dou2 = Tables.getSymbolCount(quaternary.getOpds().get(2), TypeLabelItem.type.FLOAT);
                    mv.visitLdcInsn("asd");
                    mv.visitVarInsn(ASTORE,dou2);
                    break;
                case NULL:
                    int val = Tables.getSymbolCount(quaternary.getOpds().get(2), TypeLabelItem.type.INTEGER);
                    mv.visitVarInsn(ISTORE, val);
                    break;
                }

        }
        else if(opt.equals("+")){
            switch (Tables.getType(quaternary.getOpds().get(0))){
                case INTEGER:
                    int val0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.INTEGER);
                    //System.out.println(val0);
                    int val1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                    int val2 = Tables.getSymbolCount(quaternary.getOpds().get(2), TypeLabelItem.type.INTEGER);
                    mv.visitVarInsn(ILOAD, val0);
                    mv.visitVarInsn(ILOAD, val1);
                    mv.visitInsn(IADD);
                    System.out.println("加法完成");
                    break;
                case FLOAT:
                    int dou0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.FLOAT);
                    int dou1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.FLOAT);
                    int dou2 = Tables.getSymbolCount(quaternary.getOpds().get(2), TypeLabelItem.type.FLOAT);
                    mv.visitVarInsn(DLOAD, dou0);
                    mv.visitVarInsn(DLOAD, dou1);
                    mv.visitInsn(FADD);
                    mv.visitVarInsn(FSTORE,dou2);
                    break;
            }

        }
        else if(opt.equals("PRINT")){
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            switch (Tables.getType(quaternary.getOpds().get(2))){
                case INTEGER:
                    int dou2 = Tables.getSymbolCount(quaternary.getOpds().get(2), TypeLabelItem.type.INTEGER);
                    mv.visitVarInsn(ILOAD, dou2);
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "print", "(I)V", false);
                    System.out.println("print完成");
                    break;
                case FLOAT:
                    int dou = Tables.getSymbolCount(quaternary.getOpds().get(2), TypeLabelItem.type.FLOAT);
                    mv.visitVarInsn(DLOAD, dou);
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "print", "(I)V", false);
                    break;
            }


        }


    }
}
