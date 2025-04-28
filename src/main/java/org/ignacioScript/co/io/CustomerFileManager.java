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
    protected void delete(int id) {

    }

    @Override
    protected void update(Customer customer) {

    }

    @Override
    protected Customer getById(int id) {
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
        return new Customer(
                parts[1],
                parts[2],
                parts[3],
                parts[4],
                parts[5],
                LocalDate.parse(parts[6]),
                Integer.parseInt(parts[7])
        );
    }


}
