package com.compiler.utils;

import com.compiler.parser.Symbol;
import com.compiler.parser.SymbolString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Show {
    public static void printRules(HashMap<Symbol, HashSet<SymbolString>> rules){
        for(var rule:rules.keySet()){
            System.out.print(rule);
            System.out.print("->");
            var s = rules.get(rule);
            for (var symstr:s){
                System.out.print(symstr);
                System.out.print("|");
            }
            System.out.println();
        }
    }

    public static void printSymbolList(ArrayList<Symbol> symList){
        for(Symbol sym:symList){
            System.out.println(sym);
        }
    }
}
