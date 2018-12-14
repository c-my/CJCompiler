package com.compiler.lexer;


public class Token {

    enum tokenType {INTEGER, FLOAT, DOUBLE, CHAR, STR, IDENTIFIER, DELIMITER, KEYWORD, NONE}

    private String string = "";
    private tokenType type;

    public Token(String str, tokenType t) {
        setString(str);
        setType(t);
    }

    public void setString(String newStr) {
        string = newStr;
    }

    public void setType(tokenType t) {
        type = t;
    }

    public double getDouble() {
        return Double.parseDouble(string);
    }

    public double getInt() {
        return Integer.parseInt(string);
    }

    public String getStr() {
        return string;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Token) {
            return this.string.equals(((Token) obj).string) && this.type == ((Token) obj).type;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(string);
        sb.append(" - ");
        sb.append(type);
        sb.append("]");
        return sb.toString();
    }
}
