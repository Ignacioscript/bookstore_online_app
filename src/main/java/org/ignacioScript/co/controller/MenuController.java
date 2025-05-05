package org.ignacioScript.co.controller;

import org.ignacioScript.co.service.AuthorBookService;
import org.ignacioScript.co.service.AuthorService;
import org.ignacioScript.co.service.BookService;
import org.ignacioScript.co.service.CustomerService;

import java.util.Scanner;

public class MenuController {

    public static void runMenu() {
        Scanner scanner = new Scanner(System.in);


        // Initialize services
        CustomerService customerService = new CustomerService();
        AuthorService authorService = new AuthorService();
        BookService bookService = new BookService();
        AuthorBookService authorBookService = new AuthorBookService();
        ItemOrderController itemOrderController = new ItemOrderController(scanner, customerService, bookService);

        // Initialize controllers


        CustomerController customerController = new CustomerController(scanner, customerService);
        AuthorBookController authorBookController = new AuthorBookController(scanner, authorBookService, authorService, bookService);


            // Main menu
            System.out.println("Welcome to the Library Management System");
            System.out.println("1. Manage Authors And Books");
            System.out.println("2. Manage Customers");
            System.out.println("3. Manage Orders");
            System.out.println("4. Exit and close the Library System");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1 -> authorBookController.displayMenu();
                case 2 -> customerController.displayCustomerMenu();
                case 3 -> itemOrderController.displayItemOrderMenu();
                case 4 -> {
                    System.out.println("Exiting the program. Goodbye!");
                    scanner.close(); // Close the scanner when exiting
                }
                default -> System.out.println("Invalid option, try again");
            }

    }
}
