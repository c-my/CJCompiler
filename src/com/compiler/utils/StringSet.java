package com.compiler.utils;

import java.util.Arrays;
import java.util.HashSet;

public class StringSet {
    final public static HashSet<String> KEYWORD = new HashSet<>(Arrays.asList(
            "auto",
            "break",
            "case", "char", "const", "continue",
            "default",
            "do", "double",
            "else", "enum", "extern",
            "float", "for",
            "goto",
            "if", "int",
            "long",
            "print",
            "register", "return",
            "short", "signed", "sizeof", "static", "struct", "switch",
            "typedef",
            "union", "unsigned",
            "void", "volatile",
            "while"));
    final public static HashSet<String> unary_operator = new HashSet<>(Arrays.asList(
            "&", "*", "+", "-", "~", "!"));
    final public static HashSet<String> DELIMITER = new HashSet<>(Arrays.asList(
            "==", "!=", "<=", "<", ">=", ">",         // 关系运算符
            "+", "-", "*", "/", "%", "++", "--",                                // 算术运算符
            "&&", "||", "!",                                                    // 逻辑运算符
            "&", "|", "~", "^", "<<", ">>",                                     // 位操作运算符
            "=", "+=", "-=", "*=", "/=", "%=", "&=", "|=", "^=", "<<=", ">>=",    // 赋值运算符
            "?", ":",                                                           // 条件运算符
            "{", "}", "[", "]", "(", ")",
            ",",                                                                // 逗号运算符
            ";",
            "->", "."
    ));
}
