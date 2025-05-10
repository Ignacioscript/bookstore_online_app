package org.ignacioScript.co.service;

import org.ignacioScript.co.io.ItemOrderFileManager;
import org.ignacioScript.co.model.ItemOrder;
import org.ignacioScript.co.util.FileLogger;
import org.ignacioScript.co.validation.ItemOrderValidator;

import java.io.IOException;
import java.util.List;

public class ItemOrderService {

    private final ItemOrderFileManager itemOrderFileManager;
    private final String DEFAULT_FILE_PATH = "src/main/resources/item_orders.csv";

    public ItemOrderService(ItemOrderFileManager itemOrderFileManager) {
        this.itemOrderFileManager = itemOrderFileManager;
    }

    public ItemOrderService() {
        this.itemOrderFileManager = new ItemOrderFileManager(DEFAULT_FILE_PATH,
                "src/main/resources/books.csv",
                "src/main/resources/orders.csv",
                "src/main/resources/customers.csv");
    }

    public void saveItemOrder(ItemOrder itemOrder) {
        validateItemOrder(itemOrder);

        itemOrderFileManager.append(itemOrder);
        FileLogger.logInfo("ItemOrderService - ItemOrder saved successfully");
    }

    public void updateItemOrder(ItemOrder itemOrder) {
        validateItemOrder(itemOrder);

        ItemOrder existingItemOrder = findItemOrderById(itemOrder.getItemOrderId());
        if (existingItemOrder == null) {
            throw new IllegalArgumentException("ItemOrder with ID " + itemOrder.getItemOrderId() + " does not exist");
        }

        itemOrderFileManager.update(itemOrder);
        FileLogger.logInfo("ItemOrderService - ItemOrder updated successfully: " + itemOrder);
    }

    public void deleteItemOrder(int id) {
        ItemOrder existingItemOrder = findItemOrderById(id);
        if (existingItemOrder == null) {
            throw new IllegalArgumentException("ItemOrder with ID " + id + " does not exist");
        }

        itemOrderFileManager.delete(id);
        FileLogger.logInfo("ItemOrderService - ItemOrder deleted successfully: " + existingItemOrder);
    }

    public ItemOrder findItemOrderById(int id) {
        ItemOrderValidator.validateId(id);

        try {
            ItemOrder itemOrder = itemOrderFileManager.getById(id);
            if (itemOrder == null) {
                throw new IllegalArgumentException("ItemOrder with ID: " + id + " does not exist");
            }

            FileLogger.logInfo("ItemOrderService - Successfully found ItemOrder with ID: " + id);
            return itemOrder;

        } catch (RuntimeException e) {
            FileLogger.logError("ItemOrderService - Error finding ItemOrder: " + e.getMessage());
            throw e;
        }
    }

    public List<ItemOrder> getAllItemOrders() {
        try {
            FileLogger.logInfo("ItemOrderService - Fetching all ItemOrders from file");
            return itemOrderFileManager.load();
        } catch (IOException e) {
            FileLogger.logError("ItemOrderService - Error fetching ItemOrders: " + e.getMessage());
            throw new RuntimeException("Failed to load ItemOrders");
        }
    }

    public Double getTotal(ItemOrder itemOrder, double unitPrice, int quantity) {
        FileLogger.logInfo("ItemOrderService - Getting total");

        try {
            double total = itemOrder.getTotal() * itemOrder.getQuantity();
            ItemOrderValidator.validateUnits(total, 1, 500000);
            FileLogger.logInfo("ItemOrderService - Getting total operation success");
            return total;
        }catch (Exception e) {
            FileLogger.logError("ItemOrderService - Error getting total" + e.getMessage());
            throw new RuntimeException("Getting total failed");
        }

    }

    private void validateItemOrder(ItemOrder itemOrder) {
        if (itemOrder == null) {
            throw new IllegalArgumentException("ItemOrder cannot be null");
        }

        ItemOrderValidator.validateId(itemOrder.getItemOrderId());
        ItemOrderValidator.validateUnits(itemOrder.getQuantity(), 1, 500);
        ItemOrderValidator.validateUnits(itemOrder.getTotal(), 1.0, 500000);
        ItemOrderValidator.validateBookNullParameter(itemOrder.getBook());
        ItemOrderValidator.validateOrderNullParameter(itemOrder.getOrder());
    }
}