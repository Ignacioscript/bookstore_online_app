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

        // Initialize controllers

        AuthorController authorController;
        BookController bookController;
        AuthorBookController authorBookController = new AuthorBookController(authorBookService, authorService, bookService);

        // main menu
        System.out.println("Welcome to the Library Management System");
        System.out.println("1. Manage Authors And Books");
        System.out.println("2. Manage Customers");
        System.out.println("3. Manage Orders");
        System.out.println("4. Exit");

        System.out.print("Enter your choice: ");
        int choice = Integer.parseInt(scanner.nextLine().trim());
        switch (choice) {
            case 1 -> {
                authorBookController.displayMenu(scanner);
            }
            case 2 -> { CustomerController.displayCustomerMenu(scanner, customerService);
            }

            case 3 -> {
                // OrderController orderController = new OrderController(scanner, orderService);
                // orderController.displayMenu();
                System.out.println("Order management is not implemented yet.");
            }

            case 4 -> {
                System.out.println("Exiting the program. Goodbye!");
                scanner.close();
                return;
            }


            // Main menu loop

        }
    }
}
