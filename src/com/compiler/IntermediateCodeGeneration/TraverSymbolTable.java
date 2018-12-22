package com.compiler.IntermediateCodeGeneration;

import com.compiler.parser.Tables;
import com.compiler.parser.TypeLabelItem;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class TraverSymbolTable implements Opcodes {

    public void traverSYMBOL(MethodVisitor mv){
        for(var table : Tables.SymbolTable){
            String Sname = new String(table.getName());//符号表其中一项的名字
            int Count;
            if(Tables.getDoubleValue(Sname) != Double.MAX_VALUE){
                var Sval = Tables.getDoubleValue(Sname);
                Count = Tables.getSymbolCount(Sname, TypeLabelItem.type.FLOAT);
                System.out.println(Sname + " " + Sval + " " + Count);
                mv.visitLdcInsn(Sval);
                mv.visitVarInsn(DSTORE, Count * 2);
            }
            else if(Tables.getIntValue(Sname) != Integer.MAX_VALUE){
                var Sval = Tables.getIntValue(Sname);
                Count = Tables.getSymbolCount(Sname, TypeLabelItem.type.INTEGER);
                System.out.println(Sname + " " + Sval + " " + Count);
                mv.visitIntInsn(SIPUSH, Sval);
                mv.visitVarInsn(ISTORE, Count);
            }
            else if(Tables.getCharValue(Sname) != ' '){
                var Sval =Integer.valueOf(Tables.getCharValue(Sname));
                Count = Tables.getSymbolCount(Sname, TypeLabelItem.type.CHAR);
                System.out.println(Sname + " " + Sval + " " + Count);
                mv.visitIntInsn(SIPUSH, Sval);
                //mv.visitLdcInsn(Sval);
                mv.visitVarInsn(ISTORE, Count);
            }
        }
    }

}
