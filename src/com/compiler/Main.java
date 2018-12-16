package com.compiler;

import com.compiler.lexer.Lexer;
import com.compiler.lexer.LexerTester;
import com.compiler.parser.ParserGenerator;
import com.compiler.parser.ParserTest;
import com.compiler.utils.Show;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        // write your code here
        System.out.println("Hello world");
        ParserTest pt = new ParserTest();
        Lexer lxer = new Lexer();
        LexerTester lt = new LexerTester();
        System.out.println(ParserGenerator.ReadTokenlist(lt.getTokenList()));
//        ArrayList<String> lines = new ArrayList<>();
//        lines.add("A->tb nB|nC");
//        lines.add("B->tc|ee");
//        Show.printRules(ParserGenerator.Read(lines));

    }
}
