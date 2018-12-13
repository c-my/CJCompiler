package com.compiler.lexer;

import java.util.ArrayList;
import java.util.Arrays;

// 四元式
public class Quaternary {
    public Quaternary() {

    }

    public enum operation {ADD, SUB, MUL, DIV, EQUAL}

    operation opt;
    String opd1, opd2, opd3;

    public operation getOpt() {
        return opt;
    }

    public ArrayList<String> getOpds() {
        return new ArrayList<String>(Arrays.asList(opd1, opd2, opd3));
    }
}
