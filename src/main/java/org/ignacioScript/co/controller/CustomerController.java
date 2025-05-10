package org.ignacioScript.co.controller;

import org.ignacioScript.co.model.Customer;
import org.ignacioScript.co.seeder.CustomerSeeder;
import org.ignacioScript.co.service.CustomerService;
import org.ignacioScript.co.util.ConsoleColor;
import org.ignacioScript.co.util.FileLogger;
import org.ignacioScript.co.validation.CustomerValidator;

import java.time.LocalDate;
import java.util.Scanner;

public class CustomerController {

    private CustomerService customerService;
    private static Scanner scanner;

    public CustomerController(Scanner scanner, CustomerService customerService) {
        this.customerService = customerService;
        this.scanner = scanner;
    }

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
        this.scanner = new Scanner(System.in);
    }

    public void displayCustomerMenu() {
        while (true) {
            diplayMenuOptions();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 0 -> CustomerSeeder.seedCustomers(customerService);
                case 1 -> createCustomer();
                case 2 -> viewCustomers();
                case 3 -> searchForCustomer();
                case 4 -> deleteCustomer();
                case 5 -> {
                    ConsoleColor.println("Returning to Main Menu...\n", ConsoleColor.GREEN);
                    MenuController.runMenu();
                    return;
                }
                default -> ConsoleColor.println("Invalid choice. Please try again.\n", ConsoleColor.RED);
            }
        }
    }

    private void diplayMenuOptions() {
        ConsoleColor.println("\n===== Manage Customers =====", ConsoleColor.CYAN);
        ConsoleColor.println("0. Seed Customers", ConsoleColor.BLUE);
        ConsoleColor.println("1. Create Customer", ConsoleColor.BLUE);
        ConsoleColor.println("2. View All Customers", ConsoleColor.BLUE);
        ConsoleColor.println("3. Find Customer by Email", ConsoleColor.BLUE);
        ConsoleColor.println("4. Delete Customer", ConsoleColor.BLUE);
        ConsoleColor.println("5. Back to Main Menu", ConsoleColor.BLUE);
        ConsoleColor.print("Enter your choice: ", ConsoleColor.PURPLE);
    }

    public Customer createCustomer() {
        try {
            ConsoleColor.println("===== Create Customer =====", ConsoleColor.CYAN);

            String firstName;
            while (true) {
                ConsoleColor.print("Enter customer First name: ", ConsoleColor.PURPLE);
                try {
                    firstName = scanner.nextLine();
                    CustomerValidator.validateProperNoun(firstName);
                    break;
                } catch (Exception e) {
                    ConsoleColor.println("Error: " + e.getMessage() + " Please try again.", ConsoleColor.RED);
                }
            }

            String lastName;
            while (true) {
                ConsoleColor.print("Enter customer Last name: ", ConsoleColor.PURPLE);
                try {
                    lastName = scanner.nextLine();
                    CustomerValidator.validateProperNoun(lastName);
                    break;
                } catch (Exception e) {
                    ConsoleColor.println("Error: " + e.getMessage() + " Please try again.", ConsoleColor.RED);
                }
            }

            String email;
            while (true) {
                ConsoleColor.print("Enter customer email: ", ConsoleColor.PURPLE);
                try {
                    email = scanner.nextLine();
                    CustomerValidator.validateMail(email);
                    break;
                } catch (Exception e) {
                    ConsoleColor.println("Error: " + e.getMessage() + " Please try again.", ConsoleColor.RED);
                }
            }

            String phone;
            while (true) {
                ConsoleColor.print("Enter customer phone: ", ConsoleColor.PURPLE);
                try {
                    phone = scanner.nextLine();
                    CustomerValidator.validateOnlyNumbers(phone, 20, 6);
                    break;
                } catch (Exception e) {
                    ConsoleColor.println("Error: " + e.getMessage() + " Please try again.", ConsoleColor.RED);
                }
            }

            String address;
            while (true) {
                ConsoleColor.print("Enter customer address: ", ConsoleColor.PURPLE);
                try {
                    address = scanner.nextLine();
                    CustomerValidator.validateAddress(address);
                    break;
                } catch (Exception e) {
                    ConsoleColor.println("Error: " + e.getMessage() + " Please try again.", ConsoleColor.RED);
                }
            }

            LocalDate registrationDate = LocalDate.now();
            int loyaltyPoints = 10;

            int id = getCustomerId() + 1;
            Customer customer = new Customer(firstName, lastName, email, phone, address, registrationDate, loyaltyPoints);
            customer.setCustomerId(id);
            customerService.saveCustomer(customer);
            FileLogger.logApp("CustomerController - created: " + customer);
            ConsoleColor.println("Customer created successfully! with ID: " + id, ConsoleColor.GREEN);

            return customer;

        } catch (Exception e) {
            FileLogger.logError("CustomerController - Error creating customer: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void viewCustomers() {
        try {
            ConsoleColor.println("All Customers:", ConsoleColor.CYAN);
            for (Customer customer : customerService.getAllCustomers()) {
                ConsoleColor.println(customer.toString(), ConsoleColor.WHITE);
            }
        } catch (Exception e) {
            FileLogger.logError("CustomerController - Error viewing customers: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void searchForCustomer() {
        try {
            while (true) {
                ConsoleColor.print("Enter customer email to search: ", ConsoleColor.PURPLE);
                String email;
                try {
                    email = scanner.nextLine();
                    Customer customer = customerService.findCustomerByEmail(email.toLowerCase());
                    CustomerValidator.validateMail(email);
                    if (customer != null) {
                        ConsoleColor.println("Customer found: " + customer, ConsoleColor.GREEN);
                        FileLogger.logApp("CustomerController - found: " + customer);

                        ConsoleColor.println("\nWould you like to manage this Customer?", ConsoleColor.CYAN);
                        ConsoleColor.println("1. Update Customer", ConsoleColor.BLUE);
                        ConsoleColor.println("2. Update Loyalty Points", ConsoleColor.BLUE);
                        ConsoleColor.println("3. Remove Customer", ConsoleColor.BLUE);
                        ConsoleColor.println("4. No changes", ConsoleColor.BLUE);

                        int updateChoice = Integer.parseInt(scanner.nextLine());
                        if (updateChoice == 1) {
                            int id = customer.getCustomerId();
                            updateCustomer(id);
                        } else if (updateChoice == 2) {
                            ConsoleColor.println("Current loyalty points: " + customer.getLoyaltyPoints(), ConsoleColor.WHITE);
                            ConsoleColor.print("Enter points to add: ", ConsoleColor.PURPLE);
                            int pointsToAdd = Integer.parseInt(scanner.nextLine());
                            customer.setLoyaltyPoints(customer.getLoyaltyPoints() + pointsToAdd);
                            customerService.updateCustomer(customer);
                            FileLogger.logApp("CustomerController - updated loyalty points: " + customer);
                            ConsoleColor.println("Customer: " + customer.getFirstName() + " has now: " + customer.getLoyaltyPoints() + " total loyalty points", ConsoleColor.GREEN);
                            ConsoleColor.println("Loyalty points updated successfully!", ConsoleColor.GREEN);
                        } else if (updateChoice == 3) {
                            customerService.deleteCustomer(customer.getCustomerId());
                            FileLogger.logApp("CustomerController - removed: " + customer);
                            ConsoleColor.println("Customer " + customer.getFirstName() + " with ID: " + customer.getCustomerId() + " removed successfully!", ConsoleColor.GREEN);
                        } else if (updateChoice == 4) {
                            ConsoleColor.println("No changes made to the customer.", ConsoleColor.YELLOW);
                        } else {
                            ConsoleColor.println("No valid choice made.", ConsoleColor.RED);
                        }
                    } else {
                        ConsoleColor.println("Customer not found.", ConsoleColor.RED);
                    }
                    break;
                } catch (Exception e) {
                    ConsoleColor.println("Error: " + e.getMessage() + " Please try again.", ConsoleColor.RED);
                }
            }
        } catch (Exception e) {
            FileLogger.logError("CustomerController - Error searching for customer: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void updateCustomer(int id) {
        try {
            ConsoleColor.println("===== Update Customer =====", ConsoleColor.CYAN);

            String firstName;
            while (true) {
                ConsoleColor.print("Enter customer First name: ", ConsoleColor.PURPLE);
                try {
                    firstName = scanner.nextLine();
                    CustomerValidator.validateProperNoun(firstName);
                    break;
                } catch (Exception e) {
                    ConsoleColor.println("Error: " + e.getMessage() + " Please try again.", ConsoleColor.RED);
                }
            }

            String lastName;
            while (true) {
                ConsoleColor.print("Enter customer Last name: ", ConsoleColor.PURPLE);
                try {
                    lastName = scanner.nextLine();
                    CustomerValidator.validateProperNoun(lastName);
                    break;
                } catch (Exception e) {
                    ConsoleColor.println("Error: " + e.getMessage() + " Please try again.", ConsoleColor.RED);
                }
            }

            String email;
            while (true) {
                ConsoleColor.print("Enter customer email: ", ConsoleColor.PURPLE);
                try {
                    email = scanner.nextLine();
                    CustomerValidator.validateMail(email);
                    break;
                } catch (Exception e) {
                    ConsoleColor.println("Error: " + e.getMessage() + " Please try again.", ConsoleColor.RED);
                }
            }

            String phone;
            while (true) {
                ConsoleColor.print("Enter customer phone: ", ConsoleColor.PURPLE);
                try {
                    phone = scanner.nextLine();
                    CustomerValidator.validateOnlyNumbers(phone, 20, 6);
                    break;
                } catch (Exception e) {
                    ConsoleColor.println("Error: " + e.getMessage() + " Please try again.", ConsoleColor.RED);
                }
            }

            String address;
            while (true) {
                ConsoleColor.print("Enter customer address: ", ConsoleColor.PURPLE);
                try {
                    address = scanner.nextLine();
                    CustomerValidator.validateAddress(address);
                    break;
                } catch (Exception e) {
                    ConsoleColor.println("Error: " + e.getMessage() + " Please try again.", ConsoleColor.RED);
                }
            }

            LocalDate registrationDate = LocalDate.now();
            int loyaltyPoints = 10;

            Customer updatedCustomer = new Customer(firstName, lastName, email, phone, address, registrationDate, loyaltyPoints);
            updatedCustomer.setCustomerId(id);
            customerService.updateCustomer(updatedCustomer);
            FileLogger.logApp("CustomerController - updated: " + updatedCustomer);
            ConsoleColor.println("Customer updated successfully!", ConsoleColor.GREEN);
        } catch (Exception e) {
            FileLogger.logError("CustomerController - Error updating customer: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void deleteCustomer() {
        try {
            ConsoleColor.print("Enter customer ID to delete: ", ConsoleColor.PURPLE);
            int id = Integer.parseInt(scanner.nextLine());
            Customer customerToRemove = customerService.findCustomerById(id);
            ConsoleColor.println("Are you sure you want to delete customer: " + customerToRemove.getFirstName() + " with ID " + id + "? (yes/no)", ConsoleColor.YELLOW);
            String confirmation = scanner.nextLine();
            if (!confirmation.equalsIgnoreCase("yes")) {
                ConsoleColor.println("Customer deletion cancelled.", ConsoleColor.YELLOW);
                return;
            } else {
                ConsoleColor.println("Customer deletion confirmed.", ConsoleColor.GREEN);
            }
            customerService.deleteCustomer(id);
            FileLogger.logApp("CustomerController - deleted: " + id);
            ConsoleColor.println("Customer deleted successfully!", ConsoleColor.GREEN);
        } catch (Exception e) {
            FileLogger.logError("CustomerController - Error deleting customer: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private int getCustomerId() {
        int id = 0;
        try {
            for (Customer customer : customerService.getAllCustomers()) {
                if (customer.getCustomerId() > id) {
                    id = customer.getCustomerId();
                }
            }
            return id;
        } catch (Exception e) {
            FileLogger.logError("Error getting customer ID: " + e.getMessage());
            return -1;
        }
    }
}