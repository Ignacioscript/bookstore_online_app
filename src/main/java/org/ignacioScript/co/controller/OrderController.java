package org.ignacioScript.co.controller;

import org.ignacioScript.co.model.Customer;
import org.ignacioScript.co.model.Order;
import org.ignacioScript.co.model.Status;
import org.ignacioScript.co.service.BookService;
import org.ignacioScript.co.service.CustomerService;
import org.ignacioScript.co.service.ItemOrderService;
import org.ignacioScript.co.service.OrderService;
import org.ignacioScript.co.util.ConsoleColor;
import org.ignacioScript.co.util.FileLogger;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final BookService bookService;
    private final CustomerController customerController;
    private final Scanner scanner;

    public OrderController(OrderService orderService, CustomerService customerService, BookService bookService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.bookService = bookService;
        this.customerController = new CustomerController(customerService);
        this.scanner = new Scanner(System.in);
    }

    public OrderController() {
        this.orderService = new OrderService();
        this.customerService = new CustomerService();
        this.bookService = new BookService();
        this.customerController = new CustomerController(customerService);
        this.scanner = new Scanner(System.in);
    }

    public Order createNewOrder() {
        Order order;
        try {
            Customer customer;
            FileLogger.logApp("OrderController - createOrder");
            ConsoleColor.println("Creating Order...", ConsoleColor.CYAN);
            ConsoleColor.print("Create a new Customer or use an existing one? ", ConsoleColor.PURPLE);
            ConsoleColor.println("Type: (1.new - 2. existing - 3. cancel):", ConsoleColor.BLUE);
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                ConsoleColor.println("Creating a new Customer...", ConsoleColor.CYAN);
                customer = customerController.createCustomer();
                FileLogger.logApp("OrderController - Customer created: " + customer);
            } else if (choice == 2) {
                ConsoleColor.print("Enter Customer mail: ", ConsoleColor.PURPLE);
                String email = scanner.nextLine();
                customer = getCustomerByEmail(email);
                FileLogger.logApp("OrderController - Customer found: " + customer);
                if (customer == null) {
                    ConsoleColor.println("Customer not found. Please create a new customer.", ConsoleColor.RED);
                    customer = createCustomer();
                }
            } else {
                ConsoleColor.println("Cancelling order creation.", ConsoleColor.YELLOW);
                return null;
            }

            ConsoleColor.println("\nValidating Order please wait...", ConsoleColor.CYAN);
            order = saveOrder(customer);
            FileLogger.logApp("OrderController - Order created: " + customer);
        } catch (Exception e) {
            FileLogger.logError("OrderController - Error creating order: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return order;
    }

    private Customer getCustomerByEmail(String email) {
        try {
            Customer customer = customerService.findCustomerByEmail(email);
            if (customer == null) {
                ConsoleColor.println("Customer not found.", ConsoleColor.RED);
                ConsoleColor.println("Would you like to create a new customer or retry? (1: Create new, 2: Retry)", ConsoleColor.BLUE);
                Scanner scanner = new Scanner(System.in);
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline leftover
                if (choice == 1) {
                    customer = createCustomer();
                    return customer; // Create and return a new customer
                } else if (choice == 2) {
                    ConsoleColor.print("Enter Customer email: ", ConsoleColor.PURPLE);
                    String newEmail = scanner.nextLine();
                    return getCustomerByEmail(newEmail); // Retry with a new email
                } else {
                    ConsoleColor.println("Invalid choice. Returning null.", ConsoleColor.RED);
                    return null; // Cancel operation if the input is invalid
                }
            }
            ConsoleColor.println("Customer found: " + customer.getFirstName() + " " + customer.getLastName(), ConsoleColor.GREEN);
            return customer;
        } catch (Exception e) {
            FileLogger.logError("OrderController - Error finding customer: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Customer createCustomer() {
        try {
            Customer customer = customerController.createCustomer();
            return customer;
        } catch (Exception e) {
            FileLogger.logError("CustomerController - Error creating customer: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private List<Customer> getAllCustomers() {
        try {
            ConsoleColor.println("All Customers:", ConsoleColor.CYAN);
            List<Customer> customers = customerService.getAllCustomers();
            for (Customer customer : customers) {
                ConsoleColor.println(customer.toString(), ConsoleColor.WHITE);
            }
            return customers;
        } catch (Exception e) {
            FileLogger.logError("OrderController - Error viewing customers: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Order saveOrder( Customer customer) {
        FileLogger.logApp("OrderController - createOrder");
        Status status;
        LocalDate orderDate = LocalDate.now(); // Default value for order date
        double orderTotal = 10; // Default order total
        ConsoleColor.println("Choose Payment Method: (1. Credit Card, 2. Debit Card, 3. Cash)", ConsoleColor.BLUE);
        String paymentMethod;
        int paymentChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        if (paymentChoice == 1) {
            paymentMethod = "Credit Card";
        } else if (paymentChoice == 2) {
            paymentMethod = "Debit Card";
        } else if (paymentChoice == 3) {
            paymentMethod = "Cash";
        } else {
            ConsoleColor.println("Invalid choice. Defaulting to Cash.", ConsoleColor.YELLOW);
            paymentMethod = "Cash";
        }

        ConsoleColor.println("Enter Order Status: (1. Pending, 2. Processing, 3. Shipped)", ConsoleColor.BLUE);
        int statusChoice = scanner.nextInt();
        scanner.nextLine();
        if (statusChoice == 1) {
            status = Status.PENDING;
        } else if (statusChoice == 2) {
            status = Status.PROCESSING;
        } else if (statusChoice == 3) {
            status = Status.SHIPPED;
        } else {
            ConsoleColor.println("Invalid choice. Defaulting to PENDING.", ConsoleColor.YELLOW);
            status = Status.PENDING;
        }

        Order order = new Order(customer, orderDate, orderTotal, paymentMethod, status);
        orderService.saveOrder(order);
        FileLogger.logApp("OrderController - created: " + order);

        return order;
    }

    public void updateOrder(int orderId) {
        try {
            Status newStatus;
            ConsoleColor.println("Updating Order...", ConsoleColor.CYAN);
            Order order = orderService.findOrderById(orderId);
            if (order == null) {
                ConsoleColor.println("Order not found.", ConsoleColor.RED);
                return;
            }

            ConsoleColor.println("Choose Payment Method: (1. Credit Card, 2. Debit Card, 3. Cash)", ConsoleColor.BLUE);
            String newPaymentMethod;
            int paymentChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (paymentChoice == 1) {
                newPaymentMethod = "Credit Card";
            } else if (paymentChoice == 2) {
                newPaymentMethod = "Debit Card";
            } else if (paymentChoice == 3) {
                newPaymentMethod = "Cash";
            } else {
                ConsoleColor.println("Invalid choice. Defaulting to Cash.", ConsoleColor.YELLOW);
                newPaymentMethod = "Cash";
            }
            order.setPaymentMethod(newPaymentMethod);

            ConsoleColor.println("Enter Order Status: (1. Pending, 2. Processing, 3. Shipped)", ConsoleColor.BLUE);
            int statusChoice = scanner.nextInt();
            scanner.nextLine();
            if (statusChoice == 1) {
                newStatus = Status.PENDING;
            } else if (statusChoice == 2) {
                newStatus = Status.PROCESSING;
            } else if (statusChoice == 3) {
                newStatus = Status.SHIPPED;
            } else {
                ConsoleColor.println("Invalid choice. Defaulting to PENDING.", ConsoleColor.YELLOW);
                newStatus = Status.PENDING;
            }
            order.setStatus(newStatus);

            orderService.updateOrder(order);
            FileLogger.logApp("OrderController - Order updated: " + order);


        } catch (Exception e) {
            FileLogger.logError("OrderController - Error updating order: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}