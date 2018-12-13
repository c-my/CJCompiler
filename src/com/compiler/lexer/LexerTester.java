package com.compiler.lexer;

import java.io.File;

public class LexerTester {
    public LexerTester() {
        Lexer lxer = new Lexer();
        File f = new File("res/lexer.c");
        String s = "-1.2333f";
        System.out.print(lxer.Run(s));
    }
}
