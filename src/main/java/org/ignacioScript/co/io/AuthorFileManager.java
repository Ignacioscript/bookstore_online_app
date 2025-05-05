package org.ignacioScript.co.io;

import org.ignacioScript.co.model.Author;
import org.ignacioScript.co.util.FileLogger;
import org.ignacioScript.co.validation.AuthorValidator;
import org.ignacioScript.co.validation.FileManagerValidator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages file operations for Author objects, including saving, loading, updating, deleting, and retrieving authors.
 */
public class AuthorFileManager extends FileManager<Author> {

    /**
     * Constructor to initialize the AuthorFileManager with a file path.
     * @param filePath The path to the file where authors are stored.
     */
    public AuthorFileManager(String filePath) {
        super(filePath);
        FileLogger.logInfo("Initialized AuthorFileManager for: " + filePath);
    }

    /**
     * Saves a list of authors to the file, overwriting any existing data.
     * @param authors The list of authors to save.
     */
    @Override
    public void save(List<Author> authors) {
        FileLogger.logInfo("Starting to save " + authors.size() + " authors");
        FileManagerValidator.validateExistingFile(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Author author : authors) {
                writer.write(objectToString(author));
                writer.newLine();
                FileLogger.logInfo("Author saved: " + author.getFirstName() + " " + author.getLastName());
            }
            FileLogger.logInfo("Successfully saved all authors");
        } catch (IOException e) {
            FileLogger.logError("ERROR saving authors: " + e.getMessage());
            throw new RuntimeException("Save operation failed");
        }
    }

    /**
     * Appends a single author to the file without overwriting existing data.
     * @param author The author to append.
     */
    public void appendAuthor(Author author) {
        FileLogger.logInfo("Starting to append a new Author");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(objectToString(author));
            writer.newLine();
            FileLogger.logInfo("AuthorFileManager - Append author operation success");
        } catch (IOException e) {
            FileLogger.logError("AuthorFileManager - Error appending a new author");
            throw new RuntimeException("Error appending a new author");
        }
    }

    /**
     * Loads all authors from the file into a list.
     * @return A list of authors loaded from the file.
     * @throws IOException If an error occurs while reading the file.
     */
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

    /**
     * Deletes an author from the file by their ID.
     * @param id The ID of the author to delete.
     */
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

    /**
     * Updates an existing author in the file.
     * @param author The updated author object.
     */
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

    /**
     * Retrieves an author by their ID.
     * @param id The ID of the author to retrieve.
     * @return The author with the specified ID, or null if not found.
     */
    @Override
    public Author getById(int id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Author author = stringToObject(line);

                if (author.getAuthorId() == id) {
                    FileLogger.logInfo("AuthorFileManager - Author found with ID: " + id);
                    return author;
                }
            }
            FileLogger.logInfo("AuthorFileManager - Author with ID: " + id + " Not found");
        } catch (IOException e) {
            FileLogger.logError("AuthorFileManager - Error reading file for getById: " + e.getMessage());
            throw new RuntimeException("Error while reading file to search for author with ID: " + id, e);
        }

        return null;
    }

    //TODO create a method to retrieve customer email or name that matches with a keyword to select ID
    //TODO create method to simplify creating a book and then am author

    /**
     * Converts an Author object to a string representation for file storage.
     * @param author The author to convert.
     * @return A string representation of the author.
     */
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

    /**
     * Converts a string representation of an author back into an Author object.
     * @param line The string representation of the author.
     * @return The Author object.
     */
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