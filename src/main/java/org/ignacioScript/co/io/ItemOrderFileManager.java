package org.ignacioScript.co.io;

import org.ignacioScript.co.model.Book;
import org.ignacioScript.co.model.ItemOrder;
import org.ignacioScript.co.model.Order;
import org.ignacioScript.co.util.FileLogger;
import org.ignacioScript.co.validation.FileManagerValidator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ItemOrderFileManager extends FileManager<ItemOrder> {

    private BookFileManager bookFile;
    private OrderFileManager orderFile;

    public ItemOrderFileManager(String filePath, String bookFilePath, String orderFilePath, String customerFilePath) {
        super(filePath);
        this.bookFile = new BookFileManager(bookFilePath);
        this.orderFile = new OrderFileManager(orderFilePath, customerFilePath);
        FileLogger.log("ItemOrderFileManager initialized.");
    }

    @Override
    public void save(List<ItemOrder> itemOrders) {
        FileLogger.log("Saving item orders to file.");
        FileManagerValidator.validateExistingFile(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (ItemOrder itemOrder : itemOrders) {
                writer.write(objectToString(itemOrder));
                writer.newLine();
            }
        } catch (IOException e) {
            FileLogger.log("ERROR saving item orders.");
            throw new RuntimeException("Save operation failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ItemOrder> load() throws IOException {
        List<ItemOrder> itemOrders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                itemOrders.add(stringToObject(line));
            }
        }
        return itemOrders;
    }

    @Override
    public ItemOrder getById(int id) {

        try {
            List<ItemOrder> orders = load();
            for (ItemOrder itemOrder : orders) {
                if (itemOrder.getItemOrderId() == id) {
                    return itemOrder;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;

    }

    @Override
    protected String objectToString(ItemOrder itemOrder) {
        return String.join(",",
                String.valueOf(itemOrder.getItemOrderId()),
                String.valueOf(itemOrder.getBook().getBookId()),
                String.valueOf(itemOrder.getOrder().getOrderId()),
                String.valueOf(itemOrder.getQuantity()),
                String.valueOf(itemOrder.getUnitPrice())
        );
    }

    @Override
    protected ItemOrder stringToObject(String line) {
        String[] parts = line.split(",");
        if (parts.length < 5) {
            throw new IllegalArgumentException("Invalid item order data: " + line);
        }
        try {
            int itemOrderId = Integer.parseInt(parts[0]); // << parse itemOrderId
            int bookId = Integer.parseInt(parts[1]);
            int orderId = Integer.parseInt(parts[2]);
            int quantity = Integer.parseInt(parts[3]);
            double unitPrice = Double.parseDouble(parts[4]);

            Book book = bookFile.getById(bookId);
            Order order = orderFile.getById(orderId);

            if (book == null || order == null) {
                throw new IllegalArgumentException("Book or Order not found for IDs: " + bookId + ", " + orderId);
            }

            ItemOrder itemOrder = new ItemOrder(book, order, quantity, unitPrice);
            itemOrder.setItemOrderId(itemOrderId); // << manually set the loaded ID
            return itemOrder;

        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize item order: " + line, e);
        }
    }

}