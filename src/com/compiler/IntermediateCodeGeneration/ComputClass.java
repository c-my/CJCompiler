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

    public void comput(MethodVisitor mv , Quaternary quaternary){
        final String opt = new String(quaternary.getOpt());//判断运算符
        if (opt.equals("=")) {//进行赋值运算
            switch (Tables.getType(quaternary.getOpds().get(0))){
                case INTEGER://整形的赋值运算
                    int val0 = Tables.getIntValue(quaternary.getOpds().get(0));//第一个算符的值
                    int val2 = Tables.getSymbolCount(quaternary.getOpds().get(2), TypeLabelItem.type.INTEGER);//第三个算符在本地变量中的位置
                    mv.visitIntInsn(SIPUSH, val0);//推送至栈顶
                    mv.visitVarInsn(ISTORE, val2);//栈顶值赋值
                    break;
                case FLOAT://浮点，double赋值（解决浮点问题后改善）
                    Double dou0 = Tables.getDoubleValue(quaternary.getOpds().get(0));
                    //int dou0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.FLOAT);
                    int dou2 = Tables.getSymbolCount(quaternary.getOpds().get(2), TypeLabelItem.type.FLOAT);
                    mv.visitLdcInsn("asd");
                    mv.visitVarInsn(ASTORE,dou2);
                    break;
                case NULL://常数不在符号表中，常数赋值给变量
                    if(Tables.isNumber(quaternary.getOpds().get(0)) == TypeLabelItem.type.INTEGER){
                        //四元式第一个算符是整形常数
                        int po0 = Integer.parseInt(quaternary.getOpds().get(0));//求整形常熟的值
                        int po2 = Tables.getSymbolCount(quaternary.getOpds().get(2), TypeLabelItem.type.INTEGER);//第三个算符在本地变量表中的位置
                        mv.visitIntInsn(SIPUSH, po0);
                        mv.visitVarInsn(ISTORE, po2);
                    }
                    else {
                        int val = Tables.getSymbolCount(quaternary.getOpds().get(2), TypeLabelItem.type.INTEGER);//直接将栈里的值赋给第三个算符
                        mv.visitVarInsn(ISTORE, val);
                    }
                    break;
                }

        }
        else if(opt.equals("+")){
            switch (Tables.getType(quaternary.getOpds().get(0))){
                case INTEGER:
                    int val0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.INTEGER);
                    int val1 ;

                    //System.out.println(val0);
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
                    int dou0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.FLOAT);
                    int dou1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.FLOAT);
                    int dou2 = Tables.getSymbolCount(quaternary.getOpds().get(2), TypeLabelItem.type.FLOAT);
                    mv.visitVarInsn(DLOAD, dou0);
                    mv.visitVarInsn(DLOAD, dou1);
                    mv.visitInsn(FADD);
                    mv.visitVarInsn(FSTORE,dou2);
                    break;
                case NULL:
                    if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.INTEGER){//<+,t1,a,t2>
                        int ui1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                        mv.visitVarInsn(ILOAD, ui1);
                        mv.visitInsn(IADD);
                    }
                    else if(Tables.getType(quaternary.getOpds().get(1)) == TypeLabelItem.type.NULL){//<+,t1,t2,t3>
                        mv.visitInsn(IADD);
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
                    mv.visitVarInsn(DLOAD, dou);
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
                    break;
            }
        }
        else if(opt.equals("EQ_OP")){
            //if_else 跳转语句if（a==b）类型
            System.out.println("equal");
            switch (Tables.getType(quaternary.getOpds().get(0))){
                case INTEGER:
                    int dou0 = Tables.getSymbolCount(quaternary.getOpds().get(0), TypeLabelItem.type.INTEGER);


                    int dou1 = Tables.getSymbolCount(quaternary.getOpds().get(1), TypeLabelItem.type.INTEGER);
                    Label label2 = new Label();
                    Label label3 = new Label();
                    mv.visitVarInsn(ILOAD, dou0);//将第一个操作数压栈
                    mv.visitVarInsn(ILOAD, dou1);//第二个操作数压栈
                    mv.visitJumpInsn(IF_ICMPNE,label2);//两操作数相等继续执行，否则跳到label2处
                    mv.visitIntInsn(BIPUSH, 1);//将操作数栈顶置为1，为后续<if, , ,t1 >准备
                    mv.visitJumpInsn(GOTO, label3);//无条件跳转到label3
                    mv.visitLabel(label2);//label2在这里
                    mv.visitIntInsn(BIPUSH, 0);//将栈顶值为0，为<BEGIN_ELSE, , , >准备
                    mv.visitLabel(label3);//label3在这里
                    break;
                case FLOAT://还没写
                    break;
            }

        }
        else if(opt.equals("NE_OP")){
            //if_else 跳转语句if（a!=b）类型
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
                                mv.visitLabel(bet.labelwhile);
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
                                mv.visitLabel(bet.labelwhile);
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
                            mv.visitLabel(bet.labelwhile);
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
                            mv.visitLabel(bet.labelwhile);
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
                            mv.visitLabel(bet.labelwhile);
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
                        mv.visitLabel(bet.labelwhile);
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
        else if(opt.equals("if")){
            switch (Tables.getType(quaternary.getOpds().get(2))){
                case NULL:
                    System.out.println("if");
                    mv.visitIntInsn(BIPUSH, 1);//把1推至栈顶，与原有的比较
                    mv.visitJumpInsn(IF_ICMPNE, bet.labelelse);//比较，相同继续，不同跳转到labelelse
                    break;
                case FLOAT://还没写
                    break;
            }
        }
        else if(opt.equals("BEGIN_ELSE")){
            mv.visitJumpInsn(GOTO, bet.labelend);
            mv.visitLabel(bet.labelelse);
        }
        else if(opt.equals("END_IF")){
            mv.visitLabel(bet.labelend);
        }
        else if(opt.equals("while")){
            switch (Tables.getType(quaternary.getOpds().get(2))){
                case NULL:
                    mv.visitIntInsn(BIPUSH, 1);//把1推至栈顶，与原有的比较
                    mv.visitJumpInsn(IF_ICMPNE, bet.labelEND_WHILE);//比较，相同继续，不同跳转到labelelse
                    break;
                case FLOAT://还没写
                    break;
            }
        }
        else if(opt.equals("END_WHILE")){
            mv.visitJumpInsn(GOTO,bet.labelwhile);
            mv.visitLabel(bet.labelEND_WHILE);
        }


    }
}
