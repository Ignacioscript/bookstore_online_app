package org.ignacioScript.co.io;

import org.ignacioScript.co.model.Author;
import org.ignacioScript.co.model.Customer;
import org.ignacioScript.co.util.FileLogger;
import org.ignacioScript.co.validation.FileManagerValidator;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerFileManager extends FileManager <Customer> {
    public CustomerFileManager(String filePath) {
        super(filePath);
        FileLogger.logInfo("Initialized CustomerFileManager for: " + filePath);
    }


    @Override
    public void save(List<Customer> customers) {
    FileLogger.logInfo("Starting to save " + customers.size() + " customers");


            FileManagerValidator.validateExistingFile(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Customer customer : customers) {
                writer.write(objectToString(customer));
                writer.newLine();
                FileLogger.logInfo("Save customer: " + customer);
            }
            FileLogger.logInfo("Successfully saved all customers");

        }catch (IOException e) {
            FileLogger.logError("ERROR saving customers " + e.getMessage());
            throw new RuntimeException("Save operation failed");
        }
    }

    public void appendCustomer(Customer customer) {
        FileLogger.logInfo("Starting to append a new Customer ");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(objectToString(customer));
            writer.newLine();

        }catch (IOException e) {
            FileLogger.logError("ERROR appending a new customer: " + e.getMessage());
            throw  new RuntimeException("Append Operation failed" );
        }
    }

    @Override
    public List<Customer> load() throws IOException {
        List<Customer> customers = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
         String line;
         while ((line = reader.readLine()) != null) {
             customers.add(stringToObject(line));
         }
        }
        return customers;
    }

    @Override
    public void delete(int id) {
        FileLogger.logInfo("Attempting to delete customer with ID: "  + id);
        try {
            List<Customer> customers = load();
            Customer customerToRemove = null;

            for (Customer customer : customers) {
                if (customer.getCustomerId() == id) {
                    customerToRemove = customer;
                    break;
                }
            }

            if (customerToRemove != null) {
                customers.remove(customerToRemove);
                save(customers);
                FileLogger.logInfo("Successfuly deleted customer with ID: " + id);
            }else {
                FileLogger.logInfo("No Customer found it with ID:  " + id);
            }

        }catch (IOException e) {
            FileLogger.logError("ERROR deleting customer: " + e.getMessage());
        }

    }

    @Override
    public void update(Customer customer) {
        FileLogger.logInfo("Attempting to update customer with ID: " + customer.getCustomerId());
        try {
            List<Customer> customers = load();
            boolean updated = false;

            for (int i = 0; i < customers.size(); i ++) {
                if (customers.get(i).getCustomerId() == customer.getCustomerId()) {
                    customers.set(i, customer);
                    updated = true;
                    break;
                }
            }

            if (updated) {
                save(customers);
                FileLogger.logInfo("Successfully updated customer with ID: " + customer.getCustomerId());
            }else {
                FileLogger.logInfo("No customer found with ID: " + customer.getCustomerId());
                throw new RuntimeException("Customer with ID: " + customer.getCustomerId() + " Not found");
            }

        }catch (IOException e) {
            FileLogger.logError("ERROR updating customer: " + e.getMessage());

        }

    }


    @Override
    public Customer getById(int id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Customer customer = stringToObject(line);

                if (customer.getCustomerId() == id) {
                    FileLogger.logInfo("CustomerFileManager - Customer found with ID: " + id);
                    return customer; // Return the customer immediately
                }
            }

            // Log if the customer is not found
            FileLogger.logInfo("CustomerFileManager - Customer with ID " + id + " not found.");
        } catch (IOException e) {
            // Log the error and wrap it
            FileLogger.logError("CustomerFileManager - Error reading file for getById: " + e.getMessage());
            throw new RuntimeException("Error while reading file to search for customer with ID " + id, e);
        }

        // Return null if the customer is not found
        return null;
    }

    public Customer getByEmail(String email) {

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Customer customer = stringToObject(line);

                if (customer.getMail().equalsIgnoreCase(email)) {
                    FileLogger.logInfo("CustomerFileManager - Customer found with email: " + email);
                    return customer; // Return the customer immediately
                }
            }

            // Log if the customer is not found
            FileLogger.logInfo("CustomerFileManager - Customer with email " + email + " not found.");
        } catch (IOException e) {
            // Log the error and wrap it
            FileLogger.logError("CustomerFileManager - Error reading file for getByEmail: " + e.getMessage());
            throw new RuntimeException("Error while reading file to search for customer with email " + email, e);
        }

        // Return null if the customer is not found
        return null;
    }


    @Override
    protected String objectToString(Customer customer) {

        return customer.toCsvString();
    }

    @Override
    protected Customer stringToObject(String line) {
        String[] parts = line.split(",");
        if (parts.length < 8) { // Ensure there are at least 8 parts
            throw new IllegalArgumentException("Invalid customer data: " + line);
        }
        Customer customer =  new Customer(
                parts[1],
                parts[2],
                parts[3],
                parts[4],
                parts[5],
                LocalDate.parse(parts[6]),
                Integer.parseInt(parts[7])
        );

        customer.setCustomerId(Integer.parseInt(parts[0]));
        return customer;
    }



}
