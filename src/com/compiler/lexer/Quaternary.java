package com.compiler.lexer;

// 四元式
public class Quaternary {
    public Quaternary() {

    }

    enum operation {ADD, SUB, MUL, DIV, EQUAL}

    operation opt;
    String opd1, opd2, opd3;
}
