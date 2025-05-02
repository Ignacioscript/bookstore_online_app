package org.ignacioScript.co.service;

import org.ignacioScript.co.io.AuthorFileManager;
import org.ignacioScript.co.model.Author;
import org.ignacioScript.co.util.FileLogger;
import org.ignacioScript.co.validation.AuthorValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuthorService {

    private final AuthorFileManager authorFileManager;

    private static final String DEFAULT_FILE_PATH = "src/main/resources/authors.csv";

    public AuthorService() {
        this.authorFileManager = new AuthorFileManager(DEFAULT_FILE_PATH);
    }

    public AuthorService (AuthorFileManager authorFileManager) {
        this.authorFileManager = authorFileManager;
    }


    // SAVE
    public void saveAuthor(Author author) {
         validateAuthor(author);

         authorFileManager.appendAuthor(author);
        FileLogger.logInfo("AuthorService - Author saved successfully: " + author);
    }

    //UPDATE
    public void updateAuthor(Author author) {
        validateAuthor(author);

        Author existingAuthor = findAuthorById(author.getAuthorId());
        if (existingAuthor == null) {
            throw new IllegalArgumentException("Author with ID " + author.getAuthorId() + " does not exist");
        }

        authorFileManager.update(author);
        FileLogger.logInfo("AuthorService - Author updated successfully: " + author);
    }

    //DELETE
    public void deleteAuthor(int id) {

        Author existingAuthor = findAuthorById(id);
        if (existingAuthor == null ) {
            throw new IllegalArgumentException("Author with ID " + id + " does not exist");
        }

        authorFileManager.delete(id);
        FileLogger.logInfo("AuthorService - Author deleted successfully: " + existingAuthor);
    }


    //FIND ONE
    public Author findAuthorById(int id) {

        AuthorValidator.validateId(id);
        try {
            Author loadedAuthor = authorFileManager.getById(id);

            if (loadedAuthor == null) {
                throw new IllegalArgumentException("Author with ID: " + id + " does not exist");
            }
            FileLogger.logInfo("AuthorService - Successfully found author with ID: " + id);
            return loadedAuthor;
        }catch (RuntimeException e) {
            FileLogger.logError("AuthorService - Error finding author: " + e.getMessage());
            throw e;
        }

    }

    // FIND ALL
    public List<Author> getAllAuthors() {
        try {
            FileLogger.logInfo("AuthorService - Fetching all authors from file");
            return authorFileManager.load();
        }catch (IOException e) {
            FileLogger.logError("AuthorService - Error fetching authors: " + e.getMessage());
            throw new RuntimeException("Failed to load authors");
        }
    }

    //Bubble sort authors by last name
    public List<Author> sortAuthorsByLastName(List<Author> authors) {
        FileLogger.logInfo("Sorting authors by last name");
        for (int i = 0; i < authors.size() - 1; i++) {
            for (int j = 0; j < authors.size() - 1 - i; j++) {
                if (authors.get(j).getLastName().compareTo(authors.get(j + 1).getLastName()) > 0) {
                    Author temp = authors.get(j);
                    authors.set(j, authors.get(j + 1));
                    authors.set(j + 1, temp);
                }
            }
        }
        return authors;
    }

    //Bubble sort authors by first name
    public List<Author> sortAuthorsByFirstName(List<Author> authors) {
        FileLogger.logInfo("Sorting authors by first name");
        for (int i = 0; i < authors.size() - 1; i++) {
            for (int j = 0; j < authors.size() - 1 - i; j++) {
                if (authors.get(j).getFirstName().compareTo(authors.get(j + 1).getFirstName()) > 0) {
                    Author temp = authors.get(j);
                    authors.set(j, authors.get(j + 1));
                    authors.set(j + 1, temp);
                }
            }
        }
        return authors;
    }

    public List<Author> searchAuthorsByKeyword(String keyword) {
        List<Author> authors;
        try {
            // Load all authors
            authors = authorFileManager.load();

            // Create a fixed-size array to store matches
            Author[] matchedAuthors = new Author[authors.size()];
            int matchedIndex = 0;

            // Iterate over authors to find matches
            for (Author author : authors) {
                if (author.getFirstName().toLowerCase().contains(keyword.toLowerCase()) ||
                        author.getLastName().toLowerCase().contains(keyword.toLowerCase()) ||
                        author.getBio().toLowerCase().contains(keyword.toLowerCase())) {

                    // Add to array at next index
                    matchedAuthors[matchedIndex++] = author;
                }
            }

            // Convert back to a list for the return statement
            List<Author> result = new ArrayList<>();
            for (int i = 0; i < matchedIndex; i++) {
                result.add(matchedAuthors[i]);
            }
            return result;

        } catch (IOException e) {
            throw new RuntimeException("Failed to search authors: " + e.getMessage(), e);
        }
    }



    //Helper methods
    private void validateAuthor(Author author) {
        if (author == null) {
            throw new IllegalArgumentException("Author cannot be null");
        }

        AuthorValidator.validateId(author.getAuthorId());
        AuthorValidator.validateProperNoun(author.getFirstName());
        AuthorValidator.validateProperNoun(author.getLastName());
        AuthorValidator.validateProperNoun(author.getNationality());
        AuthorValidator.validateDescription(author.getBio(), 200, 20);
    }

    private boolean doesAuthorExist(int id) {
        return false;
    }
}
