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

    public void comput(MethodVisitor mv){
        final String opt = quaternary.getOpt();//判断运算符
        if (opt == "=") {
            switch (Tables.getType(quaternary.getOpds().get(0))){
                case INTEGER:
                    int val0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.INTEGER);
                    int val2 = Tables.getSymbolCount(quaternary.getOpds().get(2), TypeLabelItem.type.INTEGER);
                    mv.visitIntInsn(BIPUSH, val0);
                    mv.visitVarInsn(ISTORE, val2);
                    break;
                    case FLOAT:
                        int dou0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.FLOAT);
                        int dou2 = Tables.getSymbolCount(quaternary.getOpds().get(2), TypeLabelItem.type.FLOAT);
                        mv.visitVarInsn(FLOAD, dou0);
                        mv.visitVarInsn(FSTORE,dou2);
                        break;
                }

        }
        else if(opt == "+"){
            switch (Tables.getType(quaternary.getOpds().get(0))){
                case INTEGER:
                    int val0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.INTEGER);
                    int val1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                    int val2 = Tables.getSymbolCount(quaternary.getOpds().get(2), TypeLabelItem.type.INTEGER);
                    mv.visitVarInsn(ILOAD, val0);
                    mv.visitVarInsn(ILOAD, val1);
                    mv.visitInsn(IADD);
                    mv.visitVarInsn(ISTORE, val2);
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

    }
}
