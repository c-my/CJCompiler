package com.compiler.parser;

import com.compiler.lexer.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ParserGenerator {

    // 根据文本字符串生成文法产生式
    static public HashMap<Symbol, HashSet<SymbolString>> Read(List<String> lines) {
        HashMap<Symbol, HashSet<SymbolString>> productionRules = new HashMap<>();
        int index = 0;
        for (String line : lines) {
            if (line.isEmpty())
                continue;
            var strs = line.split("->");
            if (strs.length < 2)
                return new HashMap<>();
            String left = strs[0];
            String right = strs[1];

            Symbol leftSym = new Symbol(left, Symbol.SymbolType.Nonterminal);
            HashSet<SymbolString> symbolStrings = new HashSet<>();

            var symstrs = right.split("\\|");
            for (var str : symstrs) {
                SymbolString symbolString = new SymbolString(index++);
                if (str.equals(" "))
                    symbolString.add(new Symbol("|", Symbol.SymbolType.Terminal));
                else {
                    var syms = str.split(" ");
                    for (String sym : syms) {
                        if (sym.equals("$"))
                            symbolString.add(new Symbol(sym, Symbol.SymbolType.Empty));
                        else if (sym.equals("PUSH"))
                            symbolString.add(new Symbol(sym, Symbol.ActionType.PUSH));
                        else if (sym.equals("PUSH_CAPACITY"))
                            symbolString.add(new Symbol(sym, Symbol.ActionType.PUSH_CAPACITY));
                        else if (sym.equals("FILL"))
                            symbolString.add(new Symbol(sym, Symbol.ActionType.FILL));
                        else if (sym.equals("FILL_I"))
                            symbolString.add(new Symbol(sym, Symbol.ActionType.FILL_I));
                        else if (sym.equals("FILL_ARRAY"))
                            symbolString.add(new Symbol(sym, Symbol.ActionType.FILL_ARRAY));
                        else if (sym.equals("FILL_EMPTY_ARRAY"))
                            symbolString.add(new Symbol(sym, Symbol.ActionType.FILL_EMPTY_ARRAY));
                        else if (sym.equals("GEQ"))
                            symbolString.add(new Symbol(sym, Symbol.ActionType.GEQ));
                        else if (sym.equals("GEQ_IF"))
                            symbolString.add(new Symbol(sym, Symbol.ActionType.GEQ_IF));
                        else if (sym.equals("END_IF"))
                            symbolString.add(new Symbol(sym, Symbol.ActionType.END_IF));
                        else if (sym.equals("BEGIN_ELSE"))
                            symbolString.add(new Symbol(sym, Symbol.ActionType.BEGIN_ELSE));
                        else if (sym.equals("GEQ_WHILE"))
                            symbolString.add(new Symbol(sym, Symbol.ActionType.GEQ_WHILE));
                        else if (sym.equals("END_WHILE"))
                            symbolString.add(new Symbol(sym, Symbol.ActionType.END_WHILE));
                        else if(sym.equals("GEQ_BREAK"))
                            symbolString.add(new Symbol(sym, Symbol.ActionType.GEQ_BREAK));
                        else if(sym.equals("GEQ_CONTINUE"))
                            symbolString.add(new Symbol(sym, Symbol.ActionType.GEQ_CONTINUE));
                        else if (sym.equals("GEQ_PRINT"))
                            symbolString.add(new Symbol(sym, Symbol.ActionType.GEQ_PRINT));
                        else if (ParserTest.isTerminal(sym))
                            symbolString.add(new Symbol(sym, Symbol.SymbolType.Terminal));
                        else
                            symbolString.add(new Symbol(sym, Symbol.SymbolType.Nonterminal));
                    }
                }
                symbolStrings.add(symbolString);
            }
            productionRules.put(leftSym, symbolStrings);
        }
        return productionRules;
    }

    // 根据tokenlist得到文法字符串
    static public ArrayList<Symbol> ReadTokenlist(ArrayList<Token> tokenList) {
        ArrayList<Symbol> symbolList = new ArrayList<>();
        for (Token token : tokenList) {
            switch (token.getType()) {
                case STR:
                    symbolList.add(new Symbol("STRING_LITERAL", Symbol.SymbolType.Terminal,
                            token.getStr(), token.getType()));
                    break;
                case IDENTIFIER:
                    symbolList.add(new Symbol("IDENTIFIER", Symbol.SymbolType.Terminal,
                            token.getStr(), token.getType()));
                    break;
                case DELIMITER:
                    switch (token.getStr()) {
                        case "|=":
                            symbolList.add(new Symbol("OR_ASSIGN", Symbol.SymbolType.Terminal));
                            break;
                        case "++":
                            symbolList.add(new Symbol("INC_OP", Symbol.SymbolType.Terminal));
                            break;
                        case "--":
                            symbolList.add(new Symbol("DEC_OP", Symbol.SymbolType.Terminal));
                            break;
                        case "&&":
                            symbolList.add(new Symbol("AND_OP", Symbol.SymbolType.Terminal));
                            break;
                        case "||":
                            symbolList.add(new Symbol("OR_OP", Symbol.SymbolType.Terminal));
                            break;
                        case "==":
                            symbolList.add(new Symbol("EQ_OP", Symbol.SymbolType.Terminal));
                            break;
                        case "!=":
                            symbolList.add(new Symbol("NE_OP", Symbol.SymbolType.Terminal));
                            break;
                        case "->":
                            symbolList.add(new Symbol("PTR_OP", Symbol.SymbolType.Terminal));
                            break;
                        default:
                            symbolList.add(new Symbol(token.getStr(), Symbol.SymbolType.Terminal));
                            break;

                    }
                    break;
                case KEYWORD:
                    symbolList.add(new Symbol(token.getStr().toUpperCase(), Symbol.SymbolType.Terminal));
                    break;
                case CHAR:
                case INTEGER:
                case FLOAT:
                case DOUBLE:
                    symbolList.add(new Symbol("CONSTANT", Symbol.SymbolType.Terminal,
                            token.getStr(), token.getType()));
                    break;

            }
        }
        return symbolList;
    }
}
