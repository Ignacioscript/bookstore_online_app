package org.ignacioScript.co.controller;

import org.ignacioScript.co.model.Author;
import org.ignacioScript.co.seeder.AuthorSeeder;
import org.ignacioScript.co.service.AuthorService;
import org.ignacioScript.co.util.FileLogger;
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
                case 3 -> manageAuthor();
                case 4 -> {
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
        System.out.println("4. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }



    public Author createAuthor() {
        System.out.println("\n===== Create New Author =====");
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

            int authorId = getAuthorId() + 1;
            Author author = new Author(firstName, lastName, bio, nationality);
            author.setAuthorId(authorId);
            authorService.saveAuthor(author);
            System.out.println("Author created successfully! with ID: " + authorId);
            FileLogger.logApp("AuthorController - author created successfully! with ID: " + authorId);
            return  author;
        } catch (Exception e) {
            FileLogger.logError("AuthorController - Error creating author: " + e.getMessage());
            System.out.println("Unexpected Error: " + e.getMessage());
            throw  new RuntimeException("Error creating author: " + e.getMessage());
        }

    }

    private void viewAuthors() {
        System.out.println("\n===== View All Authors =====");
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

    private  void updateAuthor(int id) {
        try {


                try {
                    AuthorValidator.validateId(id);
                    authorService.findAuthorById(id);
                }catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }


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

            Author updatedAuthor = new Author(firstName, lastName, bio, nationality);
            updatedAuthor.setAuthorId(id);
            authorService.updateAuthor(updatedAuthor);
            System.out.println("Author updated successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private  void deleteAuthor(int id) {
        try {
            authorService.deleteAuthor(id);
            System.out.println("Author deleted successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }



    public  Author searchForAuthor() {
        System.out.println("\n===== Find Author =====");
        System.out.println("Enter author name or last name to search:");
        String keyword;
        int authorId;

        try {
            keyword = scanner.nextLine();
            List<Author> authors = authorService.searchAuthorsByKeyword(keyword.toLowerCase());
            if (authors != null) {
                System.out.println("\n===== Authors found =====");
                for (Author author : authors) {
                    System.out.println(author);
                }
                System.out.println("\nEnter Author ID to view details:");
                authorId = Integer.parseInt(scanner.nextLine());
                return authorService.findAuthorById(authorId);
            } else {
                System.out.println("No authors found.");
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    private int getAuthorId() {
        int id = 0;
        try {
            List<Author> authors = authorService.getAllAuthors();
            for (int i = 0; i < authors.size(); i++) {
                if (authors.get(i).getAuthorId() > id) {
                    id = authors.get(i).getAuthorId();
                }
            }
            return id;
        } catch (Exception e) {
            FileLogger.logError("Error getting author ID: " + e.getMessage());
            return -1; // Return an invalid ID in case of error
        }
    }


    public void manageAuthor() {
        try {
           Author author = searchForAuthor();
            int id = author.getAuthorId();
            System.out.println("Author selected: " + author.getFirstName() + " " + author.getLastName() + " (ID: " +  author.getAuthorId() + ")");
            System.out.println("What do you want to do with the author?");
            System.out.println("1. Update Author");
            System.out.println("2. Delete Author");
            System.out.println("3. Back to Author Menu");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character
            switch (choice) {
                case 1 -> updateAuthor(id);
                case 2 -> deleteAuthor(id);
                case 3 -> {
                    System.out.println("Returning to Author menu... \n");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.\n");
            }
        }catch (Exception e) {
            FileLogger.logError("Error managing author: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }

}
