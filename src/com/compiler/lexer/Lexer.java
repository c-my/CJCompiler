package com.compiler.lexer;

import com.compiler.utils.StringSet;
import com.compiler.utils.CharSet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Lexer extends Automaton {

    public Lexer() {
        super();

        initTransitionForm();
    }

    public int CheckKeyword(String str) {
        int len = str.length();
        if (len < 2)
            return 0;
        for (int i = Math.min(8, len); i >= 2; --i) {
            var subString = str.substring(0, i);
            if (StringSet.KEYWORD.contains(subString)) {
                return i;
            }
        }
        return 0;
    }

    public int CheckDelimiter(String str) {
        if (str.isEmpty())
            return 0;
        int len = str.length();
        for (int i = Math.min(3, len); i >= 1; --i) {
            var sub = str.substring(0, i);
            if (StringSet.DELIMITER.contains(sub))
                return i;
        }
        return 0;
    }

    private HashMap<StatePair, Integer> transitionForm = new HashMap<>();

    private int startState = 0;

    private void initTransitionForm() {
        addTransitionRule(0, CharSet.ZERO, 1);

        addTransitionRule(1, CharSet.BINHEAD, 20);
        addTransitionRule(20, CharSet.BINNUM, 21);
        addTransitionRule(21, CharSet.BINNUM, 21);
        addTransitionRule(21, CharSet.EMPTY, AcceptState.bin);

        addTransitionRule(1, CharSet.HEXX, 22);
        addTransitionRule(22, CharSet.HEXNUM, 23);
        addTransitionRule(23, CharSet.HEXNUM, 23);
        addTransitionRule(23, empty, AcceptState.hex);

        addTransitionRule(1, CharSet.OCTNUM, 24);
        addTransitionRule(24, CharSet.OCTNUM, 24);
        addTransitionRule(24, CharSet.EMPTY, AcceptState.oct);

        addTransitionRule(1, CharSet.DOT, 3);

        addTransitionRule(0, CharSet.MINUS, 25);
        addTransitionRule(0, CharSet.ADD, 25);
        addTransitionRule(25, CharSet.ZERO, 3);
        addTransitionRule(25, CharSet.DECHEAD, 2);

        addTransitionRule(0, CharSet.DECHEAD, 2);
        addTransitionRule(2, CharSet.DECNUM, 2);
        addTransitionRule(2, CharSet.EMPTY, AcceptState.dec);


        addTransitionRule(2, CharSet.U, 8);
        addTransitionRule(8, CharSet.L, 10);
        addTransitionRule(10, CharSet.EMPTY, AcceptState.dec);

        addTransitionRule(2, CharSet.L, 9);
        addTransitionRule(9, CharSet.U, 11);
        addTransitionRule(11, CharSet.EMPTY, AcceptState.dec);

        addTransitionRule(2, CharSet.DOT, 3);
        addTransitionRule(3, CharSet.OCTNUM, 4);
        addTransitionRule(4, CharSet.OCTNUM, 4);
        addTransitionRule(4, CharSet.EMPTY, AcceptState.dec);

        addTransitionRule(4, CharSet.F, 12);
        addTransitionRule(12, CharSet.EMPTY, AcceptState.float_type);
        addTransitionRule(4, CharSet.EMPTY, AcceptState.float_type);

        addTransitionRule(4, CharSet.L, 14);
        addTransitionRule(14, CharSet.EMPTY, AcceptState.double_type);

        addTransitionRule(2, CharSet.EULER, 5);
        addTransitionRule(4, CharSet.EULER, 5);

        addTransitionRule(5, CharSet.MINUS, 7);
        addTransitionRule(5, CharSet.ADD, 7);
        addTransitionRule(5, CharSet.OCTNUM, 6);

        addTransitionRule(7, CharSet.DECNUM, 6);
        addTransitionRule(6, CharSet.DECNUM, 6);
        addTransitionRule(6, CharSet.EMPTY, AcceptState.dec);
        addTransitionRule(6, CharSet.L, 14);
        addTransitionRule(6, CharSet.F, 12);

        // 标识符
        addTransitionRule(0, CharSet.ALPHABET, 31);
        addTransitionRule(0, CharSet.UNDERLINE, 31);
        addTransitionRule(31, CharSet.ALPHABET, 32);
        addTransitionRule(31, CharSet.DECNUM, 32);
        addTransitionRule(31, CharSet.UNDERLINE, 32);
        addTransitionRule(31, CharSet.EMPTY, AcceptState.identifier);

        addTransitionRule(32, CharSet.ALPHABET, 32);
        addTransitionRule(32, CharSet.DECNUM, 32);
        addTransitionRule(32, CharSet.UNDERLINE, 32);
        addTransitionRule(32, CharSet.EMPTY, AcceptState.identifier);

        // 字符
        addTransitionRule(0, CharSet.SINGLEQUOTE, 33);
        // 单字符 'x'
        addTransitionRule(33, CharSet.ALPHABET, 34);
        addTransitionRule(33, CharSet.OCTNUM, 34);
        addTransitionRule(33, CharSet.SYMBOL, 34);
        addTransitionRule(33, CharSet.DOUBLEQUOTE, 34);
        addTransitionRule(34, CharSet.SINGLEQUOTE, 35);
        addTransitionRule(35, CharSet.EMPTY, AcceptState.char_type);
        // 转义字符
        addTransitionRule(33, CharSet.ESCAPE, 36);
        // 非数字转义
        addTransitionRule(36, CharSet.ESCAPECHAR, 38);
        addTransitionRule(36, CharSet.SINGLEQUOTE, 38);
        addTransitionRule(38, CharSet.SINGLEQUOTE, 35);
        // 8进制转义
        addTransitionRule(36, CharSet.OCTNUM, 39);
        addTransitionRule(39, CharSet.OCTNUM, 41);
        addTransitionRule(39, CharSet.SINGLEQUOTE, 35);
        addTransitionRule(41, CharSet.SINGLEQUOTE, 35);
        addTransitionRule(41, CharSet.OCTNUM, 49);
        addTransitionRule(49, CharSet.SINGLEQUOTE, 34);

        // 16进制转义
        addTransitionRule(36, CharSet.HEXX, 50);
        addTransitionRule(50, CharSet.HEXNUM, 51);
        addTransitionRule(51, CharSet.SINGLEQUOTE, 35);
        addTransitionRule(51, CharSet.HEXNUM, 52);
        addTransitionRule(52, CharSet.SINGLEQUOTE, 35);

        // 空字符 ''
        addTransitionRule(33, CharSet.SINGLEQUOTE, 37);
        addTransitionRule(37, CharSet.EMPTY, AcceptState.char_type);

        // 字符串
        addTransitionRule(0, CharSet.DOUBLEQUOTE, 60);
        addTransitionRule(60, CharSet.ALPHABET, 61);
        addTransitionRule(60, CharSet.OCTNUM, 61);
        addTransitionRule(60, CharSet.SYMBOL, 61);
        addTransitionRule(60, CharSet.SINGLEQUOTE, 61);
        addTransitionRule(61, CharSet.ALPHABET, 61);
        addTransitionRule(61, CharSet.OCTNUM, 61);
        addTransitionRule(61, CharSet.SYMBOL, 61);
        addTransitionRule(61, CharSet.SINGLEQUOTE, 61);
        addTransitionRule(61, CharSet.DOUBLEQUOTE, 62);
        addTransitionRule(62, CharSet.EMPTY, AcceptState.str);
        // 转义部分
        addTransitionRule(61, CharSet.ESCAPE, 63);
        addTransitionRule(60, CharSet.ESCAPE, 63);
        addTransitionRule(64, CharSet.ESCAPE, 63);
        // 非数字转义
        addTransitionRule(63, CharSet.ESCAPECHAR, 64);
        addTransitionRule(63, CharSet.DOUBLEQUOTE, 64);
        addTransitionRule(64, CharSet.DOUBLEQUOTE, 62);
        // 非数字转义回归
        addTransitionRule(64, CharSet.ALPHABET, 61);
        addTransitionRule(64, CharSet.OCTNUM, 61);
        addTransitionRule(64, CharSet.SYMBOL, 61);
        addTransitionRule(64, CharSet.SINGLEQUOTE, 61);
        // 8进制转义
        addTransitionRule(63, CharSet.OCTNUM, 65);
        addTransitionRule(65, CharSet.DOUBLEQUOTE, 62);
        addTransitionRule(65, CharSet.OCTNUM, 66);
        addTransitionRule(65, CharSet.DOUBLEQUOTE, 62);
        addTransitionRule(65, CharSet.ESCAPE, 63);
        addTransitionRule(66, CharSet.OCTNUM, 67);
        addTransitionRule(66, CharSet.ESCAPE, 63);
        addTransitionRule(67, CharSet.DOUBLEQUOTE, 62);
        addTransitionRule(67, CharSet.ESCAPE, 63);

        // 8进制回归
        addTransitionRule(65, CharSet.ALPHABET, 61);
        addTransitionRule(65, CharSet.OCTNUM, 61);
        addTransitionRule(65, CharSet.SYMBOL, 61);
        addTransitionRule(65, CharSet.SINGLEQUOTE, 61);
        addTransitionRule(66, CharSet.ALPHABET, 61);
        addTransitionRule(66, CharSet.OCTNUM, 61);
        addTransitionRule(66, CharSet.SYMBOL, 61);
        addTransitionRule(66, CharSet.SINGLEQUOTE, 61);
        addTransitionRule(67, CharSet.ALPHABET, 61);
        addTransitionRule(67, CharSet.OCTNUM, 61);
        addTransitionRule(67, CharSet.SYMBOL, 61);
        addTransitionRule(67, CharSet.SINGLEQUOTE, 61);

        // 16进制
        addTransitionRule(63, CharSet.HEXX, 68);
        addTransitionRule(68, CharSet.HEXNUM, 69);
        addTransitionRule(68, CharSet.DOUBLEQUOTE, 62);
        addTransitionRule(69, CharSet.HEXNUM, 70);
        addTransitionRule(69, CharSet.ESCAPE, 63);
        addTransitionRule(70, CharSet.ESCAPE, 63);
        addTransitionRule(70, CharSet.DOUBLEQUOTE, 62);

        // 16进制回归
        addTransitionRule(69, CharSet.ALPHABET, 61);
        addTransitionRule(69, CharSet.OCTNUM, 61);
        addTransitionRule(69, CharSet.SYMBOL, 61);
        addTransitionRule(69, CharSet.SINGLEQUOTE, 61);
        addTransitionRule(70, CharSet.ALPHABET, 61);
        addTransitionRule(70, CharSet.OCTNUM, 61);
        addTransitionRule(70, CharSet.SYMBOL, 61);
        addTransitionRule(70, CharSet.SINGLEQUOTE, 61);
        // 空串
        addTransitionRule(60, CharSet.DOUBLEQUOTE, 71);
        addTransitionRule(71, CharSet.EMPTY, AcceptState.str);

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


class AcceptState {
    final static int hex = -1;
    final static int bin = -2;
    final static int oct = -3;
    final static int dec = -4;
    final static int float_type = -5;
    final static int double_type = -6;
    final static int str = -7;
    final static int char_type = -8;
    final static int identifier = -9;

    static HashSet<Integer> getAcceptStateSet() {
        HashSet<Integer> set = new HashSet<>();
        for (int i = -1; i >= -9; --i)
            set.add(i);
        return set;
    }
}

