package com.compiler.lexer;

import java.util.HashMap;
import java.util.HashSet;

public class ConstantChecker extends Automaton {

    ConstantChecker(){
        transitionForm.put(new StatePair(STATE0, decHeader), STATE1);
        transitionForm.put(new StatePair(STATE1, decNumber), STATE1);
        transitionForm.put(new StatePair(STATE1, euler), STATE3);
        transitionForm.put(new StatePair(STATE1, empty), INTEGER);
    }

    private final int STATE0 = 0;
    private final int STATE1 = 1;
    private final int STATE2 = 2;
    private final int STATE3 = 3;
    private final int STATE4 = 4;
    private final int STATE5 = 5;
    private final int STATE6 = 6;
    private final int STATE7 = 7;
    private final int INTEGER = 8;

    HashSet<Character> zero;
    HashSet<Character> hexX;
    HashSet<Character> decHeader;
    HashSet<Character> decNumber;
    HashSet<Character> octNumber;
    HashSet<Character> hexNumber;
    HashSet<Character> dot;
    HashSet<Character> euler;


    private HashMap<StatePair, Integer> transitionForm = new HashMap<>();
    private HashSet<Integer> acceptStates = new HashSet<>();

    @Override
    int getStartState() {
        return STATE0;
    }

    @Override
    HashSet<Integer> getAcceptStates() {
        return acceptStates;
    }

    @Override
    HashMap<StatePair, Integer> getTransitionForm() {
        return transitionForm;
    }
}
