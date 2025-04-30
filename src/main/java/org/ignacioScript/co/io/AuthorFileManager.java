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
        FileLogger.logInfo("Initialized AuthorFileManager for: " + filePath);

    }


    @Override
    public void save(List<Author> authors) {
        FileLogger.logInfo("Starting to save " + authors.size() + " authors");
        FileManagerValidator.validateExistingFile(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Author author : authors) {
                writer.write(objectToString(author));
                writer.newLine();
                FileLogger.logInfo("author saved: " + author.getFirstName() + " " + author.getLastName());
            }
            FileLogger.logInfo("Successfully saved all authors");

        }catch (IOException e) {
            FileLogger.logError("ERROR saving authors: " + e.getMessage());
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
    public void delete(int id) {
        FileLogger.logInfo("Attempting to delete author with ID: " + id);
        try {
            List<Author> authors = load();
            boolean removed = authors.removeIf(author -> author.getAuthorId() == id);

            if (removed) {
                save(authors);
                FileLogger.logInfo("Successfully deleted author with ID: " + id);
            } else {
                FileLogger.logInfo("No author found with ID: " + id);
                throw new RuntimeException("Author with ID " + id + " not found");
            }
        } catch (IOException e) {
            FileLogger.logError("ERROR deleting author: " + e.getMessage());
            throw new RuntimeException("Failed to delete author: " + e.getMessage());
        }
    }

    @Override
    public void update(Author author) {
        FileLogger.logInfo("Attempting to update author with ID: " + author.getAuthorId());
        try {
            List<Author> authors = load();
            boolean updated = false;

            for (int i = 0; i < authors.size(); i++) {
                if (authors.get(i).getAuthorId() == author.getAuthorId()) {
                    authors.set(i, author);
                    updated = true;
                    break;
                }
            }

            if (updated) {
                save(authors);
                FileLogger.logInfo("Successfully updated author with ID: " + author.getAuthorId());
            } else {
                FileLogger.logInfo("No author found with ID: " + author.getAuthorId());
                throw new RuntimeException("Author with ID " + author.getAuthorId() + " not found");
            }
        } catch (IOException e) {
            FileLogger.logError("ERROR updating author: " + e.getMessage());
            throw new RuntimeException("Failed to update author: " + e.getMessage());
        }
    }

    // TODO crate logs and refactor by logInfo and LogError

    @Override
    public Author getById(int id) {

        try {
            for (Author author : load()) {
                if (author.getAuthorId() == id) {
                    return author;
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
        Author author = new Author(
                parts[1],
                parts[2],
                parts[3],
                parts[4]
        );
        author.setAuthorId(Integer.parseInt(parts[0]));
        return author;
    }


}
