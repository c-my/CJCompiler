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

    public static boolean setIntValue(String name, int val) {
        var result = SymbolTable.stream().filter(symbolTableItem -> symbolTableItem.getName().equals(name)).findFirst();
        if (result.isEmpty()) //没找到
            return false;
        var item = result.get();
        if (typeTabel.get(item.getTyp()).getType() != TypeLabelItem.type.INTEGER) //不是int
            return false;
        int index = item.getAddr();
        integerList.set(index, val);
        return true;
    }

    public static boolean setDoubleValue(String name, double val) {
        var result = SymbolTable.stream().filter(symbolTableItem -> symbolTableItem.getName().equals(name)).findFirst();
        if (result.isEmpty()) //没找到
            return false;
        var item = result.get();
        if (typeTabel.get(item.getTyp()).getType() != TypeLabelItem.type.FLOAT) //不是int
            return false;
        int index = item.getAddr();
        doubleList.set(index, val);
        return true;
    }

    public static boolean setCharValue(String name, char val) {
        var result = SymbolTable.stream().filter(symbolTableItem -> symbolTableItem.getName().equals(name)).findFirst();
        if (result.isEmpty()) //没找到
            return false;
        var item = result.get();
        if (typeTabel.get(item.getTyp()).getType() != TypeLabelItem.type.CHAR) //不是int
            return false;
        int index = item.getAddr();
        charList.set(index, val);
        return true;
    }

    //数组表
    public static ArrayList<ArrayTableItem> arrayTable = new ArrayList<>();

    //四元式表
    public static ArrayList<Quaternary> quaternaryList = new ArrayList<>();


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

    // 在符号表中查找名为name的符号并返回其类型，若符号表中不存在该符号则返回NULL类型
    public static TypeLabelItem.type getType(String name) {
        var first = SymbolTable.stream().filter(item -> item.getName().equals(name)).findFirst();
        if (first.isPresent()) {
            var item = first.get();
            return typeTabel.get(item.getTyp()).getType();
        }
        return TypeLabelItem.type.NULL;
    }

    //返回 名为name的符号 是 符号表中第几个 类型为type的 符号
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

    public static TypeLabelItem.type isNumber(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            try {
                Double.parseDouble(str);
            } catch (NumberFormatException ee) {
                return TypeLabelItem.type.NULL;
            }
            return TypeLabelItem.type.FLOAT;
        }
        return TypeLabelItem.type.INTEGER;
    }

}



