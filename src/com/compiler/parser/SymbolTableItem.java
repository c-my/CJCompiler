package com.compiler.parser;

import com.compiler.lexer.Token;

// 符号总表中的表项
public class SymbolTableItem {
    public SymbolTableItem(String name, Token.tokenType tokenType, cat_enum cat, String initVal) {
        this.name = name;
        this.cat = cat;
        switch (tokenType) {
            case CHAR:
                typ = 2;
                if (initVal.isEmpty())
                    Tables.charList.add(' ');
                else {
                    if (initVal.charAt(1) == '\\') {
                        switch (initVal.charAt(2)) {
                            case 'n':
                                Tables.charList.add('\n');
                                break;
                            case 't':
                                Tables.charList.add('\t');
                                break;
                        }
                    } else {
                        Tables.charList.add(initVal.charAt(1));

                    }
                }

                addr = Tables.charList.size() - 1;
                break;
            case FLOAT:
            case DOUBLE:
                typ = 1;
                if (initVal.isEmpty())
                    Tables.doubleList.add(0.0);
                else
                    Tables.doubleList.add(Double.parseDouble(initVal));
                addr = Tables.doubleList.size() - 1;
                break;
            case INTEGER:
                typ = 0;
                if (initVal.isEmpty())
                    Tables.integerList.add(0);
                else
                    Tables.integerList.add(Integer.parseInt(initVal));
                addr = Tables.integerList.size() - 1;
                break;
            case NONE://其他类型（数组）
                typ = Tables.typeTabel.size();
                Tables.typeTabel.add(new TypeLabelItem(TypeLabelItem.type.ARRAY));
                break;
        }
    }

    public enum cat_enum {FUNC, CONSTANT, VARIABLE, TYPE}

    private String name;    // 符号名
    private int typ;        // 符号类型（在类型表中的索引）
    private int addr;       // 符号地址
    private cat_enum cat;   // 符号种类

    public cat_enum getCat() {
        return cat;
    }

    public int getAddr() {
        return addr;
    }

    public int getTyp() {
        return typ;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name: ");
        sb.append(name);
        sb.append("  type: ");
        sb.append(typ);
        sb.append("  value: ");
        switch (Tables.typeTabel.get(typ).getType()) {
            case INTEGER:
                sb.append(Tables.integerList.get(addr));
                break;
            case FLOAT:
                sb.append(Tables.doubleList.get(addr));
                break;
            case CHAR:
                sb.append(Tables.charList.get(addr));
                break;
        }
        return sb.toString();
    }
}

//数组表
class ArrayTableItem {
    public ArrayTableItem(int up, Token.tokenType type) {
        this.up = up;
    }

    private int low = 0;
    private int up;
    private int cpt;    //指向类型表 表示元素的类型
    private int clen;   //表示每个元素占据的单元个数
}
