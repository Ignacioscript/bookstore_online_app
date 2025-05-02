package org.ignacioScript.co.controller;

import org.ignacioScript.co.model.Author;
import org.ignacioScript.co.seeder.AuthorSeeder;
import org.ignacioScript.co.service.AuthorService;

import java.util.List;
import java.util.Scanner;

public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(Scanner scanner, AuthorService authorService) {
        this.authorService = authorService;
        AuthorSeeder.seedAuthors(authorService);

        while (true) {
            displayAuthorMenu();
            int choice = getChoice(scanner);

            switch (choice) {
                case 1 -> createAuthor(scanner, authorService);
                case 2 -> viewAuthors(authorService);
                case 3 -> findAuthorById(scanner, authorService);
                case 4 -> updateAuthor(scanner, authorService);
                case 5 -> deleteAuthor(scanner, authorService);
                case 6 -> {
                    System.out.println("Returning to Main Menu...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }

        }
    }


    private static  void displayAuthorMenu() {
        System.out.println("\n===== Manage Authors =====");
        System.out.println("1. Create Author");
        System.out.println("2. View All Authors");
        System.out.println("3. Find Author by ID");
        System.out.println("4. Update Author");
        System.out.println("5. Delete Author");
        System.out.println("6. Back to Main Menu");
        System.out.print("Enter your choice: ");

    }

    private static int getChoice(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1; // Invalid choice
        }

    }

    private static void createAuthor(Scanner scanner, AuthorService authorService) {
        try {
            System.out.print("Enter First Name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter Last Name: ");
            String lastName = scanner.nextLine();
            System.out.print("Enter Bio: ");
            String bio = scanner.nextLine();
            System.out.print("Enter Nationality: ");
            String nationality = scanner.nextLine();

            Author author = new Author(firstName, lastName, bio, nationality);
            authorService.saveAuthor(author);
            System.out.println("Author created successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewAuthors(AuthorService authorService) {
        try {
            List<Author> authors = authorService.getAllAuthors();
           // authors.forEach(System.out::println);
            for (Author author : authors) {
                System.out.println(author.toString());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void findAuthorById(Scanner scanner, AuthorService authorService) {
        try {
            System.out.print("Enter Author ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            Author author = authorService.findAuthorById(id);
            System.out.println(author);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void updateAuthor(Scanner scanner, AuthorService authorService) {
        try {
            System.out.print("Enter Author ID to Update: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter New First Name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter New Last Name: ");
            String lastName = scanner.nextLine();
            System.out.print("Enter New Bio: ");
            String bio = scanner.nextLine();
            System.out.print("Enter New Nationality: ");
            String nationality = scanner.nextLine();

            Author updatedAuthor = new Author(firstName, lastName, bio, nationality);
            updatedAuthor.setAuthorId(id);
            authorService.updateAuthor(updatedAuthor);
            System.out.println("Author updated successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void deleteAuthor(Scanner scanner, AuthorService authorService) {
        try {
            System.out.print("Enter Author ID to Delete: ");
            int id = Integer.parseInt(scanner.nextLine());
            authorService.deleteAuthor(id);
            System.out.println("Author deleted successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void sortAuthors(Scanner scanner) {
        try {
            System.out.println("\nHow would you like to sort authors?");
            System.out.println("1. Sort by  First Name");
            System.out.println("2. Sort by  Last Name");
            System.out.print("Your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            List<Author> authors = authorService.getAllAuthors();

            if (choice == 1) {
                authors = authorService.sortAuthorsByLastName(authors);
            } else if (choice == 2) {
                authors = authorService.sortAuthorsByFirstName(authors);
            } else {
                System.out.println("Invalid choice!");
                return;
            }

            System.out.println("\nSorted Authors:");
            for (Author author : authors) {
                System.out.println(author.getAuthorId() + ": " + author.getFirstName() + " " + author.getLastName());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }



}
