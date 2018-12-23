package com.compiler.IntermediateCodeGeneration;

import com.compiler.parser.Quaternary;
import com.compiler.parser.Tables;
import com.compiler.parser.TypeLabelItem;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


public class ComputClass implements Opcodes {

    Quaternary quaternary;
    BytecodeGenerator bet = new BytecodeGenerator();
    public ComputClass(Quaternary quaternary) {
        this.quaternary = quaternary;
    }

    public void comput(MethodVisitor mv , Quaternary quaternary, Quaternary next){
        final String opt = new String(quaternary.getOpt());//判断运算符
        if (opt.equals("=")) {//进行赋值运算
            switch (Tables.getType(quaternary.getOpds().get(0))){
                case INTEGER://整形的赋值运算
                    int val0 = Tables.getIntValue(quaternary.getOpds().get(0));//第一个算符的值
                    int val2 = Tables.getSymbolCount(quaternary.getOpds().get(2), TypeLabelItem.type.INTEGER);//第三个算符在本地变量中的位置
                    mv.visitIntInsn(SIPUSH, val0);//推送至栈顶
                    mv.visitVarInsn(ISTORE, val2);//栈顶值赋值
                    break;
                case FLOAT://浮点，double赋值
                    Double dou0 = Tables.getDoubleValue(quaternary.getOpds().get(0));//第一个算符的值
                    int dou2 = Tables.getSymbolCount(quaternary.getOpds().get(2), TypeLabelItem.type.FLOAT);
                    mv.visitLdcInsn(dou0);//推送栈顶
                    mv.visitVarInsn(DSTORE,dou2 * 2);//弹栈赋值
                    break;
                case NULL://常数不在符号表中，常数赋值给变量
                    if(Tables.isNumber(quaternary.getOpds().get(0)) == TypeLabelItem.type.INTEGER){
                        //四元式第一个算符是整形常数<=,1, ,a>
                        int po0 = Integer.parseInt(quaternary.getOpds().get(0));//求整形常熟的值
                        int po2 = Tables.getSymbolCount(quaternary.getOpds().get(2), TypeLabelItem.type.INTEGER);//第三个算符在本地变量表中的位置
                        mv.visitIntInsn(SIPUSH, po0);
                        mv.visitVarInsn(ISTORE, po2);
                    }
                    else if(Tables.isNumber(quaternary.getOpds().get(0)) == TypeLabelItem.type.FLOAT){
                        //<=,1.1, ,a>
                        double po0 = Double.parseDouble(quaternary.getOpds().get(0));//求double 形常熟的值
                        int po2 = Tables.getSymbolCount(quaternary.getOpds().get(2), TypeLabelItem.type.FLOAT);//第三个算符在本地变量表中的位置
                        mv.visitLdcInsn(po0);
                        mv.visitVarInsn(DSTORE, po2 * 2);
                    }
                    else {//<=,t1, ,t2>
                        switch (Tables.getType(quaternary.getOpds().get(2))){
                            case INTEGER:
                                int ival = Tables.getSymbolCount(quaternary.getOpds().get(2), TypeLabelItem.type.INTEGER);//直接将栈里的值赋给第三个算符
                                mv.visitVarInsn(ISTORE, ival);
                                break;
                            case FLOAT:
                                int dval = Tables.getSymbolCount(quaternary.getOpds().get(2), TypeLabelItem.type.FLOAT);//直接将栈里的值赋给第三个算符
                                mv.visitVarInsn(DSTORE, dval * 2);
                                break;
                            case CHAR:
                                break;
                        }

                    }
                    break;
                }

        }
        else if(opt.equals("+")){
            switch (Tables.getType(quaternary.getOpds().get(0))){
                case INTEGER:
                    int val0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.INTEGER);
                    int val1 ;
                    switch (Tables.getType(quaternary.getOpds().get(1))){
                        case INTEGER://第二个算符是变量
                           val1 =  Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                            mv.visitVarInsn(ILOAD, val0);
                            mv.visitVarInsn(ILOAD, val1);
                            mv.visitInsn(IADD);
                            break;
                        case NULL://第二个算符是中间变量或常数
                            if(Tables.isNumber(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){
                                val1 = Integer.parseInt(quaternary.getOpds().get(1));
                                mv.visitVarInsn(ILOAD, val0);
                                mv.visitIntInsn(BIPUSH, val1);
                                mv.visitInsn(IADD);
                            }
                            else {
                                mv.visitVarInsn(ILOAD, val0);
                                mv.visitInsn(IADD);
                            }
                            break;
                    }
                    System.out.println("加法完成");
                    break;
                case FLOAT:
                    int Dval0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.FLOAT);//第几个值
                    int Dval1 ;
                    switch (Tables.getType(quaternary.getOpds().get(1))){
                        case FLOAT://第二个算符是变量
                            Dval1 =  Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.FLOAT);
                            mv.visitVarInsn(DLOAD, Dval0 * 2);
                            mv.visitVarInsn(DLOAD, Dval1 * 2);

                            mv.visitInsn(DADD);
                            break;
                        case NULL://第二个算符是中间变量或常数
                            if(Tables.isNumber(quaternary.getOpds().get(1)) == TypeLabelItem.type.FLOAT){
                                //是常量
                                Dval1 = Integer.parseInt(quaternary.getOpds().get(1));
                                mv.visitVarInsn(DLOAD, Dval0);
                                mv.visitLdcInsn(Dval1);
                                mv.visitInsn(DADD);
                            }
                            else {
                                //是中间变量
                                mv.visitVarInsn(DLOAD, Dval0);
                                mv.visitInsn(DADD);
                            }
                            break;
                    }
                    System.out.println("加法完成");
                    break;
                case NULL:
                    if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){//<+,t1,a,t2>
                        int ui1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                        mv.visitVarInsn(ILOAD, ui1);
                        mv.visitInsn(IADD);
                    }
                    else if (Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.FLOAT){
                        int ui1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.FLOAT);
                        mv.visitVarInsn(DLOAD, ui1);
                        mv.visitInsn(DADD);
                    }
                    else if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.NULL){//<+,t1,t2,t3>
                        if(Tables.getType(quaternary.getOpds().get(2)) == TypeLabelItem.type.INTEGER)
                            mv.visitInsn(IADD);
                        else if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.FLOAT)
                            mv.visitInsn(DADD);
                    }
                    break;
            }

        }
        else if(opt.equals("-")){
            switch (Tables.getType(quaternary.getOpds().get(0))){
                case INTEGER:
                    int val0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.INTEGER);
                    int val1 ;
                    switch (Tables.getType(quaternary.getOpds().get(1))){
                        case INTEGER://第二个算符是变量
                            val1 =  Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                            mv.visitVarInsn(ILOAD, val0);
                            mv.visitVarInsn(ILOAD, val1);
                            mv.visitInsn(ISUB);
                            break;
                        case NULL://第二个算符是中间变量或常数
                            if(Tables.isNumber(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){
                                val1 = Integer.parseInt(quaternary.getOpds().get(1));
                                mv.visitVarInsn(ILOAD, val0);
                                mv.visitIntInsn(BIPUSH, val1);
                                mv.visitInsn(ISUB);
                            }
                            else {
                                mv.visitVarInsn(ILOAD, val0);
                                mv.visitInsn(ISUB);
                            }
                            break;
                    }
                    System.out.println("加法完成");
                    break;
                case FLOAT:
                    int Dval0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.FLOAT);//第几个值
                    int Dval1 ;
                    switch (Tables.getType(quaternary.getOpds().get(1))){
                        case FLOAT://第二个算符是变量
                            Dval1 =  Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.FLOAT);
                            mv.visitVarInsn(DLOAD, Dval0 * 2);
                            mv.visitVarInsn(DLOAD, Dval1 * 2);

                            mv.visitInsn(DSUB);
                            break;
                        case NULL://第二个算符是中间变量或常数
                            if(Tables.isNumber(quaternary.getOpds().get(1)) == TypeLabelItem.type.FLOAT){
                                //是常量
                                Dval1 = Integer.parseInt(quaternary.getOpds().get(1));
                                mv.visitVarInsn(DLOAD, Dval0);
                                mv.visitLdcInsn(Dval1);
                                mv.visitInsn(DSUB);
                            }
                            else {
                                //是中间变量
                                mv.visitVarInsn(DLOAD, Dval0);
                                mv.visitInsn(DSUB);
                            }
                            break;
                    }
                    System.out.println("加法完成");
                    break;
                case NULL:
                    if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){//<+,t1,a,t2>
                        int ui1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                        mv.visitVarInsn(ILOAD, ui1);
                        mv.visitInsn(ISUB);
                    }
                    else if (Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.FLOAT){
                        int ui1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.FLOAT);
                        mv.visitVarInsn(DLOAD, ui1);
                        mv.visitInsn(DSUB);
                    }
                    else if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.NULL){//<+,t1,t2,t3>
                        if(Tables.getType(quaternary.getOpds().get(2)) == TypeLabelItem.type.INTEGER)
                            mv.visitInsn(ISUB);
                        else if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.FLOAT)
                            mv.visitInsn(DSUB);
                    }
                    break;
            }
        }
        else if(opt.equals("*")){
            switch (Tables.getType(quaternary.getOpds().get(0))){
                case INTEGER:
                    int val0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.INTEGER);
                    int val1 ;
                    switch (Tables.getType(quaternary.getOpds().get(1))){
                        case INTEGER://第二个算符是变量
                            val1 =  Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                            mv.visitVarInsn(ILOAD, val0);
                            mv.visitVarInsn(ILOAD, val1);
                            mv.visitInsn(IMUL);
                            break;
                        case NULL://第二个算符是中间变量或常数
                            if(Tables.isNumber(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){
                                val1 = Integer.parseInt(quaternary.getOpds().get(1));
                                mv.visitVarInsn(ILOAD, val0);
                                mv.visitIntInsn(BIPUSH, val1);
                                mv.visitInsn(IMUL);
                            }
                            else {
                                mv.visitVarInsn(ILOAD, val0);
                                mv.visitInsn(IMUL);
                            }
                            break;
                    }
                    System.out.println("加法完成");
                    break;
                case FLOAT:
                    int Dval0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.FLOAT);//第几个值
                    int Dval1 ;
                    switch (Tables.getType(quaternary.getOpds().get(1))){
                        case FLOAT://第二个算符是变量
                            Dval1 =  Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.FLOAT);
                            mv.visitVarInsn(DLOAD, Dval0 * 2);
                            mv.visitVarInsn(DLOAD, Dval1 * 2);

                            mv.visitInsn(DMUL);
                            break;
                        case NULL://第二个算符是中间变量或常数
                            if(Tables.isNumber(quaternary.getOpds().get(1)) == TypeLabelItem.type.FLOAT){
                                //是常量
                                Dval1 = Integer.parseInt(quaternary.getOpds().get(1));
                                mv.visitVarInsn(DLOAD, Dval0);
                                mv.visitLdcInsn(Dval1);
                                mv.visitInsn(DMUL);
                            }
                            else {
                                //是中间变量
                                mv.visitVarInsn(DLOAD, Dval0);
                                mv.visitInsn(DMUL);
                            }
                            break;
                    }
                    System.out.println("加法完成");
                    break;
                case NULL:
                    if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){//<+,t1,a,t2>
                        int ui1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                        mv.visitVarInsn(ILOAD, ui1);
                        mv.visitInsn(IMUL);
                    }
                    else if (Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.FLOAT){
                        int ui1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.FLOAT);
                        mv.visitVarInsn(DLOAD, ui1);
                        mv.visitInsn(DMUL);
                    }
                    else if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.NULL){//<+,t1,t2,t3>
                        if(Tables.getType(quaternary.getOpds().get(2)) == TypeLabelItem.type.INTEGER)
                            mv.visitInsn(IMUL);
                        else if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.FLOAT)
                            mv.visitInsn(DMUL);
                    }
                    break;
            }
        }
        else if(opt.equals("/")){
            switch (Tables.getType(quaternary.getOpds().get(0))){
                case INTEGER:
                    int val0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.INTEGER);
                    int val1 ;
                    switch (Tables.getType(quaternary.getOpds().get(1))){
                        case INTEGER://第二个算符是变量
                            val1 =  Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                            mv.visitVarInsn(ILOAD, val0);
                            mv.visitVarInsn(ILOAD, val1);
                            mv.visitInsn(IDIV);
                            break;
                        case NULL://第二个算符是中间变量或常数
                            if(Tables.isNumber(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){
                                val1 = Integer.parseInt(quaternary.getOpds().get(1));
                                mv.visitVarInsn(ILOAD, val0);
                                mv.visitIntInsn(BIPUSH, val1);
                                mv.visitInsn(IDIV);
                            }
                            else {
                                mv.visitVarInsn(ILOAD, val0);
                                mv.visitInsn(IDIV);
                            }
                            break;
                    }
                    System.out.println("加法完成");
                    break;
                case FLOAT:
                    int Dval0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.FLOAT);//第几个值
                    int Dval1 ;
                    switch (Tables.getType(quaternary.getOpds().get(1))){
                        case FLOAT://第二个算符是变量
                            Dval1 =  Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.FLOAT);
                            mv.visitVarInsn(DLOAD, Dval0 * 2);
                            mv.visitVarInsn(DLOAD, Dval1 * 2);

                            mv.visitInsn(DDIV);
                            break;
                        case NULL://第二个算符是中间变量或常数
                            if(Tables.isNumber(quaternary.getOpds().get(1)) == TypeLabelItem.type.FLOAT){
                                //是常量
                                Dval1 = Integer.parseInt(quaternary.getOpds().get(1));
                                mv.visitVarInsn(DLOAD, Dval0);
                                mv.visitLdcInsn(Dval1);
                                mv.visitInsn(DDIV);
                            }
                            else {
                                //是中间变量
                                mv.visitVarInsn(DLOAD, Dval0);
                                mv.visitInsn(DDIV);
                            }
                            break;
                    }
                    System.out.println("加法完成");
                    break;
                case NULL:
                    if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){//<+,t1,a,t2>
                        int ui1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                        mv.visitVarInsn(ILOAD, ui1);
                        mv.visitInsn(IDIV);
                    }
                    else if (Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.FLOAT){
                        int ui1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.FLOAT);
                        mv.visitVarInsn(DLOAD, ui1);
                        mv.visitInsn(DDIV);
                    }
                    else if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.NULL){//<+,t1,t2,t3>
                        if(Tables.getType(quaternary.getOpds().get(2)) == TypeLabelItem.type.INTEGER)
                            mv.visitInsn(IDIV);
                        else if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.FLOAT)
                            mv.visitInsn(DDIV);
                    }
                    break;
            }
        }
        else if(opt.equals("<<")){
            switch (Tables.getType(quaternary.getOpds().get(0))){
                case INTEGER:
                    int val0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.INTEGER);
                    int val1 ;
                    switch (Tables.getType(quaternary.getOpds().get(1))){
                        case INTEGER://第二个算符是变量
                            val1 =  Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                            mv.visitVarInsn(ILOAD, val0);
                            mv.visitVarInsn(ILOAD, val1);
                            mv.visitInsn(ISHL);
                            break;
                        case NULL://第二个算符是中间变量或常数
                            if(Tables.isNumber(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){
                                val1 = Integer.parseInt(quaternary.getOpds().get(1));
                                mv.visitVarInsn(ILOAD, val0);
                                mv.visitIntInsn(BIPUSH, val1);
                                mv.visitInsn(ISHL);
                            }
                            else {
                                mv.visitVarInsn(ILOAD, val0);
                                mv.visitInsn(ISHL);
                            }
                            break;
                    }
                    System.out.println("加法完成");
                    break;

                case NULL:
                    if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){//<+,t1,a,t2>
                        int ui1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                        mv.visitVarInsn(ILOAD, ui1);
                        mv.visitInsn(ISHL);
                    }
                    else if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.NULL){//<+,t1,t2,t3>
                        if(Tables.getType(quaternary.getOpds().get(2)) == TypeLabelItem.type.INTEGER)
                            mv.visitInsn(ISHL);

                    }
                    break;
            }
        }
        else if(opt.equals(">>")){
            switch (Tables.getType(quaternary.getOpds().get(0))){
                case INTEGER:
                    int val0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.INTEGER);
                    int val1 ;
                    switch (Tables.getType(quaternary.getOpds().get(1))){
                        case INTEGER://第二个算符是变量
                            val1 =  Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                            mv.visitVarInsn(ILOAD, val0);
                            mv.visitVarInsn(ILOAD, val1);
                            mv.visitInsn(ISHR);
                            break;
                        case NULL://第二个算符是中间变量或常数
                            if(Tables.isNumber(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){
                                val1 = Integer.parseInt(quaternary.getOpds().get(1));
                                mv.visitVarInsn(ILOAD, val0);
                                mv.visitIntInsn(BIPUSH, val1);
                                mv.visitInsn(ISHR);
                            }
                            else {
                                mv.visitVarInsn(ILOAD, val0);
                                mv.visitInsn(ISHR);
                            }
                            break;
                    }
                    System.out.println("加法完成");
                    break;

                case NULL:
                    if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){//<+,t1,a,t2>
                        int ui1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                        mv.visitVarInsn(ILOAD, ui1);
                        mv.visitInsn(ISHR);
                    }
                    else if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.NULL){//<+,t1,t2,t3>
                        if(Tables.getType(quaternary.getOpds().get(2)) == TypeLabelItem.type.INTEGER)
                            mv.visitInsn(ISHR);

                    }
                    break;
            }
        }
        else if(opt.equals("&")){
            switch (Tables.getType(quaternary.getOpds().get(0))){
                case INTEGER:
                    int val0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.INTEGER);
                    int val1 ;
                    switch (Tables.getType(quaternary.getOpds().get(1))){
                        case INTEGER://第二个算符是变量
                            val1 =  Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                            mv.visitVarInsn(ILOAD, val0);
                            mv.visitVarInsn(ILOAD, val1);
                            mv.visitInsn(IAND);
                            break;
                        case NULL://第二个算符是中间变量或常数
                            if(Tables.isNumber(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){
                                val1 = Integer.parseInt(quaternary.getOpds().get(1));
                                mv.visitVarInsn(ILOAD, val0);
                                mv.visitIntInsn(BIPUSH, val1);
                                mv.visitInsn(IAND);
                            }
                            else {
                                mv.visitVarInsn(ILOAD, val0);
                                mv.visitInsn(IAND);
                            }
                            break;
                    }
                    System.out.println("加法完成");
                    break;

                case NULL:
                    if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){//<+,t1,a,t2>
                        int ui1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                        mv.visitVarInsn(ILOAD, ui1);
                        mv.visitInsn(IAND);
                    }
                    else if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.NULL){//<+,t1,t2,t3>
                        if(Tables.getType(quaternary.getOpds().get(2)) == TypeLabelItem.type.INTEGER)
                            mv.visitInsn(IAND);

                    }
                    break;
            }
        }
        else if(opt.equals("|")){
            switch (Tables.getType(quaternary.getOpds().get(0))){
                case INTEGER:
                    int val0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.INTEGER);
                    int val1 ;
                    switch (Tables.getType(quaternary.getOpds().get(1))){
                        case INTEGER://第二个算符是变量
                            val1 =  Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                            mv.visitVarInsn(ILOAD, val0);
                            mv.visitVarInsn(ILOAD, val1);
                            mv.visitInsn(IOR);
                            break;
                        case NULL://第二个算符是中间变量或常数
                            if(Tables.isNumber(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){
                                val1 = Integer.parseInt(quaternary.getOpds().get(1));
                                mv.visitVarInsn(ILOAD, val0);
                                mv.visitIntInsn(BIPUSH, val1);
                                mv.visitInsn(IOR);
                            }
                            else {
                                mv.visitVarInsn(ILOAD, val0);
                                mv.visitInsn(IOR);
                            }
                            break;
                    }
                    System.out.println("加法完成");
                    break;

                case NULL:
                    if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){//<+,t1,a,t2>
                        int ui1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                        mv.visitVarInsn(ILOAD, ui1);
                        mv.visitInsn(IOR);
                    }
                    else if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.NULL){//<+,t1,t2,t3>
                        if(Tables.getType(quaternary.getOpds().get(2)) == TypeLabelItem.type.INTEGER)
                            mv.visitInsn(IOR);

                    }
                    break;
            }
        }
        else if(opt.equals("^")){
            switch (Tables.getType(quaternary.getOpds().get(0))){
                case INTEGER:
                    int val0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.INTEGER);
                    int val1 ;
                    switch (Tables.getType(quaternary.getOpds().get(1))){
                        case INTEGER://第二个算符是变量
                            val1 =  Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                            mv.visitVarInsn(ILOAD, val0);
                            mv.visitVarInsn(ILOAD, val1);
                            mv.visitInsn(IXOR);
                            break;
                        case NULL://第二个算符是中间变量或常数
                            if(Tables.isNumber(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){
                                val1 = Integer.parseInt(quaternary.getOpds().get(1));
                                mv.visitVarInsn(ILOAD, val0);
                                mv.visitIntInsn(BIPUSH, val1);
                                mv.visitInsn(IXOR);
                            }
                            else {
                                mv.visitVarInsn(ILOAD, val0);
                                mv.visitInsn(IXOR);
                            }
                            break;
                    }
                    System.out.println("加法完成");
                    break;

                case NULL:
                    if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){//<+,t1,a,t2>
                        int ui1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                        mv.visitVarInsn(ILOAD, ui1);
                        mv.visitInsn(IXOR);
                    }
                    else if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.NULL){//<+,t1,t2,t3>
                        if(Tables.getType(quaternary.getOpds().get(2)) == TypeLabelItem.type.INTEGER)
                            mv.visitInsn(IXOR);

                    }
                    break;
            }
        }
        else if(opt.equals("PRINT")){//print打印实现
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");//构造打印方法
            switch (Tables.getType(quaternary.getOpds().get(2))){
                case INTEGER:
                    int dou2 = Tables.getSymbolCount(quaternary.getOpds().get(2), TypeLabelItem.type.INTEGER);
                    mv.visitVarInsn(ILOAD, dou2);
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
                    System.out.println("print完成");
                    break;
                case FLOAT:
                    int dou = Tables.getSymbolCount(quaternary.getOpds().get(2), TypeLabelItem.type.FLOAT);
                    mv.visitVarInsn(DLOAD, dou * 2);
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(D)V", false);
                    break;
                case CHAR:
                    int Cdou = Tables.getSymbolCount(quaternary.getOpds().get(2), TypeLabelItem.type.CHAR);
                    mv.visitVarInsn(ILOAD, Cdou);
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(C)V", false);
                    break;
            }
        }
        else if(opt.equals("EQ_OP")){
            if(next.getOpt().toLowerCase().equals("while") || next.getOpt().toLowerCase().equals("if"))
            {
                BytecodeGenerator.labelListWhile.add(new Label());
                BytecodeGenerator.labelListWhileEnd.add(new Label());
                BytecodeGenerator.labelIndex++;
                //mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
            }
            if(next.getOpt().toLowerCase().equals("if")){
                BytecodeGenerator.labelListIF.add(new Label());
                BytecodeGenerator.lalelListELSE.add(new Label());
                BytecodeGenerator.lalelListEIFEND.add(new Label());
                BytecodeGenerator.labelIfIndex ++ ;
            }
            //if_else 跳转语句if（a==b）类型
            System.out.println("equal");
            int dou0,dou1;//记录四元式第一个第二个操作符的值或者位置
            switch (Tables.getType(quaternary.getOpds().get(0))){
                case INTEGER:
                    dou0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.INTEGER);
                    switch (Tables.getType(quaternary.getOpds().get(1))){
                        case NULL:
                            if(Tables.isNumber(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){
                                //第二个操作数是整形常数
                                dou1 = Integer.parseInt(quaternary.getOpds().get(1));
                                Label label2 = new Label();
                                Label label3 = new Label();
                                mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                                mv.visitVarInsn(ILOAD, dou0);//将第一个操作数压栈
                                mv.visitIntInsn(SIPUSH, dou1);//第二个操作数压栈
                                mv.visitJumpInsn(IF_ICMPNE,label2);//两操作数相等继续执行，否则跳到label2处
                                mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                                mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                                mv.visitLabel(label2);//label2在这里
                                mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                                mv.visitLabel(label3);//label3在这里
                            }
                            else{
                                //第二个变量是临时变量
                                Label label2 = new Label();
                                Label label3 = new Label();
                                mv.visitVarInsn(ISTORE,1);
                                mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                                mv.visitVarInsn(ILOAD, dou0);//将第一个操作数压栈
                                mv.visitVarInsn(ILOAD,1);
                                //临时变量之前就已经在数据栈内，不需要压栈
                                mv.visitJumpInsn(IF_ICMPNE,label2);//两操作数相等继续执行，否则跳到label2处
                                mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                                mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                                mv.visitLabel(label2);//label2在这里
                                mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                                mv.visitLabel(label3);//label3在这里
                            }
                            break;
                        case INTEGER:
                            dou1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                            Label label2 = new Label();
                            Label label3 = new Label();
                            mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                            mv.visitVarInsn(ILOAD, dou0);//将第一个操作数压栈
                            mv.visitVarInsn(ILOAD, dou1);//第二个操作数压栈
                            mv.visitJumpInsn(IF_ICMPNE,label2);//两操作数相等继续执行，否则跳到label2处
                            mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                            mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                            mv.visitLabel(label2);//label2在这里
                            mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                            mv.visitLabel(label3);//label3在这里
                            break;
                    }

                case FLOAT://还没写
                    break;
                case NULL://临时变量
                    //第一个变量是临时变量
                    mv.visitVarInsn(ISTORE, 0);
                    if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.NULL){
                        //第二个变量也是临时变量或者常数
                        if(Tables.isNumber(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){
                            //第二个是常量
                            dou1 = Integer.parseInt(quaternary.getOpds().get(1));
                            Label label2 = new Label();
                            Label label3 = new Label();
                            mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                            mv.visitVarInsn(ILOAD, 1);
                            mv.visitIntInsn(SIPUSH,dou1);
                            mv.visitJumpInsn(IF_ICMPNE,label2);//两操作数相等继续执行，否则跳到label2处
                            mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                            mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                            mv.visitLabel(label2);//label2在这里
                            mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                            mv.visitLabel(label3);//label3在这里
                        }
                        else{
                            //第二个是临时变量
                            Label label2 = new Label();
                            Label label3 = new Label();
                            mv.visitVarInsn(ISTORE, 1);
                            mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                            mv.visitVarInsn(ILOAD,0);
                            mv.visitVarInsn(ILOAD,1);
                            //临时变量之前就已经在数据栈内，不需要压栈
                            mv.visitJumpInsn(IF_ICMPNE,label2);//两操作数相等继续执行，否则跳到label2处
                            mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                            mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                            mv.visitLabel(label2);//label2在这里
                            mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                            mv.visitLabel(label3);//label3在这里
                        }

                    }
                    else if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){
                        //第二个是整形变量
                        dou1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                        Label label2 = new Label();
                        Label label3 = new Label();
                        mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                        mv.visitVarInsn(ILOAD,100);
                        mv.visitVarInsn(ILOAD,dou1);
                        mv.visitJumpInsn(IF_ICMPNE,label2);//两操作数相等继续执行，否则跳到label2处
                        mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                        mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                        mv.visitLabel(label2);//label2在这里
                        mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                        mv.visitLabel(label3);//label3在这里

                    }
                    else if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.FLOAT){
                        //第二个是浮点变量
                    }
                    break;
            }
        }
        else if(opt.equals("NE_OP")){
            if(next.getOpt().toLowerCase().equals("while") || next.getOpt().toLowerCase().equals("if"))
            {
                BytecodeGenerator.labelListWhile.add(new Label());
                BytecodeGenerator.labelListWhileEnd.add(new Label());
                BytecodeGenerator.labelIndex++;
                //mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
            }
            if(next.getOpt().toLowerCase().equals("if")){
                BytecodeGenerator.labelListIF.add(new Label());
                BytecodeGenerator.lalelListELSE.add(new Label());
                BytecodeGenerator.lalelListEIFEND.add(new Label());
                BytecodeGenerator.labelIfIndex ++ ;
            }
            //if_else 跳转语句if（a==b）类型
            System.out.println("equal");
            int dou0,dou1;//记录四元式第一个第二个操作符的值或者位置
            switch (Tables.getType(quaternary.getOpds().get(0))){
                case INTEGER:
                    dou0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.INTEGER);
                    switch (Tables.getType(quaternary.getOpds().get(1))){
                        case NULL:
                            if(Tables.isNumber(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){
                                //第二个操作数是整形常数
                                dou1 = Integer.parseInt(quaternary.getOpds().get(1));
                                Label label2 = new Label();
                                Label label3 = new Label();
                                mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                                mv.visitVarInsn(ILOAD, dou0);//将第一个操作数压栈
                                mv.visitIntInsn(SIPUSH, dou1);//第二个操作数压栈
                                mv.visitJumpInsn(IF_ICMPEQ,label2);//两操作数相等继续执行，否则跳到label2处
                                mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                                mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                                mv.visitLabel(label2);//label2在这里
                                mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                                mv.visitLabel(label3);//label3在这里
                            }
                            else{
                                //第二个变量是临时变量
                                Label label2 = new Label();
                                Label label3 = new Label();
                                mv.visitVarInsn(ISTORE,1);
                                mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                                mv.visitVarInsn(ILOAD, dou0);//将第一个操作数压栈
                                mv.visitVarInsn(ILOAD,1);
                                //临时变量之前就已经在数据栈内，不需要压栈
                                mv.visitJumpInsn(IF_ICMPEQ,label2);//两操作数相等继续执行，否则跳到label2处
                                mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                                mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                                mv.visitLabel(label2);//label2在这里
                                mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                                mv.visitLabel(label3);//label3在这里
                            }
                            break;
                        case INTEGER:
                            dou1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                            Label label2 = new Label();
                            Label label3 = new Label();
                            mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                            mv.visitVarInsn(ILOAD, dou0);//将第一个操作数压栈
                            mv.visitVarInsn(ILOAD, dou1);//第二个操作数压栈
                            mv.visitJumpInsn(IF_ICMPEQ,label2);//两操作数相等继续执行，否则跳到label2处
                            mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                            mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                            mv.visitLabel(label2);//label2在这里
                            mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                            mv.visitLabel(label3);//label3在这里
                            break;
                    }

                case FLOAT://还没写
                    break;
                case NULL://临时变量
                    //第一个变量是临时变量
                    mv.visitVarInsn(ISTORE, 0);
                    if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.NULL){
                        //第二个变量也是临时变量或者常数
                        if(Tables.isNumber(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){
                            //第二个是常量
                            dou1 = Integer.parseInt(quaternary.getOpds().get(1));
                            Label label2 = new Label();
                            Label label3 = new Label();
                            mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                            mv.visitVarInsn(ILOAD, 1);
                            mv.visitIntInsn(SIPUSH,dou1);
                            mv.visitJumpInsn(IF_ICMPEQ,label2);//两操作数相等继续执行，否则跳到label2处
                            mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                            mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                            mv.visitLabel(label2);//label2在这里
                            mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                            mv.visitLabel(label3);//label3在这里
                        }
                        else{
                            //第二个是临时变量
                            Label label2 = new Label();
                            Label label3 = new Label();
                            mv.visitVarInsn(ISTORE, 1);
                            mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                            mv.visitVarInsn(ILOAD,0);
                            mv.visitVarInsn(ILOAD,1);
                            //临时变量之前就已经在数据栈内，不需要压栈
                            mv.visitJumpInsn(IF_ICMPEQ,label2);//两操作数相等继续执行，否则跳到label2处
                            mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                            mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                            mv.visitLabel(label2);//label2在这里
                            mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                            mv.visitLabel(label3);//label3在这里
                        }

                    }
                    else if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){
                        //第二个是整形变量
                        dou1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                        Label label2 = new Label();
                        Label label3 = new Label();
                        mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                        mv.visitVarInsn(ILOAD,100);
                        mv.visitVarInsn(ILOAD,dou1);
                        mv.visitJumpInsn(IF_ICMPEQ,label2);//两操作数相等继续执行，否则跳到label2处
                        mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                        mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                        mv.visitLabel(label2);//label2在这里
                        mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                        mv.visitLabel(label3);//label3在这里

                    }
                    else if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.FLOAT){
                        //第二个是浮点变量
                    }
                    break;
            }
        }
        else if(opt.equals(">")){
            if(next.getOpt().toLowerCase().equals("while") || next.getOpt().toLowerCase().equals("if"))
            {
                BytecodeGenerator.labelListWhile.add(new Label());
                BytecodeGenerator.labelListWhileEnd.add(new Label());
                BytecodeGenerator.labelIndex++;
                //mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
            }
            if(next.getOpt().toLowerCase().equals("if")){
                BytecodeGenerator.labelListIF.add(new Label());
                BytecodeGenerator.lalelListELSE.add(new Label());
                BytecodeGenerator.lalelListEIFEND.add(new Label());
                BytecodeGenerator.labelIfIndex ++ ;
            }
            //if_else 跳转语句if（a==b）类型
            System.out.println("equal");
            int dou0,dou1;//记录四元式第一个第二个操作符的值或者位置
            switch (Tables.getType(quaternary.getOpds().get(0))){
                case INTEGER:
                    dou0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.INTEGER);
                    switch (Tables.getType(quaternary.getOpds().get(1))){
                        case NULL:
                            if(Tables.isNumber(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){
                                //第二个操作数是整形常数
                                dou1 = Integer.parseInt(quaternary.getOpds().get(1));
                                Label label2 = new Label();
                                Label label3 = new Label();
                                mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                                mv.visitVarInsn(ILOAD, dou0);//将第一个操作数压栈
                                mv.visitIntInsn(SIPUSH, dou1);//第二个操作数压栈
                                mv.visitJumpInsn(IF_ICMPLE,label2);//两操作数相等继续执行，否则跳到label2处
                                mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                                mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                                mv.visitLabel(label2);//label2在这里
                                mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                                mv.visitLabel(label3);//label3在这里
                            }
                            else{
                                //第二个变量是临时变量
                                Label label2 = new Label();
                                Label label3 = new Label();
                                mv.visitVarInsn(ISTORE,1);
                                mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                                mv.visitVarInsn(ILOAD, dou0);//将第一个操作数压栈
                                mv.visitVarInsn(ILOAD,1);
                                //临时变量之前就已经在数据栈内，不需要压栈
                                mv.visitJumpInsn(IF_ICMPLE,label2);//两操作数相等继续执行，否则跳到label2处
                                mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                                mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                                mv.visitLabel(label2);//label2在这里
                                mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                                mv.visitLabel(label3);//label3在这里
                            }
                            break;
                        case INTEGER:
                            dou1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                            Label label2 = new Label();
                            Label label3 = new Label();
                            mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                            mv.visitVarInsn(ILOAD, dou0);//将第一个操作数压栈
                            mv.visitVarInsn(ILOAD, dou1);//第二个操作数压栈
                            mv.visitJumpInsn(IF_ICMPLE,label2);//两操作数相等继续执行，否则跳到label2处
                            mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                            mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                            mv.visitLabel(label2);//label2在这里
                            mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                            mv.visitLabel(label3);//label3在这里
                            break;
                    }

                case FLOAT://还没写
                    break;
                case NULL://临时变量
                    //第一个变量是临时变量
                    mv.visitVarInsn(ISTORE, 0);
                    if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.NULL){
                        //第二个变量也是临时变量或者常数
                        if(Tables.isNumber(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){
                            //第二个是常量
                            dou1 = Integer.parseInt(quaternary.getOpds().get(1));
                            Label label2 = new Label();
                            Label label3 = new Label();
                            mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                            mv.visitVarInsn(ILOAD, 1);
                            mv.visitIntInsn(SIPUSH,dou1);
                            mv.visitJumpInsn(IF_ICMPLE,label2);//两操作数相等继续执行，否则跳到label2处
                            mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                            mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                            mv.visitLabel(label2);//label2在这里
                            mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                            mv.visitLabel(label3);//label3在这里
                        }
                        else{
                            //第二个是临时变量
                            Label label2 = new Label();
                            Label label3 = new Label();
                            mv.visitVarInsn(ISTORE, 1);
                            mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                            mv.visitVarInsn(ILOAD,0);
                            mv.visitVarInsn(ILOAD,1);
                            //临时变量之前就已经在数据栈内，不需要压栈
                            mv.visitJumpInsn(IF_ICMPLE,label2);//两操作数相等继续执行，否则跳到label2处
                            mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                            mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                            mv.visitLabel(label2);//label2在这里
                            mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                            mv.visitLabel(label3);//label3在这里
                        }

                    }
                    else if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){
                        //第二个是整形变量
                        dou1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                        Label label2 = new Label();
                        Label label3 = new Label();
                        mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                        mv.visitVarInsn(ILOAD,100);
                        mv.visitVarInsn(ILOAD,dou1);
                        mv.visitJumpInsn(IF_ICMPLE,label2);//两操作数相等继续执行，否则跳到label2处
                        mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                        mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                        mv.visitLabel(label2);//label2在这里
                        mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                        mv.visitLabel(label3);//label3在这里

                    }
                    else if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.FLOAT){
                        //第二个是浮点变量
                    }
                    break;
            }
        }
        else if(opt.equals("<")){
            if(next.getOpt().toLowerCase().equals("while") || next.getOpt().toLowerCase().equals("if"))
            {
                BytecodeGenerator.labelListWhile.add(new Label());
                BytecodeGenerator.labelListWhileEnd.add(new Label());
                BytecodeGenerator.labelIndex++;
                //mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
            }
            if(next.getOpt().toLowerCase().equals("if")){
                BytecodeGenerator.labelListIF.add(new Label());
                BytecodeGenerator.lalelListELSE.add(new Label());
                BytecodeGenerator.lalelListEIFEND.add(new Label());
                BytecodeGenerator.labelIfIndex ++ ;
            }
            //if_else 跳转语句if（a==b）类型
            System.out.println("equal");
            int dou0,dou1;//记录四元式第一个第二个操作符的值或者位置
            switch (Tables.getType(quaternary.getOpds().get(0))){
                case INTEGER:
                    dou0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.INTEGER);
                    switch (Tables.getType(quaternary.getOpds().get(1))){
                        case NULL:
                            if(Tables.isNumber(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){
                                //第二个操作数是整形常数
                                dou1 = Integer.parseInt(quaternary.getOpds().get(1));
                                Label label2 = new Label();
                                Label label3 = new Label();
                                mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                                mv.visitVarInsn(ILOAD, dou0);//将第一个操作数压栈
                                mv.visitIntInsn(SIPUSH, dou1);//第二个操作数压栈
                                mv.visitJumpInsn(IF_ICMPGE,label2);//两操作数相等继续执行，否则跳到label2处
                                mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                                mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                                mv.visitLabel(label2);//label2在这里
                                mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                                mv.visitLabel(label3);//label3在这里
                            }
                            else{
                                //第二个变量是临时变量
                                Label label2 = new Label();
                                Label label3 = new Label();
                                mv.visitVarInsn(ISTORE,1);
                                mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                                mv.visitVarInsn(ILOAD, dou0);//将第一个操作数压栈
                                mv.visitVarInsn(ILOAD,1);
                                //临时变量之前就已经在数据栈内，不需要压栈
                                mv.visitJumpInsn(IF_ICMPGE,label2);//两操作数相等继续执行，否则跳到label2处
                                mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                                mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                                mv.visitLabel(label2);//label2在这里
                                mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                                mv.visitLabel(label3);//label3在这里
                            }
                            break;
                        case INTEGER:
                            dou1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                            Label label2 = new Label();
                            Label label3 = new Label();
                            mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                            mv.visitVarInsn(ILOAD, dou0);//将第一个操作数压栈
                            mv.visitVarInsn(ILOAD, dou1);//第二个操作数压栈
                            mv.visitJumpInsn(IF_ICMPGE,label2);//两操作数相等继续执行，否则跳到label2处
                            mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                            mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                            mv.visitLabel(label2);//label2在这里
                            mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                            mv.visitLabel(label3);//label3在这里
                            break;
                    }

                case FLOAT://还没写
                    break;
                case NULL://临时变量
                    //第一个变量是临时变量
                    mv.visitVarInsn(ISTORE, 0);
                    if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.NULL){
                        //第二个变量也是临时变量或者常数
                        if(Tables.isNumber(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){
                            //第二个是常量
                            dou1 = Integer.parseInt(quaternary.getOpds().get(1));
                            Label label2 = new Label();
                            Label label3 = new Label();
                            mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                            mv.visitVarInsn(ILOAD, 1);
                            mv.visitIntInsn(SIPUSH,dou1);
                            mv.visitJumpInsn(IF_ICMPGE,label2);//两操作数相等继续执行，否则跳到label2处
                            mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                            mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                            mv.visitLabel(label2);//label2在这里
                            mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                            mv.visitLabel(label3);//label3在这里
                        }
                        else{
                            //第二个是临时变量
                            Label label2 = new Label();
                            Label label3 = new Label();
                            mv.visitVarInsn(ISTORE, 1);
                            mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                            mv.visitVarInsn(ILOAD,0);
                            mv.visitVarInsn(ILOAD,1);
                            //临时变量之前就已经在数据栈内，不需要压栈
                            mv.visitJumpInsn(IF_ICMPGE,label2);//两操作数相等继续执行，否则跳到label2处
                            mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                            mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                            mv.visitLabel(label2);//label2在这里
                            mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                            mv.visitLabel(label3);//label3在这里
                        }

                    }
                    else if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){
                        //第二个是整形变量
                        dou1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                        Label label2 = new Label();
                        Label label3 = new Label();
                        mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                        mv.visitVarInsn(ILOAD,100);
                        mv.visitVarInsn(ILOAD,dou1);
                        mv.visitJumpInsn(IF_ICMPGE,label2);//两操作数相等继续执行，否则跳到label2处
                        mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                        mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                        mv.visitLabel(label2);//label2在这里
                        mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                        mv.visitLabel(label3);//label3在这里

                    }
                    else if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.FLOAT){
                        //第二个是浮点变量
                    }
                    break;
            }
        }
        else if(opt.equals("<=")){
            //if_else 跳转语句if（a>b）类型
            if(next.getOpt().toLowerCase().equals("while") || next.getOpt().toLowerCase().equals("if"))
            {
                BytecodeGenerator.labelListWhile.add(new Label());
                BytecodeGenerator.labelListWhileEnd.add(new Label());
                BytecodeGenerator.labelIndex++;
                //mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
            }
            if(next.getOpt().toLowerCase().equals("if")){
                BytecodeGenerator.labelListIF.add(new Label());
                BytecodeGenerator.lalelListELSE.add(new Label());
                BytecodeGenerator.lalelListEIFEND.add(new Label());
                BytecodeGenerator.labelIfIndex ++ ;
            }
            //if_else 跳转语句if（a==b）类型
            System.out.println("equal");
            int dou0,dou1;//记录四元式第一个第二个操作符的值或者位置
            switch (Tables.getType(quaternary.getOpds().get(0))){
                case INTEGER:
                    dou0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.INTEGER);
                    switch (Tables.getType(quaternary.getOpds().get(1))){
                        case NULL:
                            if(Tables.isNumber(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){
                                //第二个操作数是整形常数
                                dou1 = Integer.parseInt(quaternary.getOpds().get(1));
                                Label label2 = new Label();
                                Label label3 = new Label();
                                mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                                mv.visitVarInsn(ILOAD, dou0);//将第一个操作数压栈
                                mv.visitIntInsn(SIPUSH, dou1);//第二个操作数压栈
                                mv.visitJumpInsn(IF_ICMPGT,label2);//两操作数相等继续执行，否则跳到label2处
                                mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                                mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                                mv.visitLabel(label2);//label2在这里
                                mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                                mv.visitLabel(label3);//label3在这里
                            }
                            else{
                                //第二个变量是临时变量
                                Label label2 = new Label();
                                Label label3 = new Label();
                                mv.visitVarInsn(ISTORE,1);
                                mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                                mv.visitVarInsn(ILOAD, dou0);//将第一个操作数压栈
                                mv.visitVarInsn(ILOAD,1);
                                //临时变量之前就已经在数据栈内，不需要压栈
                                mv.visitJumpInsn(IF_ICMPGT,label2);//两操作数相等继续执行，否则跳到label2处
                                mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                                mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                                mv.visitLabel(label2);//label2在这里
                                mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                                mv.visitLabel(label3);//label3在这里
                            }
                            break;
                        case INTEGER:
                            dou1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                            Label label2 = new Label();
                            Label label3 = new Label();
                            mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                            mv.visitVarInsn(ILOAD, dou0);//将第一个操作数压栈
                            mv.visitVarInsn(ILOAD, dou1);//第二个操作数压栈
                            mv.visitJumpInsn(IF_ICMPGT,label2);//两操作数相等继续执行，否则跳到label2处
                            mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                            mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                            mv.visitLabel(label2);//label2在这里
                            mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                            mv.visitLabel(label3);//label3在这里
                            break;
                    }

                case FLOAT://还没写
                    break;
                case NULL://临时变量
                    //第一个变量是临时变量
                    mv.visitVarInsn(ISTORE, 0);
                    if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.NULL){
                        //第二个变量也是临时变量或者常数
                        if(Tables.isNumber(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){
                            //第二个是常量
                            dou1 = Integer.parseInt(quaternary.getOpds().get(1));
                            Label label2 = new Label();
                            Label label3 = new Label();
                            mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                            mv.visitVarInsn(ILOAD, 1);
                            mv.visitIntInsn(SIPUSH,dou1);
                            mv.visitJumpInsn(IF_ICMPGT,label2);//两操作数相等继续执行，否则跳到label2处
                            mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                            mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                            mv.visitLabel(label2);//label2在这里
                            mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                            mv.visitLabel(label3);//label3在这里
                        }
                        else{
                            //第二个是临时变量
                            Label label2 = new Label();
                            Label label3 = new Label();
                            mv.visitVarInsn(ISTORE, 1);
                            mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                            mv.visitVarInsn(ILOAD,0);
                            mv.visitVarInsn(ILOAD,1);
                            //临时变量之前就已经在数据栈内，不需要压栈
                            mv.visitJumpInsn(IF_ICMPGT,label2);//两操作数相等继续执行，否则跳到label2处
                            mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                            mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                            mv.visitLabel(label2);//label2在这里
                            mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                            mv.visitLabel(label3);//label3在这里
                        }

                    }
                    else if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){
                        //第二个是整形变量
                        dou1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                        Label label2 = new Label();
                        Label label3 = new Label();
                        mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                        mv.visitVarInsn(ILOAD,100);
                        mv.visitVarInsn(ILOAD,dou1);
                        mv.visitJumpInsn(IF_ICMPGT,label2);//两操作数相等继续执行，否则跳到label2处
                        mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                        mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                        mv.visitLabel(label2);//label2在这里
                        mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                        mv.visitLabel(label3);//label3在这里

                    }
                    else if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.FLOAT){
                        //第二个是浮点变量
                    }
                    break;
            }
        }
        else if(opt.equals(">=")){
            if(next.getOpt().toLowerCase().equals("while") || next.getOpt().toLowerCase().equals("if"))
            {
                BytecodeGenerator.labelListWhile.add(new Label());
                BytecodeGenerator.labelListWhileEnd.add(new Label());
                BytecodeGenerator.labelIndex++;
                //mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
            }
            if(next.getOpt().toLowerCase().equals("if")){
                BytecodeGenerator.labelListIF.add(new Label());
                BytecodeGenerator.lalelListELSE.add(new Label());
                BytecodeGenerator.lalelListEIFEND.add(new Label());
                BytecodeGenerator.labelIfIndex ++ ;
            }
            //if_else 跳转语句if（a==b）类型
            System.out.println("equal");
            int dou0,dou1;//记录四元式第一个第二个操作符的值或者位置
            switch (Tables.getType(quaternary.getOpds().get(0))){
                case INTEGER:
                    dou0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.INTEGER);
                    switch (Tables.getType(quaternary.getOpds().get(1))){
                        case NULL:
                            if(Tables.isNumber(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){
                                //第二个操作数是整形常数
                                dou1 = Integer.parseInt(quaternary.getOpds().get(1));
                                Label label2 = new Label();
                                Label label3 = new Label();
                                mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                                mv.visitVarInsn(ILOAD, dou0);//将第一个操作数压栈
                                mv.visitIntInsn(SIPUSH, dou1);//第二个操作数压栈
                                mv.visitJumpInsn(IF_ICMPLT,label2);//两操作数相等继续执行，否则跳到label2处
                                mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                                mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                                mv.visitLabel(label2);//label2在这里
                                mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                                mv.visitLabel(label3);//label3在这里
                            }
                            else{
                                //第二个变量是临时变量
                                Label label2 = new Label();
                                Label label3 = new Label();
                                mv.visitVarInsn(ISTORE,1);
                                mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                                mv.visitVarInsn(ILOAD, dou0);//将第一个操作数压栈
                                mv.visitVarInsn(ILOAD,1);
                                //临时变量之前就已经在数据栈内，不需要压栈
                                mv.visitJumpInsn(IF_ICMPLT,label2);//两操作数相等继续执行，否则跳到label2处
                                mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                                mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                                mv.visitLabel(label2);//label2在这里
                                mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                                mv.visitLabel(label3);//label3在这里
                            }
                            break;
                        case INTEGER:
                            dou1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                            Label label2 = new Label();
                            Label label3 = new Label();
                            mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                            mv.visitVarInsn(ILOAD, dou0);//将第一个操作数压栈
                            mv.visitVarInsn(ILOAD, dou1);//第二个操作数压栈
                            mv.visitJumpInsn(IF_ICMPLT,label2);//两操作数相等继续执行，否则跳到label2处
                            mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                            mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                            mv.visitLabel(label2);//label2在这里
                            mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                            mv.visitLabel(label3);//label3在这里
                            break;
                    }

                case FLOAT://还没写
                    break;
                case NULL://临时变量
                    //第一个变量是临时变量
                    mv.visitVarInsn(ISTORE, 0);
                    if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.NULL){
                        //第二个变量也是临时变量或者常数
                        if(Tables.isNumber(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){
                            //第二个是常量
                            dou1 = Integer.parseInt(quaternary.getOpds().get(1));
                            Label label2 = new Label();
                            Label label3 = new Label();
                            mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                            mv.visitVarInsn(ILOAD, 1);
                            mv.visitIntInsn(SIPUSH,dou1);
                            mv.visitJumpInsn(IF_ICMPLT,label2);//两操作数相等继续执行，否则跳到label2处
                            mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                            mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                            mv.visitLabel(label2);//label2在这里
                            mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                            mv.visitLabel(label3);//label3在这里
                        }
                        else{
                            //第二个是临时变量
                            Label label2 = new Label();
                            Label label3 = new Label();
                            mv.visitVarInsn(ISTORE, 1);
                            mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                            mv.visitVarInsn(ILOAD,0);
                            mv.visitVarInsn(ILOAD,1);
                            //临时变量之前就已经在数据栈内，不需要压栈
                            mv.visitJumpInsn(IF_ICMPLT,label2);//两操作数相等继续执行，否则跳到label2处
                            mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                            mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                            mv.visitLabel(label2);//label2在这里
                            mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                            mv.visitLabel(label3);//label3在这里
                        }

                    }
                    else if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){
                        //第二个是整形变量
                        dou1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                        Label label2 = new Label();
                        Label label3 = new Label();
                        mv.visitLabel(BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex));
                        mv.visitVarInsn(ILOAD,100);
                        mv.visitVarInsn(ILOAD,dou1);
                        mv.visitJumpInsn(IF_ICMPLT,label2);//两操作数相等继续执行，否则跳到label2处
                        mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                        mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                        mv.visitLabel(label2);//label2在这里
                        mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                        mv.visitLabel(label3);//label3在这里

                    }
                    else if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.FLOAT){
                        //第二个是浮点变量
                    }
                    break;
            }
        }
        else if(opt.equals("if")){
            switch (Tables.getType(quaternary.getOpds().get(2))){
                case NULL:
                    System.out.println("if");
                    mv.visitIntInsn(BIPUSH, 1);//把1推至栈顶，与原有的比较
                    mv.visitJumpInsn(IF_ICMPNE, BytecodeGenerator.lalelListELSE.get(BytecodeGenerator.labelIfIndex - 1));//比较，相同继续，不同跳转到labelelse
                    break;
                case FLOAT://还没写
                    break;
            }
        }
        else if(opt.equals("BEGIN_ELSE")){
            mv.visitJumpInsn(GOTO, BytecodeGenerator.lalelListEIFEND.get(BytecodeGenerator.labelIfIndex - 1));
            mv.visitLabel(BytecodeGenerator.lalelListELSE.get(BytecodeGenerator.labelIfIndex - 1));
        }
        else if(opt.equals("END_IF")){
            mv.visitLabel(BytecodeGenerator.lalelListEIFEND.get(BytecodeGenerator.labelIfIndex - 1));
            BytecodeGenerator.labelIndex--;
            BytecodeGenerator.labelIfIndex--;
        }
        else if(opt.equals("while")){
            switch (Tables.getType(quaternary.getOpds().get(2))){
                case NULL:
                    mv.visitIntInsn(BIPUSH, 1);//把1推至栈顶，与原有的比较
                    mv.visitJumpInsn(IF_ICMPNE, BytecodeGenerator.labelListWhileEnd.get(BytecodeGenerator.labelIndex));//比较，相同继续，不同跳转到labelelse
                    break;
                case FLOAT://还没写
                    break;
            }
        }
        else if(opt.equals("END_WHILE")){
            mv.visitJumpInsn(GOTO,BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex ));
            mv.visitLabel(BytecodeGenerator.labelListWhileEnd.get(BytecodeGenerator.labelIndex));
            BytecodeGenerator.labelIndex--;
        }
        else if(opt.equals("BREAK")){
            mv.visitJumpInsn(GOTO,BytecodeGenerator.labelListWhileEnd.get(BytecodeGenerator.labelIndex - BytecodeGenerator.labelIfIndex));
        }
        else if(opt.equals("CONTINUE")){
            mv.visitJumpInsn(GOTO, BytecodeGenerator.labelListWhile.get(BytecodeGenerator.labelIndex - BytecodeGenerator.labelIfIndex));
        }
    }
}
