package com.compiler.lexer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Lexer extends Automaton {

    public Lexer() {
        super();

        initTransitionForm();
    }

    private HashMap<StatePair, Integer> transitionForm = new HashMap<>();
    private int startState = 0;

    ParserSet charSet = new ParserSet();

    private void initTransitionForm() {
        addTransitionRule(0, charSet.zero, 1);

        addTransitionRule(1, charSet.binHeader, 20);
        addTransitionRule(20, charSet.binNumber, 21);
        addTransitionRule(21, charSet.binNumber, 21);
        addTransitionRule(21, charSet.empty, AcceptState.bin);

        addTransitionRule(1, charSet.hexX, 22);
        addTransitionRule(22, charSet.hexNumber, 23);
        addTransitionRule(23, charSet.hexNumber, 23);
        addTransitionRule(23, empty, AcceptState.hex);

        addTransitionRule(1, charSet.octNumebr, 24);
        addTransitionRule(24, charSet.octNumebr, 24);
        addTransitionRule(24, charSet.empty, AcceptState.oct);

        addTransitionRule(1, charSet.dot, 3);

        addTransitionRule(0, charSet.minus, 25);
        addTransitionRule(0, charSet.add, 25);
        addTransitionRule(25, charSet.zero, 3);
        addTransitionRule(25, charSet.decHeader, 2);

        addTransitionRule(0, charSet.decHeader, 2);
        addTransitionRule(2, charSet.decNumber, 2);
        addTransitionRule(2, charSet.empty, AcceptState.dec);


        addTransitionRule(2, charSet.u, 8);
        addTransitionRule(8, charSet.l, 10);
        addTransitionRule(10, charSet.empty, AcceptState.dec);

        addTransitionRule(2, charSet.l, 9);
        addTransitionRule(9, charSet.u, 11);
        addTransitionRule(11, charSet.empty, AcceptState.dec);

        addTransitionRule(2, charSet.dot, 3);
        addTransitionRule(3, charSet.octNumebr, 4);
        addTransitionRule(4, charSet.octNumebr, 4);
        addTransitionRule(4, charSet.empty, AcceptState.dec);

        addTransitionRule(4, charSet.f, 12);
        addTransitionRule(12, charSet.empty, AcceptState.float_type);
        addTransitionRule(4, charSet.empty, AcceptState.float_type);

        addTransitionRule(4, charSet.l, 14);
        addTransitionRule(14, charSet.empty, AcceptState.double_type);

        addTransitionRule(2, charSet.euler, 5);
        addTransitionRule(4, charSet.euler, 5);

        addTransitionRule(5, charSet.minus, 7);
        addTransitionRule(5, charSet.add, 7);
        addTransitionRule(5, charSet.octNumebr, 6);

        addTransitionRule(7, charSet.decNumber, 6);
        addTransitionRule(6, charSet.decNumber, 6);
        addTransitionRule(6, charSet.empty, AcceptState.dec);
        addTransitionRule(6, charSet.l, 14);
        addTransitionRule(6, charSet.f, 12);

        // 标识符
        addTransitionRule(0, charSet.alphabet, 31);
        addTransitionRule(0, charSet.underLine, 31);
        addTransitionRule(31, charSet.alphabet, 32);
        addTransitionRule(31, charSet.decNumber, 32);
        addTransitionRule(31, charSet.underLine, 32);
        addTransitionRule(32, charSet.alphabet, 32);
        addTransitionRule(32, charSet.decNumber, 32);
        addTransitionRule(32, charSet.underLine, 32);
        addTransitionRule(32, charSet.empty, AcceptState.identifier);

        // 字符
        addTransitionRule(0, charSet.singleQuotes, 33);
        // 单字符 'x'
        addTransitionRule(33, charSet.alphabet, 34);
        addTransitionRule(33, charSet.octNumebr, 34);
        addTransitionRule(33, charSet.symbols, 34);
        addTransitionRule(33, charSet.doubleQuotes, 34);
        addTransitionRule(34, charSet.singleQuotes, 35);
        addTransitionRule(35, charSet.empty, AcceptState.char_type);
        // 转义字符
        addTransitionRule(33, charSet.escape, 36);
        // 非数字转义
        addTransitionRule(36, charSet.escapeCharacter, 38);
        addTransitionRule(36, charSet.singleQuotes, 38);
        addTransitionRule(38, charSet.singleQuotes, 35);
        // 8进制转义
        addTransitionRule(36, charSet.octNumebr, 39);
        addTransitionRule(39, charSet.octNumebr, 41);
        addTransitionRule(39, charSet.singleQuotes, 35);
        addTransitionRule(41, charSet.singleQuotes, 35);
        addTransitionRule(41, charSet.octNumebr, 49);
        addTransitionRule(49, charSet.singleQuotes, 34);

        // 16进制转义
        addTransitionRule(36, charSet.hexX, 50);
        addTransitionRule(50, charSet.hexNumber, 51);
        addTransitionRule(51, charSet.singleQuotes, 35);
        addTransitionRule(51, charSet.hexNumber, 52);
        addTransitionRule(52, charSet.singleQuotes, 35);

        // 空字符 ''
        addTransitionRule(33, charSet.singleQuotes, 37);
        addTransitionRule(37, charSet.empty, AcceptState.char_type);

        // 字符串
        addTransitionRule(0, charSet.doubleQuotes, 60);
        addTransitionRule(60, charSet.alphabet, 61);
        addTransitionRule(60, charSet.octNumebr, 61);
        addTransitionRule(60, charSet.symbols, 61);
        addTransitionRule(60, charSet.singleQuotes, 61);
        addTransitionRule(61, charSet.alphabet, 61);
        addTransitionRule(61, charSet.octNumebr, 61);
        addTransitionRule(61, charSet.symbols, 61);
        addTransitionRule(61, charSet.singleQuotes, 61);
        addTransitionRule(61, charSet.doubleQuotes, 62);
        addTransitionRule(62, charSet.empty, AcceptState.str);
        // 转义部分
        addTransitionRule(61, charSet.escape, 63);
        addTransitionRule(60, charSet.escape, 63);
        addTransitionRule(64, charSet.escape, 63);
        // 非数字转义
        addTransitionRule(63, charSet.escapeCharacter, 64);
        addTransitionRule(63, charSet.doubleQuotes, 64);
        addTransitionRule(64, charSet.doubleQuotes, 62);
        // 非数字转义回归
        addTransitionRule(64, charSet.alphabet, 61);
        addTransitionRule(64, charSet.octNumebr, 61);
        addTransitionRule(64, charSet.symbols, 61);
        addTransitionRule(64, charSet.singleQuotes, 61);
        // 8进制转义
        addTransitionRule(63, charSet.octNumebr, 65);
        addTransitionRule(65, charSet.doubleQuotes, 62);
        addTransitionRule(65, charSet.octNumebr, 66);
        addTransitionRule(65, charSet.doubleQuotes, 62);
        addTransitionRule(65, charSet.escape, 63);
        addTransitionRule(66, charSet.octNumebr, 67);
        addTransitionRule(66, charSet.escape, 63);
        addTransitionRule(67, charSet.doubleQuotes, 62);
        addTransitionRule(67, charSet.escape, 63);

        // 8进制回归
        addTransitionRule(65, charSet.alphabet, 61);
        addTransitionRule(65, charSet.octNumebr, 61);
        addTransitionRule(65, charSet.symbols, 61);
        addTransitionRule(65, charSet.singleQuotes, 61);
        addTransitionRule(66, charSet.alphabet, 61);
        addTransitionRule(66, charSet.octNumebr, 61);
        addTransitionRule(66, charSet.symbols, 61);
        addTransitionRule(66, charSet.singleQuotes, 61);
        addTransitionRule(67, charSet.alphabet, 61);
        addTransitionRule(67, charSet.octNumebr, 61);
        addTransitionRule(67, charSet.symbols, 61);
        addTransitionRule(67, charSet.singleQuotes, 61);

        // 16进制
        addTransitionRule(63, charSet.hexX, 68);
        addTransitionRule(68, charSet.hexNumber, 69);
        addTransitionRule(68, charSet.doubleQuotes, 62);
        addTransitionRule(69, charSet.hexNumber, 70);
        addTransitionRule(69, charSet.escape, 63);
        addTransitionRule(70, charSet.escape, 63);
        addTransitionRule(70, charSet.doubleQuotes, 62);

        // 16进制回归
        addTransitionRule(69, charSet.alphabet, 61);
        addTransitionRule(69, charSet.octNumebr, 61);
        addTransitionRule(69, charSet.symbols, 61);
        addTransitionRule(69, charSet.singleQuotes, 61);
        addTransitionRule(70, charSet.alphabet, 61);
        addTransitionRule(70, charSet.octNumebr, 61);
        addTransitionRule(70, charSet.symbols, 61);
        addTransitionRule(70, charSet.singleQuotes, 61);
        // 空串
        addTransitionRule(60, charSet.doubleQuotes, 71);
        addTransitionRule(71, charSet.empty, AcceptState.str);

    }

    @Override
    int getStartState() {
        return startState;
    }

    @Override
    HashSet<Integer> getAcceptStates() {
        return AcceptState.getAcceptStateSet();
    }

    @Override
    HashMap<StatePair, Integer> getTransitionForm() {
        return transitionForm;
    }

    private void addTransitionRule(int fromState, HashSet<Character> charSet, int toState) {
        transitionForm.put(new StatePair(fromState, charSet), toState);
    }
}

