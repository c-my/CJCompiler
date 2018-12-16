package com.compiler.lexer;

import com.compiler.utils.CharSet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LexerTester {
    public LexerTester() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("res/lexer.c"), StandardCharsets.US_ASCII);
            generateTokenList(lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateTokenList(List<String> lines) {
        tokenList.clear();
        //TODO: 注释的检测
        for (String line : lines) {
            int index = 0, totalIndex = 0;
            String subLine = line;
            while (totalIndex < line.length()) {
                index = 0;
                // 跳过空
                if (subLine.charAt(index) == ' ' || subLine.charAt(index) == '\t') {
                    subLine = subLine.substring(1);
                    ++totalIndex;
                    continue;
                }
                // 检查关键字
                index = lexer.CheckKeyword(subLine);
                totalIndex += index;
                if (index == subLine.length())
                    break;
                else if (index != 0 && !isKeywordSuffix(subLine.charAt(index))) {
                    // 出错
                    System.out.println("Wrong keyword");
                    break;
                } else if (index != 0) {
                    tokenList.add(new Token(subLine.substring(0, index), Token.tokenType.KEYWORD));
                    subLine = subLine.substring(index);
                    continue;
                }
                // 检查界符
                index = lexer.CheckDelimiter(subLine);
                if (index != 0) {
                    tokenList.add(new Token(subLine.substring(0, index), Token.tokenType.DELIMITER));
                    subLine = subLine.substring(index);
                    totalIndex += index;
                    continue;
                }
                // 检查标识符、字符（串）、常数
                var resPair = lexer.Run(subLine);
                index = resPair.first;
                totalIndex += index;
                if (index != 0) {
                    var t = parseType(resPair.second);
                    tokenList.add(new Token(subLine.substring(0, index), t));
                    if (index != subLine.length() && !isNumSuffix(subLine.charAt(index))) {
                        System.out.println("identifier started with number or number with two dots");
                        break;
                    }
                    subLine = subLine.substring(index);
                }
            }
        }
    }

    public ArrayList<Token> getTokenList() {
        return tokenList;
    }

    private boolean isKeywordSuffix(Character ch) {
        return ch == ')' || ch == ' ' || ch == '\t'
                || ch == '[' || ch == '*';
    }

    private boolean isNumSuffix(Character ch) {
        return !(CharSet.ALPHABET.contains(ch) || ch == '.');
    }

    private Token.tokenType parseType(int state) {
        switch (state) {
            case AcceptState.char_type:
                return Token.tokenType.CHAR;
            case AcceptState.str:
                return Token.tokenType.STR;
            case AcceptState.identifier:
                return Token.tokenType.IDENTIFIER;
            case AcceptState.dec:
            case AcceptState.oct:
            case AcceptState.hex:
            case AcceptState.bin:
                return Token.tokenType.INTEGER;
            case AcceptState.float_type:
                return Token.tokenType.FLOAT;
            case AcceptState.double_type:
                return Token.tokenType.DOUBLE;
            default:
                return Token.tokenType.NONE;

        }
    }

    private ArrayList<Token> tokenList = new ArrayList<>();
    private Lexer lexer = new Lexer();
}


