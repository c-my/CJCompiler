package com.compiler.lexer;

import java.util.HashMap;
import java.util.HashSet;

// 自动机
public class Automaton {

    private HashSet<Character> empty = new HashSet<>();
    HashMap<StatePair, Integer> transitionForm; // 状态转移表
    HashSet<Integer> acceptStates; // 终止状态
    int startState; //开始状态

    public int Run(String str) {
        int state = startState;
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
            return i;
        else {
            var key = getEmptyTrans(state);
            if (!key.isEmpty()) {
                state = transitionForm.get(key);
                if (acceptStates.contains(state)) {
                    return i;
                }
            }
        }
        return 0;
    }

    private StatePair hasNextState(int current, char ch) {
        var res = transitionForm.keySet().stream().filter(statePair -> statePair.current.equals(current) &&
                statePair.c.contains(ch)).findFirst();
        if (res.isPresent())
            return res.get();

        return new StatePair();
    }

    private StatePair getEmptyTrans(int current) {
        var res = transitionForm.keySet().stream().filter(statePair -> statePair.current.equals(current) &&
                statePair.c.isEmpty()).findFirst();
        if (res.isPresent())
            return res.get();
        return new StatePair();
    }

}

class StatePair {
    public Integer current;
    public HashSet<Character> c;

    StatePair(int s, HashSet<Character> ch) {
        current = s;
        c = ch;
    }

    StatePair() {
        current = -1;
        c = new HashSet<>();
    }

    public boolean isEmpty() {
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
