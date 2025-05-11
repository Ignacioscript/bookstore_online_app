package org.ignacioScript.co.controller;

import org.ignacioScript.co.model.Author;
import org.ignacioScript.co.seeder.AuthorSeeder;
import org.ignacioScript.co.service.AuthorService;
import org.ignacioScript.co.util.ConsoleColor;
import org.ignacioScript.co.util.FileLogger;
import org.ignacioScript.co.validation.AuthorValidator;

import java.util.List;
import java.util.Scanner;

public class AuthorController {

    private AuthorService authorService;
    private static Scanner scanner;

    public AuthorController(Scanner scanner, AuthorService authorService) {
        this.authorService = authorService;
        AuthorController.scanner = scanner;
    }

    public void displayAuthorMenu() {
        while (true) {
            displayMenuOption();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 0 -> AuthorSeeder.seedAuthors(authorService);
                case 1 -> createAuthor();
                case 2 -> viewAuthors();
                case 3 -> manageAuthor();
                case 4 -> {
                    ConsoleColor.println("Returning to Author and Book menu...\n", ConsoleColor.GREEN);
                    return;
                }
                default -> ConsoleColor.println("Invalid choice. Please try again.\n", ConsoleColor.RED);
            }
        }
    }

    private void displayMenuOption() {
        ConsoleColor.println("\n===== Manage Authors =====", ConsoleColor.CYAN);
        ConsoleColor.println("0. Seed Authors", ConsoleColor.BLUE);
        ConsoleColor.println("1. Create Author", ConsoleColor.BLUE);
        ConsoleColor.println("2. View All Authors", ConsoleColor.BLUE);
        ConsoleColor.println("3. Find Author by Name or Last Name", ConsoleColor.BLUE);
        ConsoleColor.println("4. Back to Main Menu", ConsoleColor.BLUE);
        ConsoleColor.print("Enter your choice: ", ConsoleColor.PURPLE);
    }

    public Author createAuthor() {
        ConsoleColor.println("\n===== Create New Author =====", ConsoleColor.CYAN);
        try {
            String firstName;
            while (true) {
                ConsoleColor.print("Enter First Name: ", ConsoleColor.PURPLE);
                firstName = scanner.nextLine();
                try {
                    AuthorValidator.validateProperNoun(firstName);
                    break;
                } catch (Exception e) {
                    ConsoleColor.println("Error: " + e.getMessage(), ConsoleColor.RED);
                }
            }

            String lastName;
            while (true) {
                ConsoleColor.print("Enter Last Name: ", ConsoleColor.PURPLE);
                lastName = scanner.nextLine();
                try {
                    AuthorValidator.validateProperNoun(lastName);
                    break;
                } catch (Exception e) {
                    ConsoleColor.println("Error: " + e.getMessage(), ConsoleColor.RED);
                }
            }

            String bio;
            while (true) {
                ConsoleColor.print("Enter Bio: ", ConsoleColor.PURPLE);
                bio = scanner.nextLine();
                try {
                    AuthorValidator.validateDescription(bio, 300, 20);
                    break;
                } catch (Exception e) {
                    ConsoleColor.println("Error: " + e.getMessage(), ConsoleColor.RED);
                }
            }

            String nationality;
            while (true) {
                ConsoleColor.print("Enter Nationality: ", ConsoleColor.PURPLE);
                nationality = scanner.nextLine();
                try {
                    AuthorValidator.validateProperNoun(nationality);
                    break;
                } catch (Exception e) {
                    ConsoleColor.println("Error: " + e.getMessage(), ConsoleColor.RED);
                }
            }

            int authorId = getAuthorId() + 1;
            Author author = new Author(firstName, lastName, bio, nationality);
            author.setAuthorId(authorId);
            authorService.saveAuthor(author);
            ConsoleColor.println("Author created successfully! with ID: " + authorId, ConsoleColor.GREEN);
            FileLogger.logApp("AuthorController - author created successfully! with ID: " + authorId);
            return author;
        } catch (Exception e) {
            FileLogger.logError("AuthorController - Error creating author: " + e.getMessage());
            ConsoleColor.println("Unexpected Error: " + e.getMessage(), ConsoleColor.RED);
            throw new RuntimeException("Error creating author: " + e.getMessage());
        }
    }

    private void viewAuthors() {
        ConsoleColor.println("\n===== View All Authors =====", ConsoleColor.CYAN);
        try {
            List<Author> authors = authorService.getAllAuthors();
            for (Author author : authors) {
                ConsoleColor.println(author.toString(), ConsoleColor.WHITE);
            }
        } catch (Exception e) {
            ConsoleColor.println("Error: " + e.getMessage(), ConsoleColor.RED);
        }
    }

    private void findAuthorById() {
        try {
            ConsoleColor.print("Enter Author ID: ", ConsoleColor.PURPLE);
            int id = Integer.parseInt(scanner.nextLine());
            Author author = authorService.findAuthorById(id);
            ConsoleColor.println(author.toString(), ConsoleColor.WHITE);
        } catch (Exception e) {
            ConsoleColor.println("Error: " + e.getMessage(), ConsoleColor.RED);
        }
    }

    private void updateAuthor(int id) {
        try {
            AuthorValidator.validateId(id);
            authorService.findAuthorById(id);

            Author author = authorService.findAuthorById(id);
            authorDetails(author);

            String firstName;
            while (true) {
                ConsoleColor.print("Enter First Name: ", ConsoleColor.PURPLE);
                firstName = scanner.nextLine();
                try {
                    AuthorValidator.validateProperNoun(firstName);
                    break;
                } catch (Exception e) {
                    ConsoleColor.println("Error: " + e.getMessage(), ConsoleColor.RED);
                }
            }

            String lastName;
            while (true) {
                ConsoleColor.print("Enter Last Name: ", ConsoleColor.PURPLE);
                lastName = scanner.nextLine();
                try {
                    AuthorValidator.validateProperNoun(lastName);
                    break;
                } catch (Exception e) {
                    ConsoleColor.println("Error: " + e.getMessage(), ConsoleColor.RED);
                }
            }

            String bio;
            while (true) {
                ConsoleColor.print("Enter Bio: ", ConsoleColor.PURPLE);
                bio = scanner.nextLine();
                try {
                    AuthorValidator.validateDescription(bio, 300, 20);
                    break;
                } catch (Exception e) {
                    ConsoleColor.println("Error: " + e.getMessage(), ConsoleColor.RED);
                }
            }

            String nationality;
            while (true) {
                ConsoleColor.print("Enter Nationality: ", ConsoleColor.PURPLE);
                nationality = scanner.nextLine();
                try {
                    AuthorValidator.validateProperNoun(nationality);
                    break;
                } catch (Exception e) {
                    ConsoleColor.println("Error: " + e.getMessage(), ConsoleColor.RED);
                }
            }

            Author updatedAuthor = new Author(firstName, lastName, bio, nationality);
            updatedAuthor.setAuthorId(id);
            authorService.updateAuthor(updatedAuthor);
            ConsoleColor.println("Author updated successfully!", ConsoleColor.GREEN);
            authorDetails(updatedAuthor);
        } catch (Exception e) {
            ConsoleColor.println("Error: " + e.getMessage(), ConsoleColor.RED);
        }
    }

    private void deleteAuthor(int id) {
        try {
            authorService.deleteAuthor(id);
            ConsoleColor.println("Author deleted successfully!", ConsoleColor.GREEN);
        } catch (Exception e) {
            ConsoleColor.println("Error: " + e.getMessage(), ConsoleColor.RED);
        }
    }

    public Author searchForAuthor() {
        ConsoleColor.println("\n===== Find Author =====", ConsoleColor.CYAN);
        ConsoleColor.print("Enter author name or last name to search: ", ConsoleColor.PURPLE);
        String keyword = scanner.nextLine();

        try {
            List<Author> authors = authorService.searchAuthorsByKeyword(keyword.toLowerCase());
            if (authors.isEmpty()) {
                ConsoleColor.println("No authors found.", ConsoleColor.RED);
                return null;
            } else {
                ConsoleColor.println("\n===== Authors found =====", ConsoleColor.CYAN);
                for (Author author : authors) {
                    ConsoleColor.println(author.toString(), ConsoleColor.WHITE);
                }
                ConsoleColor.print("\nEnter Author ID to view details: ", ConsoleColor.PURPLE);
                int authorId = Integer.parseInt(scanner.nextLine());
                return authorService.findAuthorById(authorId);
            }
        } catch (Exception e) {
            ConsoleColor.println("Error: " + e.getMessage(), ConsoleColor.RED);
            throw new RuntimeException(e);
        }
    }

    private int getAuthorId() {
        int id = 0;
        try {
            List<Author> authors = authorService.getAllAuthors();
            for (Author author : authors) {
                id = Math.max(id, author.getAuthorId());
            }
            return id;
        } catch (Exception e) {
            FileLogger.logError("Error getting author ID: " + e.getMessage());
            ConsoleColor.println("Error retrieving author ID.", ConsoleColor.RED);
            return -1;
        }
    }

    public void manageAuthor() {
        try {
            Author author = searchForAuthor();
            if (author == null) return;

            int id = author.getAuthorId();
            ConsoleColor.println("Author selected: " + author.getFirstName() + " " + author.getLastName() + " (ID: " + id + ")", ConsoleColor.WHITE);
            ConsoleColor.println("\nWhat do you want to do with this author?", ConsoleColor.CYAN);
            ConsoleColor.println("1. Update Author", ConsoleColor.BLUE);
            ConsoleColor.println("2. Delete Author", ConsoleColor.BLUE);
            ConsoleColor.println("3. Back to Author Menu", ConsoleColor.BLUE);
            ConsoleColor.print("Enter your choice: ", ConsoleColor.PURPLE);

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character
            switch (choice) {
                case 1 -> updateAuthor(id);
                case 2 -> deleteAuthor(id);
                case 3 -> ConsoleColor.println("Returning to Author menu...\n", ConsoleColor.GREEN);
                default -> ConsoleColor.println("Invalid choice. Please try again.\n", ConsoleColor.RED);
            }
        } catch (Exception e) {
            FileLogger.logError("Error managing author: " + e.getMessage());
            ConsoleColor.println("Error: " + e.getMessage(), ConsoleColor.RED);
        }
    }

    private void authorDetails(Author author) {
        ConsoleColor.println("\n===== Author Details =====", ConsoleColor.CYAN);
        ConsoleColor.println("ID: " + author.getAuthorId(), ConsoleColor.WHITE);
        ConsoleColor.println("First Name: " + author.getFirstName(), ConsoleColor.WHITE);
        ConsoleColor.println("Last Name: " + author.getLastName(), ConsoleColor.WHITE);
        ConsoleColor.println("Bio: " + author.getBio(), ConsoleColor.WHITE);
    }



}