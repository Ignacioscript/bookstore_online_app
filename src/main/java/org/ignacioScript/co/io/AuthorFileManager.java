package org.ignacioScript.co.io;

import org.ignacioScript.co.model.Author;
import org.ignacioScript.co.util.FileLogger;
import org.ignacioScript.co.validation.FileManagerValidator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorFileManager  extends FileManager <Author> {


    public AuthorFileManager(String filePath) {
        super(filePath);
        FileLogger.log("Initialized AuthorFileManager for: " + filePath);

    }

    //TODO finish FIleManager for all the models

    @Override
    public void save(List<Author> authors) {
        FileLogger.log("Starting to save " + authors.size() + " authors");
        FileManagerValidator.validateExistingFile(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Author author : authors) {
                writer.write(objectToString(author));
                writer.newLine();
                FileLogger.log("author saved: " + author.getFirstName() + " " + author.getLastName());
            }
            FileLogger.log("Successfully saved all authors");

        }catch (IOException e) {
            FileLogger.log("ERROR saving authors: " + e.getMessage());
            throw  new RuntimeException("Save operation failed");
        }
    }

    @Override
    public List<Author> load() throws IOException {
        List<Author> authors = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                authors.add(stringToObject(line));
            }
        }

        return authors;
    }

    @Override
    protected Author getById(int id) {

        try {
            for (Author author : load()) {
                if (author.getAuthorId() == id) {
                    return author;
                } else {
                    System.err.println("Author not founded");
                }
            }

        }catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }


        return null;
    }

    @Override
    protected String objectToString(Author author) {
        return String.join(",",
        String.valueOf(author.getAuthorId()),
        author.getFirstName(),
        author.getLastName(),
        author.getBio(),
        author.getNationality()
        );
    }

    @Override
    protected Author stringToObject(String line) {
        String[] parts = line.split(",");
        return new Author(
                parts[1],
                parts[2],
                parts[3],
                parts[4]
        );
    }


}
