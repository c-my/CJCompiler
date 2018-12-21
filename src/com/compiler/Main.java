package com.compiler;

import com.compiler.IntermediateCodeGeneration.Compiler;
import com.compiler.lexer.LexerTester;
import com.compiler.parser.ParserGenerator;
import com.compiler.parser.ParserTest;
import com.compiler.parser.Tables;


public class Main {

    public static void main(String[] args) {

        System.out.println("====================================");


        LexerTester lt = new LexerTester();
        var tokenList = lt.getTokenList();
        System.out.println(tokenList);

        System.out.println("====================================");

        ParserTest pt = new ParserTest();
        var symbolString = ParserGenerator.ReadTokenlist(tokenList);
        System.out.println(symbolString);


        boolean result = pt.Check(symbolString);

        var compiler = new Compiler();
        try{compiler.compile();}
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(result);

        System.out.println(Tables.SymbolTable);
        System.out.println(Tables.quaternaryList);



    }
}
