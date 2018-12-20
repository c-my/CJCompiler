package com.compiler.parser;

public class TypeLabelItem { public TypeLabelItem(type t) {
    this.t = t;
    switch (t) {
        case CHAR:
        case INTEGER:
        case FLOAT:
            this.tpoint = -1; //基本类型为null
            break;
        case ARRAY:
            break;
        case STRUCT:
            break;
    }
}

    public enum type {INTEGER, FLOAT, CHAR, ARRAY, STRUCT, NULL}

    private type t;
    private int tpoint;

    public int getTpoint() {
        return tpoint;
    }

    public type getType() {
        return t;
    }
}
