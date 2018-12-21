package com.compiler.IntermediateCodeGeneration;

import com.compiler.parser.Tables;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class TraverSymbolTable implements Opcodes {

    public void traverSYMBOL(MethodVisitor mv){
        for(var table : Tables.SymbolTable){
            String Sname;//符号表其中一项的名字
            int Sval = 0;
            int Count = 0;
            mv.visitIntInsn(SIPUSH, Sval);
            mv.visitVarInsn(ISTORE, Count);
        }
    }

}
