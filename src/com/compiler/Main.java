package com.compiler;

import com.compiler.lexer.Lexer;
import com.compiler.lexer.LexerTester;
import com.compiler.parser.ParserTest;

public class Main {

    public static void main(String[] args) {
        // write your code here
        System.out.println("Hello world");
        ParserTest pt = new ParserTest();
        Lexer lxer = new Lexer();
        LexerTester lt = new LexerTester();
        System.out.println(lt.getTokenList());
    }
}
