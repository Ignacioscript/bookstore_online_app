package org.ignacioScript.co.controller;

import org.ignacioScript.co.service.AuthorBookService;
import org.ignacioScript.co.service.AuthorService;
import org.ignacioScript.co.service.BookService;
import org.ignacioScript.co.service.CustomerService;
import org.ignacioScript.co.util.ConsoleColor;

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

        // Display styled menu
        ConsoleColor.println("===== Welcome to the Library Management System =====", ConsoleColor.CYAN);
        ConsoleColor.println("Created by: Ignacio Navarro", ConsoleColor.YELLOW);
        ConsoleColor.println("Email: ignacio@ignaciodev.me", ConsoleColor.YELLOW);
        ConsoleColor.println("Role: Analyst & Software Developer", ConsoleColor.YELLOW);
        ConsoleColor.println("Motto: \"Code with passion, deliver with purpose.\"", ConsoleColor.YELLOW);
        ConsoleColor.println("=====================================================\n", ConsoleColor.CYAN);

        ConsoleColor.println("===== Library Management System DashBoard =====", ConsoleColor.GREEN);
        ConsoleColor.println("Please select an option from the menu below:", ConsoleColor.WHITE);
        ConsoleColor.println("1. Manage Authors And Books", ConsoleColor.BLUE);
        ConsoleColor.println("2. Manage Customers", ConsoleColor.BLUE);
        ConsoleColor.println("3. Manage Orders", ConsoleColor.BLUE);
        ConsoleColor.println("4. Exit and close the Library System", ConsoleColor.BLUE);
        ConsoleColor.print("Enter your choice: ", ConsoleColor.PURPLE);

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        switch (choice) {
            case 1 -> authorBookController.displayMenu();
            case 2 -> customerController.displayCustomerMenu();
            case 3 -> itemOrderController.displayItemOrderMenu();
            case 4 -> {
                ConsoleColor.println("Exiting the program. Goodbye!", ConsoleColor.RED);
                scanner.close(); // Close the scanner when exiting
            }
            default -> ConsoleColor.println("Invalid option, try again", ConsoleColor.RED);
        }
    }
}