package com.compiler.utils;

import java.util.Arrays;
import java.util.HashSet;

public class CharSet {
    final public static HashSet<Character> ALPHABET = new HashSet<>(Arrays.asList(
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'));
    final public static HashSet<Character> ESCAPECHAR = new HashSet<>(Arrays.asList('a', 'b', 'f', 'n', 'r', 't', 'v'));

    final public static HashSet<Character> ZERO = new HashSet<>(Arrays.asList('0'));

    final public static HashSet<Character> BINHEAD = new HashSet<>(Arrays.asList('b', 'B'));
    final public static HashSet<Character> BINNUM = new HashSet<>(Arrays.asList('0', '1'));

    final public static HashSet<Character> DECNUM = new HashSet<>(Arrays.asList(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
    final public static HashSet<Character> DECHEAD = new HashSet<>(Arrays.asList(
            '1', '2', '3', '4', '5', '6', '7', '8', '9'));

    final public static HashSet<Character> OCTNUM = new HashSet<>(Arrays.asList(
            '0', '1', '2', '3', '4', '5', '6', '7'
    ));

    final public static HashSet<Character> HEXNUM = new HashSet<>(Arrays.asList(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
            'a', 'b', 'c', 'd', 'e', 'f'
    ));

    final public static HashSet<Character> HEXX = new HashSet<>(Arrays.asList('x', 'X'));

    final public static HashSet<Character> EULER = new HashSet<>(Arrays.asList('e', 'E'));

    final public static HashSet<Character> SYMBOL = new HashSet<>(Arrays.asList(
            ' ', '!', '#', '$', '%', '&', '(', ')', '*', '+', ',', '-', '.', '/',
            ':', ';', '<', '=', '>', '?', '@', '[', ']', '^', '`', '{', '|', '}', '~'
    ));

    final public static HashSet<Character> U = new HashSet<>(Arrays.asList('u', 'U'));
    final public static HashSet<Character> L = new HashSet<>(Arrays.asList('l', 'L'));
    final public static HashSet<Character> F = new HashSet<>(Arrays.asList('f', 'F'));
    final public static HashSet<Character> UNDERLINE = new HashSet<>(Arrays.asList('_'));
    final public static HashSet<Character> DOT = new HashSet<>(Arrays.asList('.'));
    final public static HashSet<Character> MINUS = new HashSet<>(Arrays.asList('-'));
    final public static HashSet<Character> ADD = new HashSet<>(Arrays.asList('+'));
    final public static HashSet<Character> SINGLEQUOTE = new HashSet<>(Arrays.asList('\''));
    final public static HashSet<Character> DOUBLEQUOTE = new HashSet<>(Arrays.asList('"'));
    final public static HashSet<Character> ESCAPE = new HashSet<>(Arrays.asList('\\'));
    final public static HashSet<Character> EMPTY = new HashSet<>();
}
