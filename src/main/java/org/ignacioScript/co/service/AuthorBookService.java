package org.ignacioScript.co.service;

import org.ignacioScript.co.io.AuthorBookFileManager;
import org.ignacioScript.co.model.AuthorBook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuthorBookService {

    private final AuthorBookFileManager authorBookFileManager;
    private final String DEFAULT_FILE_PATH = "src/main/resources/author_book.csv";
    private final String DEFAULT_BOOK_FILE_PATH = "src/main/resources/books.csv";
    private final String DEFAULT_AUTHOR_FILE_PATH = "src/main/resources/authors.csv";


    public AuthorBookService(AuthorBookFileManager authorBookFileManager) {
        this.authorBookFileManager = authorBookFileManager;
    }

    public AuthorBookService() {
        this.authorBookFileManager = new AuthorBookFileManager(DEFAULT_FILE_PATH, DEFAULT_BOOK_FILE_PATH, DEFAULT_AUTHOR_FILE_PATH);
    }

    // Save a single relationship
    public void saveAuthorBook(AuthorBook authorBook) {
        try {
            // Load existing relationships
            List<AuthorBook> authorBooks = authorBookFileManager.load();
            if (authorBooks == null) {
                authorBooks = new ArrayList<>();
            }

            // Add the new relationship
            authorBooks.add(authorBook);

            // Save back to the file
            authorBookFileManager.save(authorBooks);
        } catch (IOException e) {
            System.out.println("Error saving the author-book relationship: " + e.getMessage());
        }
    }

    // Load all relationships
    public List<AuthorBook> getAllAuthorBooks() {
        try {
            return authorBookFileManager.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load author-book relationships: " + e.getMessage(), e);
        }
    }
}