package com.compiler.lexer;


import java.util.HashMap;
import java.util.HashSet;

public class SymbolInfo {

    public SymbolInfo() {

    }

    enum type_e {INT, FLOAT, DOUBLE, CHAR, STRING, ARRAY, BOOL, STRUCT, POINTER}

    enum cat_e {FUNCTION, CONSTANT, TYPE, VARIABLE}

    private type_e type;
    private cat_e cat;

    public void setType(type_e type) {
        this.type = type;
    }

    public void setCat(cat_e cat) {
        this.cat = cat;
    }

    public type_e getType() {
        return type;
    }

    public cat_e getCat() {
        return cat;
    }


}

