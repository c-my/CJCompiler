package com.compiler;

import com.compiler.lexer.Lexer;
import com.compiler.lexer.LexerTester;
import com.compiler.parser.ParserGenerator;
import com.compiler.parser.ParserTest;
import com.compiler.parser.Tables;
import com.compiler.parser.Test;
import com.compiler.utils.FileReader;
import com.compiler.utils.Show;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        LexerTester lt = new LexerTester();
        var tokenList = lt.getTokenList();
        System.out.println(tokenList);

        System.out.println("=========");

        ParserTest pt = new ParserTest();
        var symbolString = ParserGenerator.ReadTokenlist(tokenList);
        System.out.println(symbolString);

        boolean result = pt.Check(symbolString);
        System.out.println(result);

        System.out.println(Tables.SymbolTable);

//        List<String> lines = FileReader.getLines("res/SimpleGrammar.txt");
//////        lines.add("primary_expression->IDENTIFIER|CONSTANT|STRING_LITERAL|( expression )");
//////        lines.add("B->tc|ee");
//        Show.printRules(ParserGenerator.Read(lines));

    }
}
