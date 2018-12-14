package com.compiler.lexer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class LexerTester {
    public LexerTester() {
        Lexer lxer = new Lexer();
        try {
            List<String> lines = Files.readAllLines(Paths.get("res/lexer.c"), StandardCharsets.US_ASCII);
            for (String line : lines) {
                int index = 0, totalIndex = 0;
                String subLine = line;
                while (totalIndex < line.length() && index < subLine.length()) {
                    // 跳过空
                    if (subLine.charAt(index) == ' ' || subLine.charAt(index) == '\t') {
                        subLine = subLine.substring(1);
                        ++totalIndex;
                        continue;
                    }
                    // 检查关键字
                    index = lxer.CheckKeyword(subLine);
                    totalIndex += index;
                    if (index == subLine.length())
                        break;
                    else if (index != 0 && !isKeywordSuffix(subLine.charAt(index))) {
                        // 出错
                        System.out.println("Wrong keyword");
                        break;
                    } else if (index != 0) {
                        System.out.println(subLine.substring(0, index));
                        subLine = subLine.substring(index);
                        index = 0;
                        continue;
                    }
                    // 检查界符
                    index = lxer.CheckDelimiter(subLine);
                    if (index != 0) {
                        System.out.println(subLine.substring(0, index));
                        subLine = subLine.substring(index);
                        totalIndex += index;
                        index = 0;
                        continue;
                    }
                    // 检查标识符、字符（串）、常数
                    index = lxer.Run(subLine);
//                    System.out.println(index);
                    totalIndex += index;
                    if (index != 0) {
                        System.out.println(subLine.substring(0, index));
                        subLine = subLine.substring(index);
                        index = 0;
                    }
//                    System.out.print(line);
//                    System.out.print(": ");
//                    System.out.println(lxer.Run(line));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isKeywordSuffix(Character ch) {
        return ch == ')' || ch == ' ' || ch == '\t'
                || ch == '[' || ch == '*';
    }
}
