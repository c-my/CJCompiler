package com.compiler.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileReader {
    public static List<String> getLines(String file){
        try {
            return Files.readAllLines(Paths.get(file), StandardCharsets.US_ASCII);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
