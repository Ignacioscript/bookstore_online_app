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
        FileLogger.log("Initialized CustomerFileManager for: " + filePath);
    }


    @Override
    public void save(List<Customer> customers) {
    FileLogger.log("Starting to save " + customers.size() + " customers");


            FileManagerValidator.validateExistingFile(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Customer customer : customers) {
                writer.write(objectToString(customer));
                writer.newLine();
                FileLogger.log("Save customer: " + customer);
            }
            FileLogger.log("Successfully saved all customers");

        }catch (IOException e) {
            FileLogger.log("ERROR saving customers " + e.getMessage());
            throw new RuntimeException("Save operation failed");
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
        FileLogger.log("Attempting to delete customer with ID: "  + id);
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
                FileLogger.log("Successfuly deleted customer with ID: " + id);
            }else {
                FileLogger.log("No Customer found it with ID:  " + id);
            }

        }catch (IOException e) {
            FileLogger.log("ERROR deleting customer: " + e.getMessage());
        }

    }

    @Override
    public void update(Customer customer) {
        FileLogger.log("Attempting to update customer with ID: " + customer.getCustomerId());
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
                FileLogger.log("Successfully updated customer with ID: " + customer.getCustomerId());
            }else {
                FileLogger.log("No customer found with ID: " + customer.getCustomerId());
                throw new RuntimeException("Customer with ID: " + customer.getCustomerId() + " Not found");
            }

        }catch (IOException e) {
            FileLogger.log("ERROR updating customer: " + e.getMessage());

        }

    }

    @Override
    public Customer getById(int id) {
        List<Customer> customers = new ArrayList<>();
           try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
               String line;
               while ((line = reader.readLine()) != null) {
                   customers.add(stringToObject(line));
               }

       } catch (IOException e) {
           throw new RuntimeException(e);
       }
           return customers.get(id);

    }

//    @Override
//    public Customer getById(int id) {
//        try {
//            for (Customer customer : load()) {
//                if (customer.getCustomerId() == id) {
//                    return customer;
//                }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to load customers.", e);
//        }
//        throw new IllegalArgumentException("Customer with ID " + id + " not found.");
//    }


    // TODO finish this helper methods
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
