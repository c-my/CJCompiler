package com.compiler.parser;

import java.util.ArrayList;
import java.util.Arrays;

public class Tables {

    //符号总表
    public static ArrayList<SymbolTableItem> SymbolTable = new ArrayList<>();
    // 类型表 为基本类型初始化
    public static ArrayList<TypeLabelItem> typeTabel = new ArrayList<>(
            Arrays.asList(new TypeLabelItem(TypeLabelItem.type.INTEGER),
                    new TypeLabelItem(TypeLabelItem.type.FLOAT),
                    new TypeLabelItem(TypeLabelItem.type.CHAR)
            )
    );
    //基本数据类型的常量表
    public static ArrayList<Integer> integerList = new ArrayList<>();
    public static ArrayList<Double> doubleList = new ArrayList<>();
    public static ArrayList<Character> charList = new ArrayList<>();

}

// 类型表中的表项
class TypeLabelItem {
    public TypeLabelItem(type t) {
        this.t = t;
        switch (t) {
            case CHAR:
            case INTEGER:
            case FLOAT:
                this.tpoint = -1; //基本类型为null
                break;
            case ARRAY:
                break;
            case STRUCT:
                break;
        }
    }

    public enum type {INTEGER, FLOAT, CHAR, ARRAY, STRUCT}

    private type t;
    private int tpoint;

    public int getTpoint() {
        return tpoint;
    }

    public type getType() {
        return t;
    }
}

// 符号总表中的表项
class SymbolTableItem {
    public SymbolTableItem(Symbol sym, cat_enum cat) {
        this.name = sym.getId();
        this.cat = cat;
        switch (sym.getValueType()) {
            case CHAR:
                typ = 2;
                Tables.charList.add(sym.getValue().charAt(1));
                addr = Tables.charList.size() - 1;
                break;
            case FLOAT:
            case DOUBLE:
                typ = 1;
                Tables.doubleList.add(Double.parseDouble(sym.getValue()));
                addr = Tables.doubleList.size() - 1;
                break;
            case INTEGER:
                typ = 0;
                Tables.integerList.add(Integer.parseInt(sym.getValue()));
                addr = Tables.integerList.size() - 1;
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
}

//数组表
class ArrayTableItem {
    public ArrayTableItem(int up) {
        this.up = up;
    }

    private int low = 0;
    private int up;
    private int cpt;    //指向类型表 表示元素的类型
    private int clen;   //表示每个元素占据的单元个数
}
