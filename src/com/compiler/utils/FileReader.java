package com.compiler.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileReader {
    public static List<String> getLines(String file) {
        try {
            return Files.readAllLines(Paths.get(file), StandardCharsets.US_ASCII);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static List<String> getLines(File file) {
        List<String> result = new ArrayList<String>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                result.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<String> getLines(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        List<String> result = new ArrayList<>();
        try {
            while (reader.ready()) {
                result.add(reader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
