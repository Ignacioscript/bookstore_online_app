package org.ignacioScript.co.controller;

import org.ignacioScript.co.model.Book;
import org.ignacioScript.co.model.ItemOrder;
import org.ignacioScript.co.model.Order;
import org.ignacioScript.co.service.BookService;
import org.ignacioScript.co.service.CustomerService;
import org.ignacioScript.co.service.ItemOrderService;
import org.ignacioScript.co.util.ConsoleColor;
import org.ignacioScript.co.util.FileLogger;

import java.util.List;
import java.util.Scanner;

public class ItemOrderController {

    private final OrderController orderController;
    private final CustomerService customerService;
    private final BookService bookService;
    private final ItemOrderService itemOrderService;
    private Scanner scanner;

    public ItemOrderController(Scanner scanner, OrderController orderController, CustomerService customerService, BookService bookService, ItemOrderService itemOrderService) {
        this.orderController = orderController;
        this.customerService = customerService;
        this.bookService = bookService;
        this.itemOrderService = itemOrderService;
        this.scanner = scanner;
    }

    public ItemOrderController(Scanner scanner, CustomerService customerService, BookService bookService) {
        this.customerService = customerService;
        this.bookService = bookService;
        this.orderController = new OrderController();
        this.itemOrderService = new ItemOrderService();
        this.scanner = scanner;
    }

