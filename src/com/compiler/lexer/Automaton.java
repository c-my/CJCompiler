package com.compiler.lexer;

import com.compiler.utils.Pair;

import java.util.HashMap;
import java.util.HashSet;

// 自动机
abstract class Automaton {

    Automaton() {
    }

    HashSet<Character> empty = new HashSet<>();

    // 返回开始状态
    abstract int getStartState();

    // 返回终止状态
    abstract HashSet<Integer> getAcceptStates();

    // 返回状态转移表
    abstract HashMap<StatePair, Integer> getTransitionForm();

    // 从开始状态开始检查，返回最终的状态
    public Pair<Integer, Integer> Run(String str) {
        var transitionForm = getTransitionForm();
        var acceptStates = getAcceptStates();
        int state = getStartState();

        int i = 0;
        while (i != str.length()) {
            final int cc = state;
            final char c = str.charAt(i);
            var key = hasNextState(state, str.charAt(i));
            if (!key.isEmpty()) { //非空转移
                state = transitionForm.get(key);
                ++i;
            } else {
                key = getEmptyTrans(state);
                if (!key.isEmpty()) { // 空转移
                    state = transitionForm.get(key);
                } else {
                    break;
                }
            }
        }
        if (acceptStates.contains(state))
            return new Pair<>(i, state);
        else {
            var key = getEmptyTrans(state);
            if (!key.isEmpty()) {
                state = transitionForm.get(key);
                if (acceptStates.contains(state)) {
                    return new Pair<>(i, state);
                }
            }
        }
        return new Pair<>(0,0);
    }

    // 返回状态转移表中是否有下一个状态
    private StatePair hasNextState(int current, char ch) {
        var transitionForm = getTransitionForm();
        var res = transitionForm.keySet().stream().filter(statePair -> statePair.current.equals(current) &&
                statePair.c.contains(ch)).findFirst();
        return res.orElseGet(StatePair::new);

    }

    private StatePair getEmptyTrans(int current) {
        var transitionForm = getTransitionForm();
        var res = transitionForm.keySet().stream().filter(statePair -> statePair.current.equals(current) &&
                statePair.c.isEmpty()).findFirst();
        return res.orElseGet(StatePair::new);
    }


    class StatePair {
        Integer current;
        HashSet<Character> c;

        StatePair(int s, HashSet<Character> ch) {
            current = s;
            c = ch;
        }

        StatePair() {
            current = -1;
            c = new HashSet<>();
        }

        boolean isEmpty() {
            return current.equals(-1);
        }

        @Override
        public int hashCode() {
            var h1 = current.hashCode();
            var h2 = ((Integer) c.size()).hashCode();
            return h1 ^ h2;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof StatePair) {
                StatePair sp = (StatePair) obj;
                return (this.c == sp.c && this.current.equals(sp.current));
            }
            return false;
        }
    }
}


