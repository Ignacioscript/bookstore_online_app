package org.ignacioScript.co.controller;

import org.ignacioScript.co.model.Author;
import org.ignacioScript.co.seeder.AuthorSeeder;
import org.ignacioScript.co.service.AuthorService;
import org.ignacioScript.co.validation.AuthorValidator;

import java.util.List;

import java.util.Scanner;

public class AuthorController {

    private AuthorService authorService;
    private  static  Scanner scanner;

    public AuthorController(Scanner scanner, AuthorService authorService) {
        this.authorService = authorService;
        this.scanner = scanner;



    }


    public  void displayAuthorMenu() {
        while (true) {
            displayMenuOption();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 0 -> AuthorSeeder.seedAuthors(authorService);
                case 1 -> createAuthor();
                case 2 -> viewAuthors();
                case 3 -> searchForAuthor();
                case 4 -> updateAuthor();
                case 5 -> deleteAuthor();
                case 6 -> {
                    System.out.println("Returning to Author and Book menu... \n");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.\n");
            }

        }

    }


    private void displayMenuOption() {
        System.out.println("\n===== Manage Authors =====");
        System.out.println("0. Seed Authors");
        System.out.println("1. Create Author");
        System.out.println("2. View All Authors");
        System.out.println("3. Find Author by Name or Last Name");
        System.out.println("4. Update Author");
        System.out.println("5. Delete Author");
        System.out.println("6. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }



    private void createAuthor() {
        try {
            String firstName;
            while (true) {
                System.out.print("Enter First Name: ");
                firstName = scanner.nextLine();
                try {
                    AuthorValidator.validateProperNoun(firstName);
                    break; // Exit loop if validation passes
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            String lastName;
            while (true) {
                System.out.print("Enter Last Name: ");
                lastName = scanner.nextLine();
                try {
                    AuthorValidator.validateProperNoun(lastName);
                    break; // Exit loop if validation passes
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            String bio;
            while (true) {
                System.out.print("Enter Bio: ");
                bio = scanner.nextLine();
                try {
                    AuthorValidator.validateDescription(bio, 300, 20);
                    break; // Exit loop if validation passes
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            String nationality;
            while (true) {
                System.out.print("Enter Nationality: ");
                nationality = scanner.nextLine();
                try {
                    AuthorValidator.validateProperNoun(nationality);
                    break; // Exit loop if validation passes
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            Author author = new Author(firstName, lastName, bio, nationality);
            authorService.saveAuthor(author);
            System.out.println("Author created successfully!");
        } catch (Exception e) {
            System.out.println("Unexpected Error: " + e.getMessage());
        }
    }

    private void viewAuthors() {
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

    private void findAuthorById() {
        try {
            System.out.print("Enter Author ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            Author author = authorService.findAuthorById(id);
            System.out.println(author);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private  void updateAuthor() {
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

    private  void deleteAuthor() {
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

    private void searchForAuthor() {
        System.out.println("Enter author name or last name to search:");
        String keyword = scanner.nextLine();
        List<Author> authors = authorService.searchAuthorsByKeyword(keyword);
        if (authors.isEmpty()) {
            System.out.println("No authors found.");
        } else {
            System.out.println("Authors found:");
            for (Author author : authors) {
                System.out.println(author);
            }
        }
    }



}
