package com.compiler.parser;

import com.compiler.lexer.Token;

public class Symbol {

    Symbol() {
        sType = SymbolType.Terminal;
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

    enum SymbolType {
        Terminal, Nonterminal, Empty, End, Action
    }

    public String getId() {
        return id;
    }

    public SymbolType getType() {
        return sType;
    }

    public String getValue() {
        return value;
    }

    private String id;
    private SymbolType sType;
    private String value;
    private Token.tokenType valueType = Token.tokenType.NONE;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
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
