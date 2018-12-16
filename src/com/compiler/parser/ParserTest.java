package com.compiler.parser;

import java.util.*;

public class ParserTest extends LLParser {

    public ParserTest() {
        super();
        init();
    }

    private HashMap<Symbol, HashSet<SymbolString>> rules = new HashMap<>();
    private Symbol startSym = new Symbol();

    private void init() {
        Symbol E = new Symbol("E", Symbol.SymbolType.Nonterminal);
        Symbol E1 = new Symbol("E1", Symbol.SymbolType.Nonterminal);
        Symbol T = new Symbol("T", Symbol.SymbolType.Nonterminal);
        Symbol T1 = new Symbol("T1", Symbol.SymbolType.Nonterminal);
        Symbol F = new Symbol("F", Symbol.SymbolType.Nonterminal);
        Symbol w0 = new Symbol("w0", Symbol.SymbolType.Terminal);
        Symbol w1 = new Symbol("w1", Symbol.SymbolType.Terminal);
        Symbol i = new Symbol("i", Symbol.SymbolType.Terminal);
        Symbol lb = new Symbol("(", Symbol.SymbolType.Terminal);
        Symbol rb = new Symbol(")", Symbol.SymbolType.Terminal);
        Symbol empty = new Symbol("empty", Symbol.SymbolType.Empty);

        SymbolString s1 = new SymbolString(new ArrayList<>(Arrays.asList(T, E1)), 1);
        SymbolString s2 = new SymbolString(new ArrayList<>(Arrays.asList(w0, T, E1)), 2);
        SymbolString s3 = new SymbolString(new ArrayList<>(Arrays.asList(empty)), 3);
        SymbolString s4 = new SymbolString(new ArrayList<>(Arrays.asList(F, T1)), 4);
        SymbolString s5 = new SymbolString(new ArrayList<>(Arrays.asList(w1, F, T1)), 5);
        SymbolString s6 = new SymbolString(new ArrayList<>(Collections.singletonList(empty)), 6);
        SymbolString s7 = new SymbolString(new ArrayList<>(Arrays.asList(i)), 7);
        SymbolString s8 = new SymbolString(new ArrayList<>(Arrays.asList(lb, E, rb)), 8);

        rules.put(E, new HashSet<>(Arrays.asList(s1)));
        rules.put(E1, new HashSet<>(Arrays.asList(s2, s3)));
        rules.put(T, new HashSet<>(Arrays.asList(s4)));
        rules.put(T1, new HashSet<>(Arrays.asList(s5, s6)));
        rules.put(F, new HashSet<>(Arrays.asList(s7, s8)));

        startSym = E;

        var form = getForm();
        for (var k : form.keySet()) {
            System.out.print(k);
            System.out.print("\t");
            System.out.println(form.get(k));
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

    public void initProductionRules(){
//        rules.put(new Symbol("primary_expression", Symbol.SymbolType.Nonterminal),)

    }

}
