package com.compiler.parser;

import com.compiler.lexer.Token;

public class Symbol {

    Symbol() {
        sType = SymbolType.Terminal;
//        id = "";
    }


    Symbol(final String name, SymbolType st) {
        id = name;
        sType = st;
        value = "";
    }

    Symbol(final String name, SymbolType st, String value, Token.tokenType type) {
        id = name;
        sType = st;
        this.value = value;
        this.valueType = type;
    }

    Symbol(final String name, ActionType at) {
        id = name;
        aType = at;
        sType = SymbolType.Action;
        this.value = "";
    }

    enum SymbolType {
        Terminal, Nonterminal, Empty, End, Action
    }

    enum ActionType {
        PUSH, FILL, FILL_I //有初值的填表
        , FILL_ARRAY //填数组表
        , FILL_EMPTY_ARRAY //填空数组
        , PUSH_CAPACITY //将数组容量压栈
        , GEQ    //生成运算四元式
        , GEQ_IF, END_IF, BEGIN_ELSE, GEQ_WHILE, END_WHILE, GEQ_PRINT, GEQ_BREAK
    }

    public String getId() {
        return id;
    }

    public SymbolType getType() {
        return sType;
    }

    public ActionType getaType() {
        return aType;
    }

    public String getValue() {
        return value;
    }

    public Token.tokenType getValueType() {
        return valueType;
    }

    private String id;
    private SymbolType sType;
    private ActionType aType;
    private String value;
    private Token.tokenType valueType = Token.tokenType.NONE;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (sType == SymbolType.Nonterminal)
            sb.append("[N]");
        else if (sType == SymbolType.Action)
            sb.append("[AC]");
        sb.append(id);
        if (valueType != Token.tokenType.NONE) {
            sb.append("(");
            sb.append(value);
            sb.append(")");
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Symbol) {
            var sym = (Symbol) obj;
            return this.id.equals(sym.id) &&
                    this.sType.equals(sym.sType);
        }
        return false;
    }
}
