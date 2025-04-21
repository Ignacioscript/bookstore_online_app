package org.ignacioScript.co.validation;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TextFileManagerValidator extends Validator{

    public static void  validateBookAtributes(String bookDetails) {
        if (bookDetails == null || bookDetails.trim().isEmpty()) {
            throw new IllegalArgumentException("library must have a valid book");
        }
        if (!bookDetails.matches("^[A-Za-z0-9\\s:,'-]+$")) {
            throw new IllegalArgumentException("Line contains invalid characters");
        }
    }

    public static void validateExistingFile(String filePath) throws FileNotFoundException {
        if (!Files.exists(Path.of(filePath))) {
            throw new FileNotFoundException(filePath + " does not exist");
        }
    }
}
