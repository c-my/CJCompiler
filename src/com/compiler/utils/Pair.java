package com.compiler.utils;

public class Pair<T, V> {
    public Pair(T fir, V sec) {
        first = fir;
        second = sec;
    }

    @Override
    public String toString() {
        return "(" +
                first.toString() +
                ", " +
                second.toString() +
                ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pair) {
            return first.equals(((Pair) obj).first) && second.equals(((Pair) obj).second);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return first.hashCode() ^ second.hashCode();
    }

    public T first;
    public V second;

}