class ParserSet {

    public ParserSet() {
        initNumberSet();
        initLetters();
        initSymbol();
        initEmpty();
    }

    HashSet<Character> zero = new HashSet<>();
    HashSet<Character> binNumber = new HashSet<>();
    HashSet<Character> binHeader = new HashSet<>();
    HashSet<Character> decNumber = new HashSet<>();   //十进制
    HashSet<Character> decHeader = new HashSet<>();
    HashSet<Character> octNumebr = new HashSet<>();   //八进制
    HashSet<Character> hexNumber = new HashSet<>();
    HashSet<Character> hexX = new HashSet<>();
    HashSet<Character> dot = new HashSet<>();
    HashSet<Character> euler = new HashSet<>();

    HashSet<Character> symbols = new HashSet<>();
    HashSet<Character> lettersWithoutNum = new HashSet<>();

    HashSet<Character> empty = new HashSet<>();

    HashSet<Character> minus = new HashSet<>();
    HashSet<Character> add = new HashSet<>();
    HashSet<Character> u = new HashSet<>();
    HashSet<Character> l = new HashSet<>();
    HashSet<Character> f = new HashSet<>();

    HashSet<Character> underLine = new HashSet<>();
    HashSet<Character> singleQuotes = new HashSet<>();
    HashSet<Character> doubleQuotes = new HashSet<>();
    HashSet<Character> escapeCharacter = new HashSet<>();
    HashSet<Character> alphabet = new HashSet<>();

