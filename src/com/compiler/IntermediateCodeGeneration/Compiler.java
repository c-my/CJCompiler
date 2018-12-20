package com.compiler.IntermediateCodeGeneration;

import com.compiler.parser.Tables;
import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Compiler implements Opcodes {
    public void Compilermain(String[] args) throws Exception {
        new Compiler().compile(args);
    }

    public void compile(String[] args) throws Exception {

        final File enkelFile = new File("res/lexer.c");
        String fileName = enkelFile.getName();
        //String fileAbsolutePath = enkelFile.getAbsolutePath();
        //String className = StringUtils.remove(fileName, ".enk");
        final byte[] byteCode = new BytecodeGenerator().generateBytecode(Tables.quaternaryList);
        saveBytecodeToClassFile(fileName, byteCode);
    }



    private static void saveBytecodeToClassFile(String fileName, byte[] byteCode) throws IOException {
        final String classFile = StringUtils.replace(fileName, ".c", ".class");
        OutputStream os = new FileOutputStream(classFile);
        System.out.println(".class文件生成");
        os.write(byteCode);
        System.out.println(".class文件写入");
        os.close();
        System.out.println(".class文件关闭");
    }
}
