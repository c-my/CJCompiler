package com.compiler.parser;

import java.util.HashMap;
import java.util.HashSet;

abstract class LLParser {

    abstract HashMap<Symbol, HashSet<SymbolString>> getProductionRules();

    private HashSet<Symbol> getFirst(final SymbolString symstr) {
        var first = new HashSet<Symbol>();
        if (!symstr.isEmpty()) {
            // 左部为终结符
            var s = symstr.get(0);
            if (s.getType().equals(Symbol.SymbolType.Terminal)) {
                first.add(s);
                return first;
            }

            // 左部为非终结符
        }
        return first;
    }

    private HashSet<Symbol> getFirst(final Symbol sym) {
        HashSet<Symbol> first = new HashSet<>();
        if (sym.getType().equals(Symbol.SymbolType.Terminal)) {
            first.add(sym);
        } else {
            var rules = getProductionRules();
            if (!rules.keySet().contains(sym)) {
                return first;
            }
            var strs = rules.get(sym);
            // 遍历字符序列
            for (var i = strs.iterator(); i.hasNext(); ) {
                var f = getFirst(i.next());
                first.addAll(f);
            }
        }
        return first;
    }

    private HashSet<Symbol> getFollow(final Symbol sym) {
        return null;
    }

    private HashSet<Symbol> getSelece(final SymbolString symstr) {
        if (canProduceEmpty(symstr)) {
            final var rules = getProductionRules();
            var it = rules.keySet().stream().filter((statePair)->rules.get(statePair).contains(symstr)).findFirst();
            if(it.isEmpty())
                return new HashSet<>();
            var first = getFirst(symstr);
            var follow = getFollow(it.get());
            first.addAll(follow);
            return first;
        } else {
            return getFirst(symstr);
        }
    }

    private boolean canProduceEmpty(final SymbolString symstr) {
        for (Symbol o : (Iterable<Symbol>) symstr) {
            if (!canProduceEmpty(o)) {
                return false;
            }
        }
        return true;
    }

    private boolean canProduceEmpty(final Symbol sym) {
        var rules = getProductionRules();
        if(sym.getType().equals(Symbol.SymbolType.Nonterminal)){
            if(!rules.keySet().contains(sym)){ // 没有产生式
                return false;
            }
            var symstr = rules.get(sym);
            for(var st:symstr){
                if(st.isEmpty()){
                    return true;
                }
            }
            return false;
        }
        else return sym.getType().equals(Symbol.SymbolType.Empty);
    }
}
