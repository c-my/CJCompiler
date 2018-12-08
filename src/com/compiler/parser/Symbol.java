package com.compiler.parser;

public class Symbol {

    Symbol(){
        sType = SymbolType.Terminal;
    };
    Symbol(final String name, SymbolType st){
        id = name;
        sType = st;
    }

    enum SymbolType{
        Terminal, Nonterminal, Empty, End, Action
    }

    public String getId() {
        return id;
    }

    public SymbolType getType() {
        return sType;
    }

    private String id;
    private SymbolType sType;

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Symbol) {
            var sym = (Symbol)obj;
            return this.id.equals(sym.id)&&
                    this.sType.equals(sym.sType);
        }
        return false;
    }
}
