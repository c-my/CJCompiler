package com.compiler.IntermediateCodeGeneration;

import com.compiler.parser.Tables;
import com.compiler.parser.TypeLabelItem;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class TraverSymbolTable implements Opcodes {

    public void traverSYMBOL(MethodVisitor mv){
        for(var table : Tables.SymbolTable){
            String Sname = new String(table.getName());//符号表其中一项的名字
            int Sval = Tables.getIntValue(Sname);
            int Count = Tables.getSymbolCount(Sname, TypeLabelItem.type.INTEGER);
            mv.visitIntInsn(SIPUSH, Sval);
            mv.visitVarInsn(ISTORE, Count);
        }
    }

}
