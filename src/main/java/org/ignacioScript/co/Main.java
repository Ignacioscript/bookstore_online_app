package org.ignacioScript.co;

import org.ignacioScript.co.controller.*;
import org.ignacioScript.co.model.Customer;
import org.ignacioScript.co.model.ItemOrder;
import org.ignacioScript.co.service.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialize Services
        AuthorService authorService = new AuthorService();
        CustomerService customerService = new CustomerService();
        BookService bookService = new BookService();
        OrderService orderService = new OrderService();
        ItemOrderService itemOrderService = new ItemOrderService();

        AuthorController authorController;
        CustomerController customerController;
        BookController bookController;
        OrderController orderController;
        ItemOrderController itemOrderController;

        Scanner scanner = new Scanner(System.in); // Scanner for user input

//TODO finish controller and revie the update ID issue, search for dockerize this app

        while (true) {
            displayMainMenu();
            int choice = getChoice(scanner);

            switch (choice) {
                case 1 -> new AuthorController(scanner, authorService);
//                case 2 -> new CustomerController(scanner, customerService);
//                case 3 -> new BookController(scanner, bookService);
//                case 4 -> new OrderController(scanner, orderService);
//                case 5 -> new ItemOrderController(scanner, itemOrderService);
                case 6 -> {
                    System.out.println("Exiting the application. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n===== Main Menu =====");
        System.out.println("1. Manage Authors");
        System.out.println("2. Manage Customers");
        System.out.println("3. Manage Books");
        System.out.println("4. Manage Orders");
        System.out.println("5. Manage ItemOrders");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getChoice(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1; // Invalid choice
        }
    }
}