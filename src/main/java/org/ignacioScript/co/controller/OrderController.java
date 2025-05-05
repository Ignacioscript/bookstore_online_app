package org.ignacioScript.co.controller;

import org.ignacioScript.co.model.Customer;
import org.ignacioScript.co.model.Order;
import org.ignacioScript.co.model.Status;
import org.ignacioScript.co.service.BookService;
import org.ignacioScript.co.service.CustomerService;
import org.ignacioScript.co.service.ItemOrderService;
import org.ignacioScript.co.service.OrderService;
import org.ignacioScript.co.util.FileLogger;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final BookService bookService;


    public OrderController(OrderService orderService, CustomerService customerService, BookService bookService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.bookService = bookService;
    }

    public OrderController() {
        this.orderService = new OrderService();
        this.customerService = new CustomerService();
        this.bookService = new BookService();
    }




    public Order createOrder(Scanner scanner, CustomerService customerService, BookService bookService) {
        Order order;
        try {
            Customer customer;
            FileLogger.logApp("OrderController - createOrder");
            System.out.println("Creating Order...");
            System.out.print("Create a new Customer or use an existing one?  ");
            System.out.println(" Type: (1.new - 2. existing - 3. cancel):");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                System.out.println("Creating a new Customer...");
                customer =  createCustomer(scanner);
                FileLogger.logApp("OrderController - Customer created: " + customer);


            } else if (choice == 2) {
                System.out.print("Enter Customer mail: ");
                String email = scanner.nextLine();
                 customer = getCustomerByEmail(email);
                 FileLogger.logApp("OrderController - Customer found: " + customer);
                if (customer == null) {
                    System.out.println("Customer not found. Please create a new customer.");
                    customer = createCustomer(scanner);
                }

            } else {
                System.out.println("Cancelling order creation.");
                return null;
            }


            System.out.println(" \n Validating Order please wait...");
            order = createOrder(scanner, customer);
            FileLogger.logApp("OrderController - Order created: " + customer);;


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
                System.out.println("Customer not found.");
                return null;
            }
            System.out.println("Customer found: " + customer.getFirstName() + " " + customer.getLastName());
            return customer;
        } catch (Exception e) {
            FileLogger.logError("OrderController - Error finding customer: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Customer createCustomer(Scanner scanner) {
        try {
            System.out.println("Enter customer First name:");
            String firstName = scanner.nextLine();
            System.out.println("Enter customer Last name:");
            String lastName = scanner.nextLine();
            System.out.println("Enter customer email:");
            String email = scanner.nextLine();
            System.out.println("Enter customer phone:");
            String phone = scanner.nextLine();
            System.out.println("Enter customer address:");
            String address = scanner.nextLine();
            LocalDate registrationDate = LocalDate.now(); // Default value for registration date
            System.out.println("Enter initial points:");
            int loyaltyPoints =  Integer.parseInt(scanner.nextLine());

            Customer customer = new Customer(firstName, lastName, email, phone, address, registrationDate, loyaltyPoints);
            customerService.saveCustomer(customer);
            FileLogger.logApp("CustomerController - created: " + customer);
            System.out.println("Customer created successfully!");
            return customer;
        } catch (Exception e) {
            FileLogger.logError("CustomerController - Error creating customer: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private List<Customer> getAllCustomers() {
        try {
            System.out.println("All Customers:");
            List<Customer> customers = customerService.getAllCustomers();
            for (Customer customer : customers) {
                System.out.println(customer);
            }
            return customers;
        } catch (Exception e) {
            FileLogger.logError("OrderController - Error viewing customers: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Order createOrder(Scanner scanner, Customer customer) {
        FileLogger.logApp("OrderController - createOrder");
        Status status;
        LocalDate orderDate = LocalDate.now(); // Default value for order date
//        System.out.println("Enter Order Total:");
        double orderTotal = 10;// Consume newline
        System.out.println("Choose Payment Method: (1. Credit Card, 2. Debit Card, 3. Cash)");
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
            System.out.println("Invalid choice. Defaulting to Cash.");
            paymentMethod = "Cash";
        }


        System.out.println("Enter Order Status: (1. Pending, 2. Processing, 3. Shipped)");
        int statusChoice = scanner.nextInt();
        scanner.nextLine();
        if (statusChoice == 1) {
            status = Status.PENDING;
        } else if (statusChoice == 2) {
            status = Status.PROCESSING;
        } else if (statusChoice == 3) {
            status = Status.SHIPPED;
        } else {
            System.out.println("Invalid choice. Defaulting to PENDING.");
            status = Status.PENDING;
        }

        Order order = new Order(customer, orderDate, orderTotal, paymentMethod, status);
        orderService.saveOrder(order);
        FileLogger.logApp("OrderController - created: " + order);

        return order;
    }

}
