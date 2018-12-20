package com.compiler.IntermediateCodeGeneration;

import com.compiler.parser.Quaternary;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.io.*;

public class JvmCode extends PreJvmCode{

    Quaternary quaternary = new Quaternary();
    public void GetJvmCode(){

    }

    public static void addContainsToFile(long position,String contents) throws IOException {
        //1、参数校验
        File file = new File("C:\\Users\\代进\\Desktop\\111.txt");
        System.out.println(file);
        //判断文件是否存在
        if(!(file.exists() && file.isFile())){
            System.out.println("文件不存在  ~ ");
            return;
        }
        //判断position是否合法
        if((position < 0) || (position > file.length())){
            System.out.println("position不合法 ~ ");
            return;
        }

        //2、创建临时文件
        File tempFile =File.createTempFile("sss", ".temp",new File("d:/"));
        //3、用文件输入流、文件输出流对文件进行操作
        FileOutputStream outputStream = new FileOutputStream(tempFile);
        FileInputStream inputStream = new FileInputStream(tempFile);
        //在退出JVM退出时自动删除
        tempFile.deleteOnExit();

        //4、创建RandomAccessFile流
        RandomAccessFile rw = new RandomAccessFile(file,"rw");
        //文件指定位置到 position
        rw.seek(position);

        int tmp;
        //5、将position位置后的内容写入临时文件
        while((tmp = rw.read())!=-1){
            outputStream.write(tmp);
        }
        //6、将追加内容 contents 写入 position 位置
        rw.seek(position);
        rw.write(contents.getBytes());

        //7、将临时文件写回文件，并将创建的流关闭
        while((tmp = inputStream.read())!=-1){
            rw.write(tmp);
        }
        rw.close();
        outputStream.close();
        inputStream.close();
    }

    private void EqualJvm(){//四元式运算符为等于号（=）生成字节码
        //<=,1，，a >
        System.out.println("0: iconst_1");
        System.out.println("1: istore_1");
        String contents = new String("0: iconst_1" + "\n" + "1: istore_1");
        try {
            addContainsToFile(file.length()-1,contents);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void GetBite(){
        ClassWriter cw = new ClassWriter(0);
        MethodVisitor mv;
        //cw.visit(52, ACC_PUBLUC + ACC_SUPER, name, null, "java/lang/Object",null);
    }

    public static void main(String[] args){
        JvmCode c = new JvmCode();
        c.printHead();
        c.EqualJvm();
    }


}
