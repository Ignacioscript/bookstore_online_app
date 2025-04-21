package org.ignacioScript.co.io;

import org.ignacioScript.co.validation.TextFileManagerValidator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TextFileManager {


    private final String  FILE_PATH;
    private List<String> lines;


    public TextFileManager(String FILE_PATH) {
        this.FILE_PATH = FILE_PATH;
        lines = new ArrayList<>();
    }

    public void writeLine(String line) throws FileNotFoundException {
        TextFileManagerValidator.validateExistingFile(FILE_PATH);
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(FILE_PATH))) {
            writer.write(line);
            writer.newLine();

        }catch (IOException e) {
            System.err.println("File not found");
            System.err.println(e.getMessage());
        }
    }

    public void appendLine(String line) throws IOException {
        TextFileManagerValidator.validateExistingFile(FILE_PATH);
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(FILE_PATH, true)
        )) {
            writer.write(line);
            writer.newLine();
        }
    }


    public List<String> readAllLines() throws IOException {
        TextFileManagerValidator.validateExistingFile(FILE_PATH);
       try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
           return reader.lines().collect(Collectors.toList());
       }
    }

}
