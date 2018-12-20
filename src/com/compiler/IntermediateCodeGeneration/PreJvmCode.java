package com.compiler.IntermediateCodeGeneration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PreJvmCode {
    File file = null;
    public void printHead(){
        //main函数JVM头

        FileWriter fw = null;
        file = new File("C:\\Users\\代进\\Desktop\\111.txt");
        try{
            if(!file.exists()){
                //文件不存在，创建文件
                file.createNewFile();
            }
            fw = new FileWriter(file);
            fw.write("Compiled from \"main.java\"\n" +
                    "public class main {\n" +
                    "  public main();\n" +
                    "    Code:\n" +
                    "       0: aload_0\n" +
                    "       1: invokespecial #1                  // Method java/lang/Object.\"<init>\":()V\n" +
                    "       4: return\n" +
                    "\n" +
                    "  public static void main(java.lang.String[]);\n" +
                    "    Code:" +
                    "" +
                    "}");
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(fw != null){
                try{
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //添加文件其余部分
    /*String contents = new String("abcd");
	        try {
        test.addContainsToFile(file.length()-1,contents);
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }*/
}
