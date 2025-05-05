package org.ignacioScript.co.seeder;

import org.ignacioScript.co.model.Customer;
import org.ignacioScript.co.service.CustomerService;

import java.time.LocalDate;
import java.util.List;

public class CustomerSeeder {

    public static void seedCustomers(CustomerService customerService) {

        // Example customers
        try {
            List<Customer> customers = List.of(
                    new Customer("John", "Doe", "john.doe@example.com", "1234567890", "123 Main St", LocalDate.now(), 10),
                    new Customer("Jane", "Smith", "jane.smith@example.com", "9876543210", "456 Elm St", LocalDate.now(), 10),
                    new Customer("Alice", "Johnson", "alice.johnson@example.com", "5551234567", "789 Oak St", LocalDate.now(), 10),
                    new Customer("Bob", "Brown", "bob.brown@example.com", "4449876543", "321 Pine St", LocalDate.now(), 10),
                    new Customer("Charlie", "Davis", "charlie.davis@example.com", "3334567890", "654 Maple St", LocalDate.now(), 10),
                    new Customer("Diana", "Evans", "diana.evans@example.com", "2221234567", "987 Birch St", LocalDate.now(), 10),
                    new Customer("Ethan", "Foster", "ethan.foster@example.com", "1119876543", "159 Cedar St", LocalDate.now(), 10),
                    new Customer("Fiona", "Garcia", "fiona.garcia@example.com", "6664567890", "753 Walnut St", LocalDate.now(), 10),
                    new Customer("George", "Harris", "george.harris@example.com", "7771234567", "951 Chestnut St", LocalDate.now(), 10),
                    new Customer("Hannah", "Ivy", "hannah.ivy@example.com", "8889876543", "357 Spruce St", LocalDate.now(), 10)
            );

            // Save each customer
            for (Customer customer : customers) {
                customerService.saveCustomer(customer);
            }

            System.out.println("Customers seeded successfully!");
        } catch (Exception e) {
            System.err.println("Error seeding customers: " + e.getMessage());
            throw new RuntimeException(e);

        }
    }
}
