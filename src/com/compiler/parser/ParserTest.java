package com.compiler.parser;

import com.compiler.utils.FileReader;
import com.compiler.utils.Pair;
import com.compiler.utils.StringSet;

import java.util.*;

public class ParserTest extends LLParser {

    public ParserTest() {
        super();
        init();

    }

    private HashMap<Symbol, HashSet<SymbolString>> rules = new HashMap<>();
    private Symbol startSym = new Symbol("S", Symbol.SymbolType.Nonterminal);

    private void init() {
        initTerminalSymbols();
        initRules();
//        var form = getForm();
//        for (var k : form.keySet()) {
//            System.out.print(k);
//            System.out.print("\t");
//            System.out.println(form.get(k));
//        }
        var rules = getProductionRules();
        for (var ru : rules.keySet()) {
            var strs = rules.get(ru);
            for (var st : strs) {
                var select = getSelect(st);
                System.out.print(st);
                System.out.print(":  ");
                System.out.println(select);
            }
        }

    }

    @Override
    HashMap<Symbol, HashSet<SymbolString>> getProductionRules() {
        return rules;
    }

    @Override
    Symbol getStartSymbol() {
        return startSym;
    }

    private static HashSet<String> terminalSymbols = new HashSet<>();

    private void initTerminalSymbols() {
        terminalSymbols.add("CONSTANT");
        terminalSymbols.add("STRING_LITERAL");
        terminalSymbols.add("IDENTIFIER");
        terminalSymbols.add("LEFT_OP");
        terminalSymbols.add("RIGHT_OP");
        terminalSymbols.add("INC_OP");
        terminalSymbols.add("DEC_OP");
        terminalSymbols.add("PTR_OP");
        terminalSymbols.add("AND_OP");
        terminalSymbols.add("OR_OP");
        terminalSymbols.add("LE_OP");
        terminalSymbols.add("GE_OP");
        terminalSymbols.add("NE_OP");
        terminalSymbols.add("EQ_OP");
        terminalSymbols.add("SIZEOF");
        terminalSymbols.add("RIGHT_ASSIGN");
        terminalSymbols.add("LEFT_ASSIGN");
        terminalSymbols.add("ADD_ASSIGN");
        terminalSymbols.add("SUB_ASSIGN");
        terminalSymbols.add("MUL_ASSIGN");
        terminalSymbols.add("DIV_ASSIGN");
        terminalSymbols.add("MOD_ASSIGN");
        terminalSymbols.add("AND_ASSIGN");
        terminalSymbols.add("XOR_ASSIGN");
        terminalSymbols.add("OR_ASSIGN");
    }

    private void initRules() {
        List<String> lines = FileReader.getLines("res/SimpleGrammar.txt");
        rules = ParserGenerator.Read(lines);
    }


    // 判断是否为终结符
    public static boolean isTerminal(String sym) {
        return terminalSymbols.contains(sym)
                || StringSet.KEYWORD.contains(sym.toLowerCase())
                || StringSet.DELIMITER.contains(sym.toLowerCase());
    }

    public static boolean isAction(String sym) {
        return false;
    }

}
