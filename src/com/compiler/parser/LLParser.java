package com.compiler.parser;

import java.util.HashMap;
import java.util.HashSet;

abstract class LLParser {

    abstract HashMap<Symbol, HashSet<SymbolString>> getProductionRules();

    abstract Symbol getStartSymbol();

    @Override
    public String toString() {
        HashMap<Symbol, HashSet<SymbolString>> rules = getProductionRules();
        var keys = rules.keySet();
        StringBuilder sb = new StringBuilder();

        for (var key : keys) {
            sb.append(key.toString());
            sb.append("->");
            for (var s : rules.get(key)) {
                sb.append(s.toString());
                sb.append("|");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private Symbol endSym = new Symbol("#", Symbol.SymbolType.End);

    public HashMap<Pair<Symbol, Symbol>, SymbolString> getForm() {
        HashMap<Pair<Symbol, Symbol>, SymbolString> form = new HashMap<>();
        var rules = getProductionRules();
        for (var ru : rules.keySet()) {
            var strs = rules.get(ru);
            for (var st : strs) {
                var select = getSelect(st);
                for (var sym : select) {
                    form.put(new Pair<>(ru, sym), st);
                }
            }
        }
        return form;
    }

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
            first = getFirst(s);
            for (var it = symstr.iterator(); it.hasNext(); ) {
                var sy = it.next();
                if (canProduceEmpty(sy)) {
                    var fi = getFirst(sy);
                    first.addAll(fi);
                }
            }
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
        HashSet<Symbol> follow = new HashSet<>();
        if (sym.getType().equals(Symbol.SymbolType.Terminal))
            return follow;
        if (sym.equals(getStartSymbol())) {
            follow.add(endSym);
        }
        var rules = getProductionRules();
        for (var r : rules.keySet()) {
            var strs = rules.get(r);
            for (var st : strs) {
                if (st.contains(sym)) {
                    if (st.indexOf(sym) != st.size() - 1) {// 该符号不位于产生式末尾
                        Symbol next = st.get(st.indexOf(sym) + 1);
                        if (next.getType().equals(Symbol.SymbolType.Terminal)) {
                            follow.add(next);
                        } else if (next.getType().equals(Symbol.SymbolType.Nonterminal)) {
                            var sub = st.subList(st.indexOf(sym) + 1, st.size());
                            var fo = getFirst(sub);
                            follow.addAll(fo);
                            if (canProduceEmpty(sub)) {
                                if (!r.equals(sym)) {
                                    var v = getFollow(r);
                                    follow.addAll(v);
                                }
                            }
                        }
                    } else { // 该符号位于产生式末尾
                        if (!r.equals(sym)) {
                            var fo = getFollow(r);
                            follow.addAll(fo);
                        }
                    }
                }
            }
        }
        return follow;
    }

    private HashSet<Symbol> getSelect(final SymbolString symstr) {
        if (canProduceEmpty(symstr)) {
            final var rules = getProductionRules();
            var it = rules.keySet().stream().filter((statePair) -> rules.get(statePair).contains(symstr)).findFirst();
            if (it.isEmpty())
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
        if (sym.getType().equals(Symbol.SymbolType.Nonterminal)) {
            if (!rules.keySet().contains(sym)) { // 没有产生式
                return false;
            }
            var symstr = rules.get(sym);
            for (var st : symstr) {
                if (st.isEmpty()) {
                    return true;
                }
            }
            return false;
        } else return sym.getType().equals(Symbol.SymbolType.Empty);
    }
}