    HashSet<Character> escape = new HashSet<>();


    private void initNumberSet() {
        zero.add('0');
        binNumber.addAll(Arrays.asList('0', '1'));
        binHeader.addAll(Arrays.asList('b', 'B'));
        decNumber.addAll(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
        decHeader.addAll(Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9'));
        octNumebr.addAll(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7'));
        hexNumber.addAll(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'a', 'b', 'c', 'd', 'e', 'f'));
        hexX.addAll(Arrays.asList('x', 'X'));
        dot.add('.');
        euler.addAll(Arrays.asList('e', 'E'));
        u.addAll(Arrays.asList('U', 'u'));
        l.addAll(Arrays.asList('L', 'l'));
        f.addAll(Arrays.asList('F', 'f'));
    }

    private void initLetters() {
        symbols.addAll(Arrays.asList(' ', '!', '#', '$', '%', '&', '(', ')', '*', '+', ',', '-', '.', '/',
                ':', ';', '<', '=', '>', '?', '@', '[', ']', '^', '`', '{', '|', '}', '~'));
//        lettersWithoutNum.addAll(Arrays.asList(' ', '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-',
//                '.', '/', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
//                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^', '_',
//                '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
//                't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~'));
        alphabet.addAll(Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
                'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'));
        escapeCharacter.addAll(Arrays.asList('a', 'b', 'f', 'n', 'r', 't', 'v'));
    }

    private void initEmpty() {
    }

    private void initSymbol() {
        minus.add('-');
        add.add('+');
        underLine.add('_');
        singleQuotes.add('\'');
        doubleQuotes.add('"');
        escape.add('\\');
    }
}

class AcceptState {
    static int hex = -1;
    static int bin = -2;
    static int oct = -3;
    static int dec = -4;
    static int float_type = -5;
    static int double_type = -6;
    static int str = -7;
    static int char_type = -8;
    static int identifier = -9;

    static HashSet<Integer> getAcceptStateSet() {
        HashSet<Integer> set = new HashSet<>();
        for (int i = -1; i >= -9; --i)
            set.add(i);
        return set;
    }
}

