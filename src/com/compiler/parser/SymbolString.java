package com.compiler.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SymbolString implements Iterable {

    SymbolString(ArrayList<Symbol> list, int i) {
        symString = list;
        id = i;
    }


    public Iterator<Symbol> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<Symbol> {
        int cursor;
        int lastRet = -1;

        Itr() {
        }

        @Override
        public boolean hasNext() {
            return cursor != symString.size();
        }

        @Override
        public Symbol next() {
            int i = cursor;
            if (i >= symString.size())
                throw new NoSuchElementException();
            cursor = i + 1;
            return symString.get(lastRet = i);
        }
    }


    public boolean isEmpty() {
        return symString.isEmpty();
    }

    public Symbol get(int index) {
        return symString.get(index);
    }

    public int getId() {
        return id;
    }

    public void add(Symbol sym) {
        symString.add(sym);
    }

    private ArrayList<Symbol> symString;
    private int id;
}
