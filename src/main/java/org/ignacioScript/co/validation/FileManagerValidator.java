package org.ignacioScript.co.validation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileManagerValidator extends Validator{


    public static void validateExistingFile(String filePath) {
        Path path = Path.of(filePath);
        try {

            if (!Files.exists(path)) {
                Files.createFile(path);
            }

        }catch (IOException e) {
            throw new RuntimeException("Failed to validate or create the file: " + filePath, e);
        }



    }
}
