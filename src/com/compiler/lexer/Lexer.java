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

    HashSet<Character> letters = new HashSet<>();

    HashSet<Character> empty = new HashSet<>();

    HashSet<Character> minus = new HashSet<>();
    HashSet<Character> add = new HashSet<>();
    HashSet<Character> u = new HashSet<>();
    HashSet<Character> l = new HashSet<>();
    HashSet<Character> f = new HashSet<>();

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
        letters.addAll(Arrays.asList(' ', '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3',
                '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
                'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[',
                '\\', ']', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
                'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~'));
    }

    private void initEmpty() {
    }

    private void initSymbol() {
        minus.add('-');
        add.add('+');
    }
}

class AcceptState {
    static int hex = -1;
    static int bin = -2;
    static int oct = -3;
    static int dec = -4;
    static int float_type = -5;
    static int double_type = -6;

    static HashSet<Integer> getAcceptStateSet() {
        HashSet<Integer> set = new HashSet<>();
        for (int i = -1; i >= -6; --i)
            set.add(i);
        return set;
    }
}

