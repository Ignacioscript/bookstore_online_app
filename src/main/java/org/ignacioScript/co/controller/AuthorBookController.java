package org.ignacioScript.co.controller;

import org.ignacioScript.co.model.Author;
import org.ignacioScript.co.model.AuthorBook;
import org.ignacioScript.co.model.Book;
import org.ignacioScript.co.service.AuthorBookService;
import org.ignacioScript.co.service.AuthorService;
import org.ignacioScript.co.service.BookService;

import java.util.List;
import java.util.Scanner;

public class AuthorBookController {

    private final AuthorBookService authorBookService;
    private final AuthorService authorService;
    private final BookService bookService;

    public AuthorBookController(AuthorBookService authorBookService, AuthorService authorService, BookService bookService) {
        this.authorBookService = authorBookService;
        this.authorService = authorService;
        this.bookService = bookService;
    }

    public void createAuthorBookRelationship(Scanner scanner) {
        try {
            // Step 1: Search or list books
            System.out.println("\nSearch for a book to relate:");
            System.out.print("Enter a keyword or leave empty to list all books: ");
            String bookKeyword = scanner.nextLine();
            List<Book> books;

            if (bookKeyword.isEmpty()) {
                books = bookService.getAllBooks(); // List all books if no keyword is entered
            } else {
                books = bookService.searchBooksByKeyword(bookKeyword);
            }

            if (books.isEmpty()) {
                System.out.println("No books found.");
                return;
            }

            // Display books
            System.out.println("\nAvailable Books:");
            for (Book book : books) {
                System.out.println(book.getBookId() + ": " + book.getBookTitle());
            }

            // Prompt user to select a book
            System.out.print("Enter the Book ID to relate: ");
            int bookId = Integer.parseInt(scanner.nextLine());
            Book selectedBook = bookService.findBookById(bookId);
            if (selectedBook == null) {
                System.out.println("Invalid Book ID!");
                return;
            }

            // Step 2: Search or list authors
            System.out.println("\nSearch for an author to relate:");
            System.out.print("Enter a keyword or leave empty to list all authors: ");
            String authorKeyword = scanner.nextLine();
            List<Author> authors;

            if (authorKeyword.isEmpty()) {
                authors = authorService.getAllAuthors(); // List all authors if no keyword is entered
            } else {
                authors = authorService.searchAuthorsByKeyword(authorKeyword);
            }

            if (authors.isEmpty()) {
                System.out.println("No authors found.");
                return;
            }

            // Display authors
            System.out.println("\nAvailable Authors:");
            for (Author author : authors) {
                System.out.println(author.getAuthorId() + ": " + author.getFirstName() + " " + author.getLastName());
            }

            // Prompt user to select an author
            System.out.print("Enter the Author ID to relate: ");
            int authorId = Integer.parseInt(scanner.nextLine());
            Author selectedAuthor = authorService.findAuthorById(authorId);
            if (selectedAuthor == null) {
                System.out.println("Invalid Author ID!");
                return;
            }

            // Step 3: Create and save the relationship
            AuthorBook authorBook = new AuthorBook(selectedBook, selectedAuthor);
            authorBookService.saveAuthorBook(authorBook); // Save the relationship
            System.out.println("\nRelationship created successfully:");
            System.out.println("Book: " + selectedBook.getBookTitle());
            System.out.println("Author: " + selectedAuthor.getFirstName() + " " + selectedAuthor.getLastName());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}