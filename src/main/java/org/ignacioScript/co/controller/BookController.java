package org.ignacioScript.co.controller;

import org.ignacioScript.co.model.Book;
import org.ignacioScript.co.seeder.BookSeeder;
import org.ignacioScript.co.service.AuthorService;
import org.ignacioScript.co.service.BookService;
import org.ignacioScript.co.util.FileLogger;
import org.ignacioScript.co.validation.BookValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BookController {

    private BookService bookService;
    private static Scanner scanner;
    private AuthorService authorService;
    private Book book;


    public BookController(Scanner scanner, BookService bookService) {
        this.bookService = bookService;
        this.scanner = scanner;
    }

    public BookController() {

    }

    public  void displayBookMenu() {
        while (true) {
            displayMenuOption();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 0 ->  BookSeeder.seedBooks(bookService);
                case 1 -> createBook();
                case 2 -> viewBooks();
                case 3 -> searchForBook();
                case 4 -> updateBook();
                case 5 -> deleteBook();
                case 6 -> {
                    System.out.println("Returning to Main Menu...\n");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.\n");
            }

        }
    }

    private void displayMenuOption() {
        System.out.println("\n===== Manage Books =====");
        System.out.println("0. Seed Books");
        System.out.println("1. Create Book");
        System.out.println("2. View All Books");
        System.out.println("3. Find Book by ID");
        System.out.println("4. Update Book");
        System.out.println("5. Delete Book");
        System.out.println("6. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }






    private  void createBook() {
        FileLogger.logApp("BookController - Creating a new book");
        try {

            String isbn;
            while (true) {
                System.out.println("Enter ISBN:");

                try {
                    isbn = scanner.nextLine();
                    BookValidator.validateIsbn(isbn);
                    break;
                }catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }


            String bookTitle;
            while (true) {
                System.out.println("Enter book title:");

                try {
                    bookTitle = scanner.nextLine();
                    BookValidator.validateTitle(bookTitle);
                    break;
                }catch (Exception e) {
                    FileLogger.logError("BookController - input error BookValidator.validateTitle");
                    System.out.println("Error " + e.getMessage());
                }

            }

            String bookDescription;
            while (true) {
                System.out.println("Enter book description:");
                bookDescription = scanner.nextLine();
                try {
                    BookValidator.validateDescription(bookDescription, 100, 20);
                    break;
                }catch (Exception e) {
                    FileLogger.logError("BookController - input error BookValidator.validateDescription");
                    System.err.println("Error " + e.getMessage());
                }

            }

            String bookPublisher;
            while (true) {
                System.out.println("Enter book publisher:");
                bookPublisher = scanner.nextLine();
                try {
                    BookValidator.validatePublisher(bookPublisher);
                    break;
                }catch (Exception e) {
                    FileLogger.logError("BookController - input error BookValidator.validatePublisher");
                    System.err.println("Error " + e.getMessage());
                }

            }

            LocalDate publicationDate;
            while (true) {
                System.out.println("Enter publication date format (yyyy-mm-dd):");

                try {
                    publicationDate = LocalDate.parse(scanner.nextLine());
                    BookValidator.validatePublicationDate(publicationDate);
                    break;
                }catch (Exception e) {
                    FileLogger.logError("BookController - input error BookValidator.validatePublicationDate");
                    System.err.println("Error invalid date format, please try again..." );
                }

            }

            double price;
            while (true) {
                System.out.println("Enter price:");

                try {
                    price = Double.parseDouble(scanner.nextLine());
                    BookValidator.validatePrice(price);
                    break;
                }catch (Exception e) {
                    FileLogger.logError("BookController - input error BookValidator.validatePrice");
                    System.err.println("Error " + e.getMessage());
                }

            }

            int stock;
            while (true) {
                System.out.println("Enter stock:");
                stock = Integer.parseInt(scanner.nextLine());
                try {
                    BookValidator.validateStock(stock);
                    break;
                }catch (Exception e) {
                    FileLogger.logError("BookController - input error BookValidator.validateStock");
                    System.err.println("Error " + e.getMessage());
                }

            }

            int bookId = getBookId() + 1;


            book = new Book(bookId ,isbn, bookTitle, bookDescription, bookPublisher, publicationDate, price, stock);
            bookService.saveBook(book);

            FileLogger.logApp("BookController - book created: " + book);
            System.out.println("Book created successfully! with ID: " + book.getBookId());
        } catch (Exception e) {
            FileLogger.logError("BookController - Error creating book: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private void sortBooks() {
        try {
            System.out.println("\nHow would you like to sort books?");
            System.out.println("1. Bubble Sort by Title");
            System.out.println("2. Quicksort by Title");
            System.out.print("Your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            List<Book> books = bookService.getAllBooks();

            if (choice == 1) {
                books = bookService.sortBooksByTitle(books);
            } else if (choice == 2) {
                bookService.quickSortBooksByTitle(books, 0, books.size() - 1);
            } else {
                System.out.println("Invalid choice!");
                return;
            }

            System.out.println("\nSorted Books:");
            for (Book book : books) {
                System.out.println(book.getBookId() + ": " + book.getBookTitle());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    private void searchForBook() {
        System.out.println("Enter book title or ISBN to search:");
        String keyword = scanner.nextLine();
        List<Book> books = bookService.searchBooksByKeyword(keyword);
        if (books.isEmpty()) {
            System.out.println("No books found.");
        } else {
            System.out.println("Books found:");
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }

    private void viewBooks() {
        FileLogger.logApp("BookController - Viewing all books");
        try {
            List<Book> books = bookService.getAllBooks();
            System.out.println("\n===== All Books =====");
            for (Book book : books) {
                System.out.println(book.toCsvString());
            }
        } catch (Exception e) {
            FileLogger.logError("BookController - Error viewing books: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private  void findBookById() {
        FileLogger.logApp("BookController - Finding book by ID");
      try {
          System.out.println("Enter Book ID to find:");
            int bookId = Integer.parseInt(scanner.nextLine());
            Book book = bookService.findBookById(bookId);
            if (book != null) {
                System.out.println("Book found: " + book);
            } else {
                System.out.println("Book not found with ID: " + bookId);
            }
      }catch (Exception e) {
            FileLogger.logError("BookController - Error finding book by ID: " + e.getMessage());
            throw new RuntimeException(e);
        }
      }


    private  void updateBook() {
        FileLogger.logApp("BookController - Updating a book");
        try {

            int bookId;
            while (true) {
                System.out.println("Enter Book ID to update:");
                bookId = Integer.parseInt(scanner.nextLine());
                try {
                    BookValidator.validateId(bookId);
                    break;
                }catch (Exception e) {
                    FileLogger.logError("BookController - update input error BookValidator.validateBookId");
                    System.out.println("Error " + e.getMessage());
                }
            }


            String isbn;
            while (true) {
                System.out.println("Enter ISBN:");
                isbn = scanner.nextLine();
                try {
                    BookValidator.validateIsbn(isbn);
                    break;
                }catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }


            String bookTitle;
            while (true) {
                System.out.println("Enter book title:");
                bookTitle = scanner.nextLine();
                try {
                    BookValidator.validateTitle(bookTitle);
                    break;
                }catch (Exception e) {
                    FileLogger.logError("BookController - input error BookValidator.validateTitle");
                    System.out.println("Error " + e.getMessage());
                }

            }

            String bookDescription;
            while (true) {
                System.out.println("Enter book description:");
                bookDescription = scanner.nextLine();
                try {
                    BookValidator.validateDescription(bookDescription, 100, 20);
                    break;
                }catch (Exception e) {
                    FileLogger.logError("BookController - input error BookValidator.validateDescription");
                    System.err.println("Error " + e.getMessage());
                }

            }

            String bookPublisher;
            while (true) {
                System.out.println("Enter book publisher:");
                bookPublisher = scanner.nextLine();
                try {
                    BookValidator.validatePublisher(bookPublisher);
                    break;
                }catch (Exception e) {
                    FileLogger.logError("BookController - input error BookValidator.validatePublisher");
                    System.err.println("Error " + e.getMessage());
                }

            }

            LocalDate publicationDate;
            while (true) {
                System.out.println("Enter publication date format (yyyy-mm-dd):");

                try {
                    publicationDate = LocalDate.parse(scanner.nextLine());
                    BookValidator.validatePublicationDate(publicationDate);
                    break;
                }catch (Exception e) {
                    FileLogger.logError("BookController - input error BookValidator.validatePublicationDate");
                }

            }

            double price;
            while (true) {
                System.out.println("Enter price:");
                price = Double.parseDouble(scanner.nextLine());
                try {
                    BookValidator.validatePrice(price);
                    break;
                }catch (Exception e) {
                    FileLogger.logError("BookController - input error BookValidator.validatePrice");
                    System.err.println("Error " + e.getMessage());
                }

            }

            int stock;
            while (true) {
                System.out.println("Enter stock:");
                stock = Integer.parseInt(scanner.nextLine());
                try {
                    BookValidator.validateStock(stock);
                    break;
                }catch (Exception e) {
                    FileLogger.logError("BookController - input error BookValidator.validateStock");
                    System.err.println("Error " + e.getMessage());
                }

            }

            Book updatedBook = new Book(isbn, bookTitle, bookDescription, bookPublisher, publicationDate, price, stock);
            updatedBook.setBookId(bookId);
            bookService.updateBook(updatedBook);
            FileLogger.logApp("BookController - Book updated: " + updatedBook);
            System.out.println("Book updated successfully!");
        } catch (Exception e) {
            FileLogger.logError("BookController - Error updating book: " + e.getMessage());
            throw new RuntimeException(e);
        }


    }

    private  void deleteBook() {
        FileLogger.logApp("BookController - Deleting a book");
        try {
            System.out.println("Enter Book ID to delete:");
            int bookId = Integer.parseInt(scanner.nextLine());
            bookService.deleteBook(bookId);
            FileLogger.logApp("BookController - Book deleted with ID: " + bookId);
            System.out.println("Book deleted successfully!");
        } catch (Exception e) {
            FileLogger.logError("BookController - Error deleting book: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private int getBookId() {
        int id = 0;
        try {
            List<Book> books = bookService.getAllBooks();
            for (int i = 0; i < books.size(); i++) {
               id = books.get(i).getBookId();
            }
            return id;
        }
        catch (Exception e) {
            FileLogger.logError("BookController - Error getting book ID: " + e.getMessage());
            return  -1;
        }
    }


}
