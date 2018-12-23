package com.compiler.parser;

import com.compiler.lexer.Token;
import com.compiler.utils.Pair;

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
        Stack<Symbol> semStack = new Stack<>();
        Stack<Symbol> capacityStack = new Stack<>();
        Stack<Symbol> structStack = new Stack<>();

        String currentStruct = "";

        str.add(endSym);
        symStack.push(endSym);
        symStack.push(getStartSymbol());
        Symbol topSym = new Symbol();
        int index = 0;
        int tmpVariableIndex = 0;
        Symbol lastTopSym = new Symbol();
        while (index < str.size()) {
            topSym = symStack.pop();
            switch (topSym.getType()) {
                case Action:
                    switch (topSym.getaType()) {
                        case FILL:
                            Symbol name = semStack.pop();// 只能提供名字
                            Symbol type = semStack.pop();// 提供类型名
                            if (Tables.SymbolTable.stream().anyMatch(item -> item.getName().equals(name.getValue()))) {
                                System.out.println("redefine symbol " + name.getValue());
                                return false;
                            }
                            Token.tokenType tokenType = Token.tokenType.NONE;
                            switch (type.getId().toLowerCase()) {
                                case "int":
                                    tokenType = Token.tokenType.INTEGER;
                                    break;
                                case "double":
                                    tokenType = Token.tokenType.DOUBLE;
                                    break;
                                case "float":
                                    tokenType = Token.tokenType.FLOAT;
                                    break;
                                case "char":
                                    tokenType = Token.tokenType.CHAR;
                                    break;
                            }
                            Tables.SymbolTable.add(new SymbolTableItem(name.getValue(), tokenType, SymbolTableItem.cat_enum.VARIABLE, "", currentStruct));
                            break;
                        case FILL_I:
                            Symbol i_initVal = semStack.pop();
                            Symbol i_name = semStack.pop();// 只能提供名字
                            Symbol i_type = semStack.pop();// 提供类型名
                            Token.tokenType i_tokenType = Token.tokenType.NONE;
                            if (Tables.SymbolTable.stream().filter(item -> item.getName().equals(i_name.getValue())).findFirst().isPresent()) {
                                System.out.println("redefined symbol " + i_name.getValue());
                                return false;
                            }
                            switch (i_type.getId().toLowerCase()) {
                                case "int":
                                    i_tokenType = Token.tokenType.INTEGER;
                                    break;
                                case "double":
                                    i_tokenType = Token.tokenType.DOUBLE;
                                    break;
                                case "float":
                                    i_tokenType = Token.tokenType.FLOAT;
                                    break;
                                case "char":
                                    i_tokenType = Token.tokenType.CHAR;
                                    break;
                            }
                            if (i_initVal.getId().equals("CONSTANT"))
                                Tables.SymbolTable.add(new SymbolTableItem(i_name.getValue(), i_tokenType, SymbolTableItem.cat_enum.VARIABLE, i_initVal.getValue(), currentStruct));
                            else if (i_initVal.getId().equals("IDENTIFIER")) {
                                //在符号表中找到这个标识符的值
                                switch (Tables.getType(i_initVal.getValue())) {
                                    case INTEGER:
                                        Tables.SymbolTable.add(new SymbolTableItem(i_name.getValue(), i_tokenType, SymbolTableItem.cat_enum.VARIABLE, Integer.toString(Tables.getIntValue(i_initVal.getValue())), currentStruct));
                                        break;
                                    case FLOAT:
                                        Tables.SymbolTable.add(new SymbolTableItem(i_name.getValue(), i_tokenType, SymbolTableItem.cat_enum.VARIABLE, Double.toString(Tables.getDoubleValue(i_initVal.getValue())), currentStruct));
                                        break;
                                    case CHAR:
                                        Tables.SymbolTable.add(new SymbolTableItem(i_name.getValue(), i_tokenType, SymbolTableItem.cat_enum.VARIABLE, Character.toString(Tables.getCharValue(i_initVal.getValue())), currentStruct));
                                        break;
                                    case NULL:
                                        break;
                                }
                            }

                            break;
                        case PUSH:
                            semStack.push(lastTopSym);

                            break;
                        case GEQ:
                            Symbol opd2 = semStack.pop();
                            Symbol opt = semStack.pop();
                            Symbol opd1 = semStack.pop();
                            if (opt.getId().equals("=")) {
                                Tables.quaternaryList.add(new Quaternary(opt.getId(), opd2.getValue(), "", opd1.getValue()));
                            } else if (opt.getId().equals("+=") || opt.getId().equals("-=") || opt.getId().equals("*=") || opt.getId().equals("/=")) {
                                Tables.quaternaryList.add(new Quaternary(opt.getId().substring(0, 1), opd1.getValue(), opd2.getValue(), "t" + Integer.toString(tmpVariableIndex)));
                                Tables.quaternaryList.add(new Quaternary("=", "t" + Integer.toString(tmpVariableIndex), "", opd1.getValue()));
                                ++tmpVariableIndex;
                                semStack.push(opd1);
                            } else {
                                Tables.quaternaryList.add(new Quaternary(opt.getId(), opd1.getValue(), opd2.getValue(), "t" + Integer.toString(tmpVariableIndex)));
                                Symbol tmp = new Symbol(opd2.getId(), Symbol.SymbolType.Terminal, "t" + Integer.toString(tmpVariableIndex), Token.tokenType.DOUBLE);
                                semStack.push(tmp);
                                ++tmpVariableIndex;

                            }
                            break;
                        case GEQ_IF:
                            Symbol conditionSym = semStack.pop();
//                            Symbol ifSym = semStack.pop();
                            Tables.quaternaryList.add(new Quaternary("if", "", "", conditionSym.getValue()));
                            break;
                        case END_IF:
                            Tables.quaternaryList.add(new Quaternary("END_IF", "", "", ""));
                            break;
                        case BEGIN_ELSE:
                            Tables.quaternaryList.add(new Quaternary("BEGIN_ELSE", "", "", ""));
                            break;
                        case GEQ_WHILE:
                            Symbol cycleSym = semStack.pop();
                            Tables.quaternaryList.add(new Quaternary("while", "", "", cycleSym.getValue()));
                            break;
                        case END_WHILE:
                            Tables.quaternaryList.add(new Quaternary("END_WHILE", "", "", ""));
                            break;
                        case GEQ_BREAK:
                            Tables.quaternaryList.add(new Quaternary("BREAK", "", "", ""));
                            break;
                        case GEQ_CONTINUE:
                            Tables.quaternaryList.add(new Quaternary("CONTINUE", "", "", ""));
                            break;
                        case PUSH_CAPACITY:
                            capacityStack.push(lastTopSym);
                            break;
                        case GEQ_PRINT:
                            Symbol prntSym = semStack.pop();
                            Tables.quaternaryList.add(new Quaternary("PRINT", "", "", prntSym.getValue()));
                            break;
                        case FILL_EMPTY_ARRAY:
                            var emptyArrayCapacity = Integer.parseInt(capacityStack.pop().getValue());
                            var arrayTypeSym = semStack.pop();
                            var emptyArrIdentifier = semStack.pop();
                            ArrayList empArr;
                            switch (arrayTypeSym.getValue().toLowerCase()) {
                                case "int":
                                    empArr = new ArrayList<Integer>();
                                    break;
                                case "double":
                                case "float":
                                    empArr = new ArrayList<Double>();
                                    break;
                                case "char":
                                    empArr = new ArrayList<Character>();
                                    break;
                                default:
                                    return false;
                            }
                            for (int i = 0; i < emptyArrayCapacity; ++i) {
                                empArr.add(semStack.pop());
                            }
//                            System.out.println("Capacity: " + emptyArrayCapacity);
//                            System.out.println("Type: " + semStack.pop().getId());
                            Tables.SymbolTable.add(new SymbolTableItem(emptyArrIdentifier.getValue(), Token.tokenType.NONE, SymbolTableItem.cat_enum.TYPE, ""));
//                            Tables.arrayTable.add(new ArrayTableItem(Integer.parseInt(capacitySym.getValue())));
                            break;
                        case FILL_ARRAY:
                            var capacitysSym = Integer.parseInt(capacityStack.pop().getValue());
//                            System.out.println("Capacitys: " + capacitysSym);
//                            for (int i = 0; i < capacitysSym; ++i)
//                                System.out.print(semStack.pop().getValue() + " ");
//                            System.out.println();
                            break;
                        case BEGIN_STRUCT:

                            currentStruct = lastTopSym.getValue();
                            Tables.SymbolTable.add(new SymbolTableItem(currentStruct, Token.tokenType.NONE, SymbolTableItem.cat_enum.TYPE, ""));
                            break;
                        case END_STRUCUT:
                            currentStruct = "";
                            break;
                        case PUSH_MEMBER:
//                            Symbol memberSym = semStack.pop();
//                            Symbol belongstoSym = semStack.pop();
//                            var findRes = Tables.SymbolTable.stream().filter(item -> item.getName().equals(belongstoSym.getValue()) && item.getTyp() > 2).findFirst();
//                            if (findRes.isEmpty()) {
//                                System.out.println("No such variable!");
//                                return false;
//                            }
//                            findRes = Tables.SymbolTable.stream().filter(item -> item.getName().equals(memberSym.getValue())).findFirst();
//                            if (findRes.isEmpty()) {
//                                System.out.println("No such variable!");
//                                return false;
//                            }
//                            var shoudldBelongsTo = findRes.get().getBelongTo();
//                            if (!shoudldBelongsTo.equals(belongstoSym.getValue())) {
//                                System.out.println(belongstoSym.getValue() + " no such member");
//                                return false;
//                            }
//                            semStack.push(memberSym);
                            break;
                    }
                    continue;
                case Terminal:
                case Empty:
                    if (!topSym.equals(str.get(index))) {
                        System.out.print("top is:");
                        System.out.print(topSym);
                        System.out.println(" not " + str.get(index));

                        return false;
                    }
                    lastTopSym = str.get(index);
                    ++index;
                    break;
                case Nonterminal:
                    final int index_tmp = index;
                    final Symbol topSym_tmp = topSym;
                    var res = form.keySet().stream().filter(pair -> pair.first.equals(topSym_tmp) && pair.second.equals(str.get(index_tmp))).findFirst();
                    if (res.isEmpty()) {
                        System.out.print(topSym);
                        System.out.print("::");
                        System.out.println(str.get(index));
                        System.out.print("Stack: ");
                        System.out.println(symStack);
                        return false;
                    }
                    var symStr = form.get(res.get());
                    for (int i = symStr.size() - 1; i >= 0; --i) {
                        if (symStr.get(i).getType() != Symbol.SymbolType.Empty) {
                            symStack.push(symStr.get(i));
                        }
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
            var s = symstr.getFirstSymbol();
            if (s.getId().isEmpty())
                return first;
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
        if (sym.getType() == Symbol.SymbolType.Terminal) {
            first.add(sym);
        } else if (sym.getType() != Symbol.SymbolType.Action) {
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
        } /*else if (sym.getType() == Symbol.SymbolType.Action) {
            return true;
        } */ else return sym.getType() == Symbol.SymbolType.Empty;
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
