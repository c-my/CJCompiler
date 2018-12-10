package com.compiler.parser;

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
    public int hashCode() {
        return first.hashCode() ^ second.hashCode();
    }

    public T first;
    public V second;

}
