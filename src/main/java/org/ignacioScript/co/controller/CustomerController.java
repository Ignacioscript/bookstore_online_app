package org.ignacioScript.co.controller;

import org.ignacioScript.co.model.Customer;
import org.ignacioScript.co.seeder.CustomerSeeder;
import org.ignacioScript.co.service.CustomerService;
import org.ignacioScript.co.util.FileLogger;

import java.time.LocalDate;
import java.util.Scanner;

public class CustomerController {

    private  CustomerService customerService;
    private  static  Scanner scanner;

    public CustomerController(Scanner scanner, CustomerService customerService) {
        this.customerService = customerService;
        this.scanner = scanner;


    }



    public  void displayCustomerMenu() {
            //displayCustomerMenu(scanner);


        while (true) {
            diplayMenuOptions();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 0 -> CustomerSeeder.seedCustomers(customerService);
                case 1 -> createCustomer();
                case 2 -> viewCustomers();
                case 3 -> searchForCustomer();
                case 4 -> updateCustomer();
                case 5 -> deleteCustomer();
                case 6 -> {
                    System.out.println("Returning to Main Menu...\n");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.\n");
            }

        }


    }

    private void diplayMenuOptions() {
        System.out.println("\n===== Manage Customers =====");
        System.out.println("0. Seed Customers");
        System.out.println("1. Create Customer");
        System.out.println("2. View All Customers");
        System.out.println("3. Find Customer by Email");
        System.out.println("4. Update Customer");
        System.out.println("5. Delete Customer");
        System.out.println("6. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }




    private  void createCustomer() {
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
        } catch (Exception e) {
            FileLogger.logError("CustomerController - Error creating customer: " + e.getMessage());
            throw new RuntimeException(e);
        }


    }

    private   void viewCustomers() {
        try {
            System.out.println("All Customers:");
            for (Customer customer : customerService.getAllCustomers()) {
                System.out.println(customer);
            }
        } catch (Exception e) {
            FileLogger.logError("CustomerController - Error viewing customers: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private   void searchForCustomer() {
        System.out.println("Enter customer email to search:");
        String email = scanner.nextLine();
        try {
            Customer customer = customerService.findCustomerByEmail(email);
            if (customer != null) {
                System.out.println("Customer found: " + customer);
                FileLogger.logApp("CustomerController - found: " + customer);

                System.out.println("\n");
                System.out.println("Would you like to update this customer? or add new loyalty points type:  1. Update or  2. Add points, 3. No changes");
                int updateChoice = Integer.parseInt(scanner.nextLine());
                if (updateChoice == 1) {
                    updateCustomer();
                } else if (updateChoice == 2) {
                    System.out.println("Enter points to add:");
                    int pointsToAdd = Integer.parseInt(scanner.nextLine());
                    customer.setLoyaltyPoints(customer.getLoyaltyPoints() + pointsToAdd);
                    customerService.updateCustomer(customer);
                    FileLogger.logApp("CustomerController - updated loyalty points: " + customer);
                    System.out.println("Loyalty points updated successfully!");
                } else {
                    System.out.println("No changes made to the customer.");
                }

            } else {
                System.out.println("Customer not found.");
            }
        } catch (Exception e) {
            FileLogger.logError("CustomerController - Error searching for customer: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private  void updateCustomer() {
        try {
            System.out.println("Enter customer ID to update:");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter new First name:");
            String firstName = scanner.nextLine();
            System.out.println("Enter new Last name:");
            String lastName = scanner.nextLine();
            System.out.println("Enter new email:");
            String email = scanner.nextLine();
            System.out.println("Enter new phone:");
            String phone = scanner.nextLine();
            System.out.println("Enter new address:");
            String address = scanner.nextLine();
            LocalDate registrationDate = LocalDate.now(); // Default value for registration date
            System.out.println("Enter new points:");
            int loyaltyPoints = Integer.parseInt(scanner.nextLine());

            Customer updatedCustomer = new Customer(firstName, lastName, email, phone, address, registrationDate, loyaltyPoints);
            updatedCustomer.setCustomerId(id); // Set the ID of the customer to be updated
            customerService.updateCustomer(updatedCustomer);
            FileLogger.logApp("CustomerController - updated: " + updatedCustomer);
            System.out.println("Customer updated successfully!");
        } catch (Exception e) {
            FileLogger.logError("CustomerController - Error updating customer: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private   void deleteCustomer() {
        try {
            System.out.println("Enter customer ID to delete:");
            int id = Integer.parseInt(scanner.nextLine());
            customerService.deleteCustomer(id);
            FileLogger.logApp("CustomerController - deleted: " + id);
            System.out.println("Customer deleted successfully!");
        } catch (Exception e) {
            FileLogger.logError("CustomerController - Error deleting customer: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }


}
