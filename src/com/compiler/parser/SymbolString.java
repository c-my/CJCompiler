package com.compiler.parser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class SymbolString implements Iterable {

    SymbolString(List<Symbol> list, int i) {
        symString = new ArrayList<>(list);
        id = i;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Symbol s:symString){
            sb.append(s.toString());
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof SymbolString){
            SymbolString ss = (SymbolString)obj;
            return this.id == ss.id && this.symString.equals(ss.symString);
        }
        return false;
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
        return symString.isEmpty()||
                (symString.get(0).getType().equals(Symbol.SymbolType.Empty));
    }

    public int size() {
        return symString.size();
    }

    public Symbol get(int index) {
        return symString.get(index);
    }

    public boolean contains(Symbol sym) {
        return symString.contains(sym);
    }

    public int indexOf(Symbol sym) {
        return symString.indexOf(sym);
    }

    public SymbolString subList(int fromindex, int toindex) {
        return new SymbolString(symString.subList(fromindex, toindex), this.id);
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
