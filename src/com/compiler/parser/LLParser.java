package com.compiler.parser;

import com.compiler.utils.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

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

    public boolean Check(SymbolString str) {
        var form = getForm();
        Stack<Symbol> symStack = new Stack<>();

        str.add(endSym);
        symStack.push(endSym);
        symStack.push(getStartSymbol());

        int index = 0;
        while (index < str.size()) {
            var topSym = symStack.pop();
        }
        return false;
    }

    public boolean Check(ArrayList<Symbol> str) {
        var form = getForm();
        Stack<Symbol> symStack = new Stack<>();

        str.add(endSym);
        symStack.push(endSym);
        symStack.push(getStartSymbol());

        int index = 0;
        while (index < str.size()) {
            var topSym = symStack.pop();
            switch (topSym.getType()) {
                case Terminal:
                case Empty:
                    if (!topSym.equals(str.get(index))) {

                        return false;
                    }
                    ++index;
                    break;
                case Nonterminal:
                    final int index_tmp = index;
                    var res = form.keySet().stream().filter(pair -> pair.first.equals(topSym) && pair.second.equals(str.get(index_tmp))).findFirst();
                    if (res.isEmpty()) {
                        System.out.print(topSym);
                        System.out.print("::");
                        System.out.println(str.get(index));
                        System.out.print("Stack: ");
                        System.out.println(symStack);
                        return false;
                    }
                    var symStr = form.get(res.get());
                    System.out.print("Stack: "+symStack+"  ");
                    System.out.print(topSym + "->");
                    System.out.println(symStr);
                    for (int i = symStr.size() - 1; i >= 0; --i) {
                        if (symStr.get(i).getType() != Symbol.SymbolType.Empty)
                            symStack.push(symStr.get(i));
                    }
                    break;
                case End:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    protected HashMap<Pair<Symbol, Symbol>, SymbolString> getForm() {
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
            for (var it = 0; it + 1 < symstr.size(); ++it) {
                var sy = symstr.get(it);
                if (canProduceEmpty(sy)) {
                    var fi = getFirst(symstr.get(it + 1));
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
                var next = i.next();
                assert (!next.get(0).equals(sym)) : "not a ll1 grammar(has left recursion)";
                var f = getFirst(next);
                first.addAll(f);
            }
        }
        return first;
    }

    private HashSet<Symbol> getFollow(final Symbol sym) {
//        HashSet<Symbol> follow = new HashSet<>();
//        if (sym.getType().equals(Symbol.SymbolType.Terminal))
//            return follow;
//        if (sym.equals(getStartSymbol())) {
//            follow.add(endSym);
//        }
//        var rules = getProductionRules();
//        for (var r : rules.keySet()) {
//            var strs = rules.get(r);
//            for (var st : strs) {
//                if (st.contains(sym)) {
//                    if (st.indexOf(sym) != st.size() - 1) {// 该符号不位于产生式末尾
//                        Symbol next = st.get(st.indexOf(sym) + 1);
//                        if (next.getType().equals(Symbol.SymbolType.Terminal)) {
//                            follow.add(next);
//                        } else if (next.getType().equals(Symbol.SymbolType.Nonterminal)) {
//                            var sub = st.subList(st.indexOf(sym) + 1, st.size());
//                            var fo = getFirst(sub);
//                            follow.addAll(fo);
//                            if (canProduceEmpty(sub)) {
//                                if (!r.equals(sym)) {
//                                    var v = getFollow(r);
//                                    follow.addAll(v);
//                                }
//                            }
//                        }
//                    } else { // 该符号位于产生式末尾
//                        if (!r.equals(sym) && !isAtLast(sym, r)) {
//                            var fo = getFollow(r);
//                            follow.addAll(fo);
//                        }
//                    }
//                }
//            }
//        }
//        return follow;
        return getFollow(sym, new Symbol());
    }

    // 忽略以某一字符结尾的字符序列
    private HashSet<Symbol> getFollow(final Symbol sym, final Symbol exp) {
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
                        if (!r.equals(sym) && !r.equals(exp)) {
                            var fo = getFollow(r, sym);
                            follow.addAll(fo);
                        }
                    }
                }
            }
        }
        return follow;
    }

    protected HashSet<Symbol> getSelect(final SymbolString symstr) {
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

    private boolean isAtLast(Symbol key, Symbol val) {
        var strs = getProductionRules().get(key);
        for (var st : strs) {
            if (st.get(st.size() - 1).equals(val)) {
                return true;
            }
        }
        return false;
    }
}