    public void displayItemOrderMenu() {
        ConsoleColor.println("\n===== Order Menu =====", ConsoleColor.CYAN);
        ConsoleColor.println("1. Create Order", ConsoleColor.BLUE);
        ConsoleColor.println("2. Update Order", ConsoleColor.BLUE);
//        ConsoleColor.println("3. Delete Order", ConsoleColor.BLUE);
        ConsoleColor.println("3. Find Order by ID", ConsoleColor.BLUE);
        ConsoleColor.println("4. List All Item Orders", ConsoleColor.BLUE);
        ConsoleColor.println("5. Back to Main Menu", ConsoleColor.BLUE);
        ConsoleColor.print("Enter your choice: ", ConsoleColor.PURPLE);

        int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> createItemOrder();
            case 2 -> updateItemOrder();
//            case 3 -> deleteItemOrder();
            case 3 -> findItemOrderById();
            case 4 -> listAllItemOrders();
            case 5 -> {
                ConsoleColor.println("Returning to Main Menu...\n", ConsoleColor.GREEN);
                MenuController.runMenu();
            }
            default -> ConsoleColor.println("Invalid option, please try again.", ConsoleColor.RED);
        }
    }

    private void createItemOrder() {
        ConsoleColor.println("\n===== Create New Order =====", ConsoleColor.CYAN);
        Book book;
        int numOfBooks;

        ConsoleColor.println("Choose the book you want to order:", ConsoleColor.WHITE);
        ConsoleColor.println("1. Search by title", ConsoleColor.BLUE);
        ConsoleColor.println("6. Back to Main Menu", ConsoleColor.BLUE);
        ConsoleColor.print("Enter your choice: ", ConsoleColor.PURPLE);
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice == 1) {
            book = getBook();
            ConsoleColor.print("Enter the quantity you want to order: ", ConsoleColor.PURPLE);
            numOfBooks = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        } else if (choice == 6) {
            ConsoleColor.println("Returning to Main Menu...\n", ConsoleColor.GREEN);
            MenuController.runMenu();
            return;
        } else {
            ConsoleColor.println("Invalid choice. Please try again.", ConsoleColor.RED);
            return;
        }

        double bookPrice = book.getPrice();
        double total = bookPrice * numOfBooks;

        Order order = getOrder();
        ConsoleColor.println("Total price of the order: " + total + " USD", ConsoleColor.YELLOW);

        int itemOrderId = getItemOrderId() + 1;
        ItemOrder itemOrder = new ItemOrder(book, order, numOfBooks, total);
        itemOrder.setItemOrderId(itemOrderId);
        itemOrderService.saveItemOrder(itemOrder);

        ConsoleColor.println("Item Order created successfully! with ID: " + itemOrderId, ConsoleColor.GREEN);
        printItemOrder(itemOrder);
        FileLogger.logApp("ItemOrderController - Item Order created: " + itemOrder);

        ConsoleColor.println("Would you like to create another order? (yes/no)", ConsoleColor.BLUE);
        String response = scanner.nextLine();
        if (response.equalsIgnoreCase("yes")) {
            createItemOrder();
        } else {
            ConsoleColor.println("Returning to Main Menu...\n", ConsoleColor.GREEN);
            MenuController.runMenu();
        }
    }

    private void printItemOrder(ItemOrder itemOrder) {
        ConsoleColor.println("\n===== Item Order Details =====", ConsoleColor.CYAN);
        ConsoleColor.println("Item Order ID: " + itemOrder.getItemOrderId(), ConsoleColor.WHITE);
        ConsoleColor.println("Order ID: " + itemOrder.getOrder().getOrderId(), ConsoleColor.WHITE);
        ConsoleColor.println("Customer ID: " + itemOrder.getOrder().getCustomer().getCustomerId() + "  Name: " + itemOrder.getOrder().getCustomer().getFirstName() + " " + itemOrder.getOrder().getCustomer().getLastName(), ConsoleColor.WHITE);
        ConsoleColor.println("Book: " + itemOrder.getBook().getBookTitle(), ConsoleColor.WHITE);
        ConsoleColor.println("Quantity: " + itemOrder.getQuantity(), ConsoleColor.WHITE);
        ConsoleColor.println("Unit Price: " + itemOrder.getBook().getPrice(), ConsoleColor.WHITE);
        ConsoleColor.println("Total Price: " + (itemOrder.getTotal()), ConsoleColor.WHITE);
        ConsoleColor.println("Order Date: " + itemOrder.getOrder().getOrderDate(), ConsoleColor.WHITE);
        ConsoleColor.println("Order Status: " + itemOrder.getOrder().getStatus(), ConsoleColor.WHITE);
        ConsoleColor.println("==================================\n", ConsoleColor.CYAN);
    }

    private Book getBook() {
        ConsoleColor.println("\n===== Select Books ======", ConsoleColor.CYAN);

        Book book;
        ConsoleColor.print("Enter keyword to search for books: ", ConsoleColor.PURPLE);
        String keyword = scanner.nextLine();
        List<Book> books = bookService.searchBooksByKeyword(keyword);
        if (books.isEmpty()) {
            ConsoleColor.println("No books found. Please try again.", ConsoleColor.RED);
            return getBook();
        } else {
            ConsoleColor.println("Books found:", ConsoleColor.WHITE);
            for (Book bookItem : books) {
                ConsoleColor.println(bookItem.toString(), ConsoleColor.WHITE);
            }
        }
        ConsoleColor.print("Enter the ID of the book you want to order: ", ConsoleColor.PURPLE);
        int bookId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        book = bookService.findBookById(bookId);
        if (book == null) {
            ConsoleColor.println("Book not found. Please try again.", ConsoleColor.RED);
            return getBook();
        }
        ConsoleColor.println("Book selected: " + book, ConsoleColor.GREEN);
        return book;
    }

    public Order getOrder() {
        FileLogger.logApp("ItemOrderController - getOrder");
        Order order = orderController.createNewOrder();
        ConsoleColor.println("Order created successfully!", ConsoleColor.GREEN);
        return order;
    }

    private void updateItemOrder() {
        ConsoleColor.println("\n===== Update Item Order =====", ConsoleColor.CYAN);
        ConsoleColor.print("Enter Item Order ID to update or Show Orders (1.Find by Id - 2.Show all ", ConsoleColor.BLUE);
        ConsoleColor.print("Enter your choice: ", ConsoleColor.PURPLE);
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        if (choice == 1) {
            findItemOrderById();
        } else if (choice == 2) {
            listAllItemOrders();
        } else {
            ConsoleColor.println("Invalid choice. Please try again.", ConsoleColor.RED);
            return;
        }
        ConsoleColor.print("Enter Item Order ID: ", ConsoleColor.PURPLE);
        int itemOrderId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        ConsoleColor.print("Enter new quantity: ", ConsoleColor.PURPLE);
        ItemOrder itemOrder = itemOrderService.findItemOrderById(itemOrderId);
        if (itemOrder == null) {
            ConsoleColor.println("Item Order not found.", ConsoleColor.RED);
            return;
        }

        oldItemOrderDetails(itemOrderId);

        ConsoleColor.println("Let's update this Order", ConsoleColor.CYAN);
        ConsoleColor.print("Enter new quantity: ", ConsoleColor.PURPLE);
        int newQuantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        itemOrder.setQuantity(newQuantity);

        double newTotal = itemOrder.getBook().getPrice() * newQuantity;
        itemOrder.setTotal(newTotal);
        itemOrderService.updateItemOrder(itemOrder);
        ConsoleColor.println("Item Order updated successfully!", ConsoleColor.GREEN);

        printItemOrder(itemOrder);

        ConsoleColor.println("Would you like to update another order? (yes/no)", ConsoleColor.BLUE);
        String response = scanner.nextLine();
        if (response.equalsIgnoreCase("yes")) {
            updateItemOrder();
        } else {
            ConsoleColor.println("Returning to Main Menu...\n", ConsoleColor.GREEN);
            MenuController.runMenu();
        }


    }

    private void oldItemOrderDetails(int id) {
        ItemOrder itemOrder = itemOrderService.findItemOrderById(id);
        if (itemOrder != null) {
            ConsoleColor.println("Item Order Details:", ConsoleColor.CYAN);
            ConsoleColor.println("Item Order ID: " + itemOrder.getItemOrderId(), ConsoleColor.WHITE);
            ConsoleColor.println("Order ID: " + itemOrder.getOrder().getOrderId(), ConsoleColor.WHITE);
            ConsoleColor.println("Customer ID: " + itemOrder.getOrder().getCustomer().getCustomerId() + "  Name: " + itemOrder.getOrder().getCustomer().getFirstName() + " " + itemOrder.getOrder().getCustomer().getLastName(), ConsoleColor.WHITE);
            ConsoleColor.println("Book: " + itemOrder.getBook().getBookTitle(), ConsoleColor.WHITE);
            ConsoleColor.println("Quantity: " + itemOrder.getQuantity(), ConsoleColor.WHITE);
            ConsoleColor.println("Unit Price: " + itemOrder.getBook().getPrice(), ConsoleColor.WHITE);
            ConsoleColor.println("Total Price: " + (itemOrder.getTotal()), ConsoleColor.WHITE);
            ConsoleColor.println("Order Date: " + itemOrder.getOrder().getOrderDate(), ConsoleColor.WHITE);
            ConsoleColor.println("Order Status: " + itemOrder.getOrder().getStatus(), ConsoleColor.WHITE);
            ConsoleColor.println("==================================\n", ConsoleColor.CYAN);
        } else {
            ConsoleColor.println("Item Order not found.", ConsoleColor.RED);
        }
    }

    private void deleteItemOrder() {
        ConsoleColor.println("\n===== Delete Item Order =====", ConsoleColor.CYAN);
        ConsoleColor.print("Enter Item Order ID to delete: ", ConsoleColor.PURPLE);
        int itemOrderId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        ItemOrder itemOrder = itemOrderService.findItemOrderById(itemOrderId);
        if (itemOrder != null) {
            itemOrderService.deleteItemOrder(itemOrderId);
            ConsoleColor.println("Item Order deleted successfully!", ConsoleColor.GREEN);
        } else {
            ConsoleColor.println("Item Order not found.", ConsoleColor.RED);
        }
    }

    private void findItemOrderById() {
        ConsoleColor.println("\n===== Find Item Order by ID =====", ConsoleColor.CYAN);
        ConsoleColor.print("Enter Item Order ID: ", ConsoleColor.PURPLE);
        int itemOrderId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        ItemOrder itemOrder = itemOrderService.findItemOrderById(itemOrderId);
        if (itemOrder != null) {
            ConsoleColor.println("Item Order found:", ConsoleColor.GREEN);
            printItemOrder(itemOrder);
        } else {
            ConsoleColor.println("Item Order not found.", ConsoleColor.RED);
        }

        ConsoleColor.println("Would you like to update or delete this order? (1.Update - 2.Delete - 3.Cancel): ", ConsoleColor.BLUE);
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        if (choice == 1) {
            updateItemOrder();
        } else if (choice == 2) {
            deleteItemOrder();
        } else if (choice == 3) {
            ConsoleColor.println("Returning to Main Menu...\n", ConsoleColor.GREEN);
            MenuController.runMenu();
        }

        else {
            ConsoleColor.println("Invalid choice. Returning to Main Menu...", ConsoleColor.RED);
        }

        ConsoleColor.println("Would you like to search for another order? (yes/no)", ConsoleColor.BLUE);
        String response = scanner.nextLine();
        if (response.equalsIgnoreCase("yes")) {
            findItemOrderById();
        } else {
            ConsoleColor.println("Returning to Main Menu...\n", ConsoleColor.GREEN);
            MenuController.runMenu();
        }
    }

    private void listAllItemOrders() {
        ConsoleColor.println("\n===== List All Item Orders =====", ConsoleColor.CYAN);
        String response;
        List<ItemOrder> itemOrders = itemOrderService.getAllItemOrders();
        if (itemOrders.isEmpty()) {
            ConsoleColor.println("No item orders found.", ConsoleColor.RED);
        } else {
            for (ItemOrder itemOrder : itemOrders) {
                printItemOrder(itemOrder);
            }
        }

        ConsoleColor.println("Would you like to manage a specific order? (yes/no)", ConsoleColor.BLUE);
        scanner.nextLine();
        response = scanner.nextLine();
        if (response.equalsIgnoreCase("yes")) {
            findItemOrderById();
        } else {
            ConsoleColor.println("Returning to Main Menu...\n", ConsoleColor.GREEN);
            MenuController.runMenu();
        }
    }

    private int getItemOrderId() {
        int id = 0;
        try {
            List<ItemOrder> itemOrders = itemOrderService.getAllItemOrders();
            for (int i = 0; i < itemOrders.size(); i++) {
                id = itemOrders.get(i).getItemOrderId();
            }
            return id;

        } catch (Exception e) {
            FileLogger.logError("ItemOrder - Error getting itemOrder: " + e.getMessage());
            ConsoleColor.println("Error retrieving item orders. Please try again.", ConsoleColor.RED);
            return -1;
        }
    }
}