package com.compiler.lexer;

import java.util.HashMap;
import java.util.HashSet;

public class Lexer extends Automaton {

    public Lexer(){
        super();
        transitionForm = new HashMap<>();
        acceptStates = new HashSet<>();
    }

    private HashMap<StatePair, Integer> transitionForm;
    private int startState = 0;
    private HashSet<Integer> acceptStates;

    @Override
    int getStartState() {
        return startState;
    }

    @Override
    HashSet<Integer> getAcceptStates() {
        return acceptStates;
    }

    @Override
    HashMap<StatePair, Integer> getTransitionForm() {
        return transitionForm;
    }

    private void addTransitionRule(int fromState, HashSet<Character> charSet, int toState){
        transitionForm.put(new StatePair(fromState, charSet), toState);
    }
}
