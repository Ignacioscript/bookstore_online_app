package org.ignacioScript.co.seeder;

import org.ignacioScript.co.model.Book;
import org.ignacioScript.co.service.BookService;
import org.ignacioScript.co.validation.BookValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookSeeder {

    public static void seedBooks(BookService bookService) {
        try {
            // List of books to seed
            List<Book> seedBooks = List.of(
                    new Book(1,
                            "978-0439708180",
                            "Harry Potter and the Sorcerer's Stone",
                            "A magical journey of a young wizard's first year at Hogwarts.",
                            "Bloomsbury",
                            LocalDate.of(1997, 6, 26),
                            25.99,
                            100
                    ),
                    new Book(2,
                            "978-0261103573",
                            "The Hobbit",
                            "A classic tale of Bilbo Baggins a hobbit who travels on an incredible journey.",
                            "Allen & Unwin",
                            LocalDate.of(1937, 9, 21),
                            15.99,
                            50
                    ),
                    new Book(3,
                            "978-0141439518",
                            "Pride and Prejudice",
                            "Jane Austen explores the humorous side of romance and society.",
                            "Penguin Classics",
                            LocalDate.of(1813, 1, 28),
                            10.99,
                            200
                    ),
                    new Book(4,
                            "978-0061120084",
                            "To Kill a Mockingbird",
                            "A novel about racial injustice and moral character in a small American town.",
                            "Harper Perennial Modern Classics",
                            LocalDate.of(1960, 7, 11),
                            12.99,
                            70
                    ),
                    new Book(5,
                            "978-0743273565",
                            "The Great Gatsby",
                            "F. Scott Fitzgerald's masterpiece about wealth and the American dream.",
                            "Scribner",
                            LocalDate.of(1925, 4, 10),
                            14.99,
                            85
                    )
            );

            // Validating and saving each book
            for (Book book : seedBooks) {
                // Ensures all created books comply with validation rules
                 bookService.saveBook(book);// Saves each book using FileManager
            }

            System.out.println("Books seeded successfully!");

        } catch (Exception e) {
            System.err.println("Error seeding books: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
