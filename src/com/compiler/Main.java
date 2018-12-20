package com.compiler;

import com.compiler.lexer.LexerTester;
import com.compiler.parser.*;

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
        var quaternaryList = Tables.quaternaryList;
        System.out.println(result);

        System.out.println(Tables.SymbolTable);
        System.out.println(Tables.quaternaryList);


    }
}
