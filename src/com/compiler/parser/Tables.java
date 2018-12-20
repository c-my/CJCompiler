package com.compiler.parser;

import com.compiler.lexer.Token;

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

    //四元式表
    public static ArrayList<Quaternary> quaternaryList = new ArrayList<>();

    public static SymbolTableItem findSymbol(String name){
        var res = SymbolTable.stream().filter(item -> item.getName().equals(name)).findFirst();
        if(res.isPresent())
            return res.get();
        return new SymbolTableItem();
    }


    public static int getIntValue(String name) {
        var first = SymbolTable.stream().filter(item -> item.getName().equals(name)).findFirst();
        if (first.isPresent()) {
            var item = first.get();
            if (typeTabel.get(item.getTyp()).getType() == TypeLabelItem.type.INTEGER)
                return integerList.get(item.getAddr());
        }
        return Integer.MAX_VALUE;
    }

    public static double getDoubleValue(String name) {
        var first = SymbolTable.stream().filter(item -> item.getName().equals(name)).findFirst();
        if (first.isPresent()) {
            var item = first.get();
            if (typeTabel.get(item.getTyp()).getType() == TypeLabelItem.type.FLOAT)
                return doubleList.get(item.getAddr());
        }
        return Double.MAX_VALUE;
    }

    public static char getCharValue(String name) {
        var first = SymbolTable.stream().filter(item -> item.getName().equals(name)).findFirst();
        if (first.isPresent()) {
            var item = first.get();
            if (typeTabel.get(item.getTyp()).getType() == TypeLabelItem.type.CHAR) {
                return charList.get(item.getAddr());
            }
        }
        return ' ';
    }

    public static TypeLabelItem.type getType(String name) {
        var first = SymbolTable.stream().filter(item -> item.getName().equals(name)).findFirst();
        if (first.isPresent()) {
            var item = first.get();
            return typeTabel.get(item.getTyp()).getType();
        }
        return TypeLabelItem.type.NULL;
    }

    public static int getSymbolCount(String name, TypeLabelItem.type type) {
        int index = 0;
        for (SymbolTableItem item : SymbolTable) {
            if (typeTabel.get(item.getTyp()).getType() == type) {
                if (item.getName().equals(name)) {
                    return index;
                }
                ++index;
            }
        }
        return index;
    }

}

//// 类型表中的表项
//class TypeLabelItem {
//    public TypeLabelItem(type t) {
//        this.t = t;
//        switch (t) {
//            case CHAR:
//            case INTEGER:
//            case FLOAT:
//                this.tpoint = -1; //基本类型为null
//                break;
//            case ARRAY:
//                break;
//            case STRUCT:
//                break;
//        }
//    }
//
//    public enum type {INTEGER, FLOAT, CHAR, ARRAY, STRUCT, NULL}
//
//    private type t;
//    private int tpoint;
//
//    public int getTpoint() {
//        return tpoint;
//    }
//
//    public type getType() {
//        return t;
//    }
//}

// 符号总表中的表项
class SymbolTableItem {
    public SymbolTableItem(){

    }
    public SymbolTableItem(String name, Token.tokenType tokenType, cat_enum cat, String initVal) {
        this.name = name;
        this.cat = cat;
        switch (tokenType) {
            case CHAR:
                typ = 2;
                if (initVal.isEmpty())
                    Tables.charList.add(' ');
                else
                    Tables.charList.add(initVal.charAt(1));
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
    public ArrayTableItem(int up) {
        this.up = up;
    }

    private int low = 0;
    private int up;
    private int cpt;    //指向类型表 表示元素的类型
    private int clen;   //表示每个元素占据的单元个数
}
