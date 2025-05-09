package org.ignacioScript.co.controller;

import org.ignacioScript.co.model.Customer;
import org.ignacioScript.co.seeder.CustomerSeeder;
import org.ignacioScript.co.service.CustomerService;
import org.ignacioScript.co.util.FileLogger;
import org.ignacioScript.co.validation.CustomerValidator;

import java.time.LocalDate;
import java.util.Scanner;

public class CustomerController {

    private  CustomerService customerService;
    private  static  Scanner scanner;

    public CustomerController(Scanner scanner, CustomerService customerService) {
        this.customerService = customerService;
        this.scanner = scanner;


    }

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
        this.scanner = new Scanner(System.in);
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
                case 4 -> deleteCustomer();
                case 5 -> {
                    System.out.println("Returning to Main Menu...\n");
                    MenuController.runMenu();
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
        System.out.println("4. Delete Customer");
        System.out.println("5. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }




    public  Customer createCustomer() {
        try {
            System.out.println("===== Create Customer =====");

            String firstName;
            while (true) {
                System.out.println("Enter customer First name:");
                try {
                    firstName = scanner.nextLine();
                    CustomerValidator.validateProperNoun(firstName);
                    break;
                }catch (Exception e) {
                    System.err.println("Error: " + e.getMessage() + " Please try again.");
                }
            }

            String lastName;
            while (true) {
                System.out.println("Enter customer Last name:");
                try {
                    lastName = scanner.nextLine();
                    CustomerValidator.validateProperNoun(lastName);
                    break;
                }catch (Exception e) {
                    System.err.println("Error: " + e.getMessage() + " Please try again.");
                }
            }

            String email;
            while (true) {
                System.out.println("Enter customer email:");
                try {
                    email = scanner.nextLine();
                    CustomerValidator.validateMail(email);
                    break;
                }catch (Exception e) {
                    System.err.println("Error: " + e.getMessage() + " Please try again.");
                }
            }

            String phone;
            while (true) {
                System.out.println("Enter customer phone:");
                try {
                    phone = scanner.nextLine();
                    CustomerValidator.validateOnlyNumbers(phone, 20, 6);
                    break;
                }catch (Exception e) {
                    System.err.println("Error: " + e.getMessage() + " Please try again.");
                }
            }

            String address;
            while (true) {
                System.out.println("Enter customer address:");
                try {
                    address = scanner.nextLine();
                    CustomerValidator.validateAddress(address);
                    break;
                }catch (Exception e) {
                    System.err.println("Error: " + e.getMessage() + " Please try again.");
                }
            }

            LocalDate registrationDate = LocalDate.now(); // Default value for registration date
            int loyaltyPoints =  10; // Default value for loyalty points

            int id = getCustomerId() + 1; // Increment the ID for the new customer
            Customer customer = new Customer(firstName, lastName, email, phone, address, registrationDate, loyaltyPoints);
            customer.setCustomerId(id);
            customerService.saveCustomer(customer);
            FileLogger.logApp("CustomerController - created: " + customer);
            System.out.println("Customer created successfully! with ID: " + id);

            return customer;

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

    public void searchForCustomer() {
        try {

            while (true) {
                System.out.println("Enter customer email to search:");
                String email;
                try {

                    email = scanner.nextLine();
                    Customer customer = customerService.findCustomerByEmail(email.toLowerCase());
                    CustomerValidator.validateMail(email);
                    if (customer != null) {
                        System.out.println("Customer found: " + customer);
                        FileLogger.logApp("CustomerController - found: " + customer);

                        System.out.println("\n");
                        System.out.println("Would you like to manage this Customer?");
                        System.out.println("1. Update Customer");
                        System.out.println("2. Update Loyalty Points");
                        System.out.println("3. Remove Customer");
                        System.out.println("4. No changes");

                        int updateChoice = Integer.parseInt(scanner.nextLine());
                        if (updateChoice == 1) {
                            int id = customer.getCustomerId();
                            updateCustomer(id);

                        } else if (updateChoice == 2) {
                            System.out.println("Current loyalty points: " + customer.getLoyaltyPoints());
                            System.out.println("Enter points to add:");
                            int pointsToAdd = Integer.parseInt(scanner.nextLine());
                            customer.setLoyaltyPoints(customer.getLoyaltyPoints() + pointsToAdd);
                            customerService.updateCustomer(customer);
                            FileLogger.logApp("CustomerController - updated loyalty points: " + customer);
                            System.out.println("Customer: " + customer.getFirstName() +  " has now : " + customer.getLoyaltyPoints() + " total loyalty points");
                            System.out.println("Loyalty points updated successfully!");
                        } else if (updateChoice == 3) {
                            //deleteCustomer();
                            customerService.deleteCustomer(customer.getCustomerId());
                            FileLogger.logApp("CustomerController - removed: " + customer);
                            System.out.println("Customer " + customer.getFirstName() + " with ID: " + customer.getCustomerId() + " removed successfully!");
                        } else if (updateChoice == 4) {
                            System.out.println("No changes made to the customer.");
                        }
                        else {
                            System.out.println("No valid choice made.");
                        }

                    } else {
                        System.out.println("Customer not found.");
                    }
                break;
                }catch (Exception e) {
                    System.err.println("Error: " + e.getMessage() + " Please try again.");
                }

            }

        } catch (Exception e) {
            FileLogger.logError("CustomerController - Error searching for customer: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private  void updateCustomer(int id) {
        try {

            System.out.println("===== Update Customer =====");


            String firstName;
            while (true) {
                System.out.println("Enter customer First name:");
                try {
                    firstName = scanner.nextLine();
                    CustomerValidator.validateProperNoun(firstName);
                    break;
                }catch (Exception e) {
                    System.err.println("Error: " + e.getMessage() + " Please try again.");
                }
            }

            String lastName;
            while (true) {
                System.out.println("Enter customer Last name:");
                try {
                    lastName = scanner.nextLine();
                    CustomerValidator.validateProperNoun(lastName);
                    break;
                }catch (Exception e) {
                    System.err.println("Error: " + e.getMessage() + " Please try again.");
                }
            }

            String email;
            while (true) {
                System.out.println("Enter customer email:");
                try {
                    email = scanner.nextLine();
                    CustomerValidator.validateMail(email);
                    break;
                }catch (Exception e) {
                    System.err.println("Error: " + e.getMessage() + " Please try again.");
                }
            }

            String phone;
            while (true) {
                System.out.println("Enter customer phone:");
                try {
                    phone = scanner.nextLine();
                    CustomerValidator.validateOnlyNumbers(phone, 20, 6);
                    break;
                }catch (Exception e) {
                    System.err.println("Error: " + e.getMessage() + " Please try again.");
                }
            }

            String address;
            while (true) {
                System.out.println("Enter customer address:");
                try {
                    address = scanner.nextLine();
                    CustomerValidator.validateAddress(address);
                    break;
                }catch (Exception e) {
                    System.err.println("Error: " + e.getMessage() + " Please try again.");
                }
            }

            LocalDate registrationDate = LocalDate.now(); // Default value for registration date
            int loyaltyPoints =  10; // Default value for loyalty points

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

    public void deleteCustomer() {
        try {
            System.out.println("Enter customer ID to delete:");
            int id = Integer.parseInt(scanner.nextLine());
           Customer customerToRemove = customerService.findCustomerById(id);
            System.out.println("Are you sure you want to delete customer: "+ customerToRemove.getFirstName()  + " with ID " + id + "? (yes/no)");
            String confirmation = scanner.nextLine();
            if (!confirmation.equalsIgnoreCase("yes")) {
                System.out.println("Customer deletion cancelled.");
                return;
            } else {
                System.out.println("Customer deletion confirmed.");
            }
            customerService.deleteCustomer(id);
            FileLogger.logApp("CustomerController - deleted: " + id);
            System.out.println("Customer deleted successfully!");
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
            return -1; // Return an invalid ID in case of error
        }
    }


}
