package com.compiler.parser;

import java.util.ArrayList;
import java.util.Arrays;

// 四元式
public class Quaternary {
    public Quaternary() {

    }

    public Quaternary(String opt, String opd1, String opd2, String opd3) {
        this.opd1 = opd1;
        this.opd2 = opd2;
        this.opd3 = opd3;
        this.opt = opt;
    }

    String opt;
    String opd1, opd2, opd3;

    public String getOpt() {
        return opt;
    }

    public ArrayList<String> getOpds() {
        return new ArrayList<String>(Arrays.asList(opd1, opd2, opd3));
    }
}
