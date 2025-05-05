package org.ignacioScript.co.controller;


import org.ignacioScript.co.model.Book;
import org.ignacioScript.co.model.ItemOrder;
import org.ignacioScript.co.model.Order;
import org.ignacioScript.co.service.BookService;
import org.ignacioScript.co.service.CustomerService;
import org.ignacioScript.co.service.ItemOrderService;
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

    public ItemOrderController( Scanner scanner, CustomerService customerService, BookService bookService) {
        this.customerService = customerService;
        this.bookService = bookService;
        this.orderController = new OrderController();
        this.itemOrderService = new ItemOrderService();
        this.scanner = scanner;
    }

    public void displayItemOrderMenu() {
        System.out.println("Item Order Menu:");
        System.out.println("1. Create Item Order");
        System.out.println("2. Update Item Order");
        System.out.println("3. Delete Item Order");
        System.out.println("4. Find Item Order by ID");
        System.out.println("5. List All Item Orders");
        System.out.println("6. Back to Main Menu");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> createItemOrder();
            case 2 -> updateItemOrder();
            case 3 -> deleteItemOrder();
            case 4 -> findItemOrderById();
            case 5 -> listAllItemOrders();
            case 6 -> {
                System.out.println("Returning to Main Menu... \n");
                MenuController.runMenu();
            }
        }
    }

    private void createItemOrder() {
        // Implementation for creating an item order
        System.out.println("Creating Item Order...");
        System.out.println("Enter ID for this order:");
        int orderId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.println("Choose the book you want to order:");
        Book book = getBook();
        Order order = getOrder();
        System.out.println("Enter the quantity of the book:");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        double total = book.getPrice() * quantity;
        System.out.println("total price of the order: " + total + " USD");


        ItemOrder itemOrder = new ItemOrder( book, order, quantity, total);
        itemOrder.setItemOrderId(orderId);
        itemOrderService.saveItemOrder(itemOrder);
        System.out.println("Item Order created successfully!");
        FileLogger.logApp("ItemOrderController - Item Order created: " + itemOrder);

        // Add your logic here
    }


    private Book getBook() {
        // Implementation for getting a book by ID
        System.out.println("Enter keyword to search for books:");
        String keyword = scanner.nextLine();
        List<Book> bookList = bookService.searchBooksByKeyword(keyword);
        if (bookList.isEmpty()) {
            System.out.println("No books found. Please try again.");
            return getBook();
        }
        for (Book book : bookList) {
            if (book.getBookTitle().equalsIgnoreCase(keyword)) {
                System.out.println("Book found: " + book.getBookTitle() + " with ID: " + book.getBookId());
            } else {
                System.out.println("Book not found. Please try again.");
                return getBook();
            }
        }
        System.out.println("Enter the ID of the book you want to order:");
        int bookId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        Book book = bookService.findBookById(bookId);
        if (book == null) {
            System.out.println("Book not found. Please try again.");
            return getBook();
        }
        System.out.println("Book found: " + book);
        return book;
    }

    public Order getOrder() {
        FileLogger.logApp("ItemOrderController - getOrder");
        System.out.println("Creating a new Order...");
       Order order = orderController.createOrder(scanner, customerService, bookService);
        System.out.println("Order created successfully!");
        return  order;
    }

    private void updateItemOrder() {
        System.out.println("Not implemented yet");
    }

    private void deleteItemOrder() {
        System.out.println("Not implemented yet");
    }

    private void findItemOrderById() {
        System.out.println("Not implemented yet");
    }

    private void listAllItemOrders() {
        System.out.println("Not implemented yet");
    }


}
