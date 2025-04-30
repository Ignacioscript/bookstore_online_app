package org.ignacioScript.co.io;

import org.ignacioScript.co.model.Customer;
import org.ignacioScript.co.model.Order;
import org.ignacioScript.co.model.Status;
import org.ignacioScript.co.util.FileLogger;
import org.ignacioScript.co.validation.FileManagerValidator;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderFileManager extends FileManager<Order> {

    private final CustomerFileManager customerFile;

    public OrderFileManager(String filePath, String customerFilePath) {
        super(filePath);
        this.customerFile = new CustomerFileManager(customerFilePath);
        FileLogger.logInfo("OrderFileManager initialized.");
    }



    @Override
    public void save(List<Order> orders) {
        FileLogger.logInfo("Saving orders to file.");
        FileManagerValidator.validateExistingFile(filePath);
        FileManagerValidator.validateExistingFile(customerFile.filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Order order : orders) {
                writer.write(objectToString(order));
                writer.newLine();
            }
        } catch (IOException e) {
            FileLogger.logError("ERROR saving orders.");
            throw new RuntimeException("Save operation failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Order> load() throws IOException {
        List<Order> orders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                orders.add(stringToObject(line));
            }
        }
        return orders;
    }

    @Override
    public void delete(int id) {
        FileLogger.logInfo("Attempting to update order with ID: " + id);
        try {
            List<Order> orders = load();
            Order orderToRemove = null;

            for (Order order : orders) {
                if (order.getOrderId() == id) {
                    orderToRemove = order;
                    break;
                }
            }

            if (orderToRemove != null) {
                orders.remove(orderToRemove);
                save(orders);
                FileLogger.logInfo("Successfully deleted order with ID: " + id);
            }else {
                FileLogger.logInfo("No Order found with ID: " + id);
            }

        }catch (IOException e) {
            FileLogger.logError("ERROR deleting order: " + e.getMessage());
        }
    }

    @Override
    public void update(Order order) {
         FileLogger.logInfo("Attempting to update order with ID: " + order.getOrderId());
         try {
             List<Order> orders = load();
             boolean updated = false;

             for (int i = 0; i < orders.size(); i ++) {
                 if (orders.get(i).getOrderId() == order.getOrderId()) {
                     orders.set(i, order);
                     updated = true;
                     break;
                 }
             }

             if (updated) {
                 save(orders);
                 FileLogger.logInfo("Successfully updated order with ID: " + order.getOrderId());
             }else {
                 FileLogger.logInfo("Mo Order found with ID: " + order.getOrderId());
                 throw new RuntimeException("Order with ID: " + order.getOrderId() + " Not found");
             }

         }catch (IOException e) {
             FileLogger.logError("ERROR updating order: " + e.getMessage());
         }

    }

    @Override
    public Order getById(int id) {
       try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
           String line;
           while ((line = reader.readLine()) != null) {
               Order order = stringToObject(line);
               if (order.getOrderId() == id) {
                   FileLogger.logInfo("OrderFileManager - Order found with ID: " + id);
                   return  order;
               }
           }

           FileLogger.logInfo("OrderFileManager - Order with ID: " + id + " Not found");
       }catch (IOException e) {
           FileLogger.logError("OrderFileManager - Error reading file for getById: " + e.getMessage());
           throw new RuntimeException("Error while reading file to search for order with ID: " + id, e);
       }
        return null;

    }

    @Override
    protected String objectToString(Order order) {
        return String.join(",",
                String.valueOf(order.getOrderId()),
                order.getCustomer().toCsvString(),
                String.valueOf(order.getOrderDate()),
                String.valueOf(order.getTotal()),
                order.getPaymentMethod(),
                order.getStatus().toString()
        );
    }

    @Override
    protected Order stringToObject(String line) {
        String[] parts = line.split(",");
        if (parts.length < 13) { // Order + embedded Customer fields
            throw new IllegalArgumentException("Invalid order data: " + line);
        }
        try {
            // Parse Customer fields
            Customer customer = new Customer(
                    parts[2],  // firstName
                    parts[3],  // lastName
                    parts[4],  // email
                    parts[5],  // phoneNumber
                    parts[6],  // address
                    LocalDate.parse(parts[7]), // registrationDate
                    Integer.parseInt(parts[8]) // loyaltyPoints
            );
            customer.setCustomerId(Integer.parseInt(parts[1])); // << SET Customer ID manually

            // Parse Order fields
            LocalDate orderDate = LocalDate.parse(parts[9]);
            double total = Double.parseDouble(parts[10]);
            String paymentMethod = parts[11];
            Status status = Status.valueOf(parts[12]);

            Order order = new Order(
                    customer,
                    orderDate,
                    total,
                    paymentMethod,
                    status
            );
            order.setOrderId(Integer.parseInt(parts[0])); // << SET Order ID manually 🔥
            return order;

        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing order data: " + line, e);
        }
    }


//    @Override
//   protected Order stringToObject(String line) {
//        String[] parts = line.split(",");
//
//
//
//        Customer customer = customerFile.getById(Integer.parseInt(parts[1]));
//        LocalDate orderDate = LocalDate.parse(parts[9]);
//        double total = Double.parseDouble(parts[]);
//        String paymentMethod = parts[4];
//        Status status = Status.valueOf(parts[5]);
//
//        return new Order(
//                customer,
//                orderDate,
//                total,
//                paymentMethod,
//                status
//
//        );
//
//    }



}