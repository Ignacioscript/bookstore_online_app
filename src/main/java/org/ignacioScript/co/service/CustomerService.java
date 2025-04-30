package org.ignacioScript.co.service;

import org.ignacioScript.co.io.CustomerFileManager;
import org.ignacioScript.co.model.Customer;
import org.ignacioScript.co.util.FileLogger;
import org.ignacioScript.co.validation.CustomerValidator;

import java.io.IOException;
import java.util.List;


//TODO finish services for all the remain classes.
public class CustomerService {

    private final CustomerFileManager customerFileManager;

    // Constructor with a default filepath
    private static final String DEFAULT_FILE_PATH = "src/main/resources/customers.csv";

    public CustomerService() {
        this.customerFileManager = new CustomerFileManager(DEFAULT_FILE_PATH);
    }

    // Constructor with a custom file manager (useful for tests or flexible system design)
    public CustomerService(CustomerFileManager customerFileManager) {
        this.customerFileManager = customerFileManager;
    }

    // Save customer to file after validation
    public void saveCustomer(Customer customer) {
        validateCustomer(customer); // Perform business validation

        customerFileManager.appendCustomer(customer);
        FileLogger.logInfo("CustomerService - Customer saved successfully: " + customer);
    }

    // Update customer
    public void updateCustomer(Customer customer) {
        validateCustomer(customer);

        if (!doesCustomerExist(customer.getCustomerId())) {
            throw new IllegalArgumentException("Customer with ID " + customer.getCustomerId() + " does not exist.");
        }

        customerFileManager.update(customer);
        FileLogger.logInfo("CustomerService - Customer updated successfully: " + customer);
    }

    // Delete customer
    public void deleteCustomer(int id) {
        if (!doesCustomerExist(id)) {
            throw new IllegalArgumentException("Customer with ID " + id + " does not exist.");
        }

        customerFileManager.delete(id);
        FileLogger.logInfo("CustomerService - Customer with ID " + id + " deleted successfully.");
    }

    // Retrieve all customers
    public List<Customer> getAllCustomers() {
        try {
            FileLogger.logInfo("CustomerService - Fetching all customers from file.");
            return customerFileManager.load();
        } catch (IOException e) {
            FileLogger.logError("CustomerService  - Error fetching customers: " + e.getMessage());
            throw new RuntimeException("Failed to load customers", e);
        }
    }

    // Find a customer by ID (add flexibility to your service)
    public Customer findCustomerById(int id) {
        // Delegate to CustomerFileManager's getById
        try {
            Customer customer = customerFileManager.getById(id);

            if (customer == null) {
                throw new IllegalArgumentException("Customer with ID " + id + " does not exist.");
            }

            FileLogger.logInfo("CustomerService - Successfully found customer with ID: " + id);
            return customer;

        } catch (RuntimeException e) {
            FileLogger.logError("CustomerService - Error finding customer: " + e.getMessage());
            throw e; // Rethrow to propagate the error upwards
        }
    }



    // Validation logic
    private void validateCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }

        // Example validations (reuse your existing validator methods)
        CustomerValidator.validateProperNoun(customer.getFirstName());
        CustomerValidator.validateProperNoun(customer.getLastName());
        CustomerValidator.validateMail(customer.getMail());

        // Add other business-specific validations like max credit limit, valid phone numbers, etc.
    }

    // Check if a customer exists (by ID) - helps in update and delete methods
    private boolean doesCustomerExist(int id) {
        return getAllCustomers().stream()
                .anyMatch(customer -> customer.getCustomerId() == id);
    }
}
