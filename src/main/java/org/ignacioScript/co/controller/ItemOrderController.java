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
        System.out.println("\n===== Order Menu: ======");
        System.out.println("1. Create Order");
        System.out.println("2. Update Order");
        System.out.println("3. Delete Order");
        System.out.println("4. Find Order by ID");
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
        // Implementation for creating an item orders
        System.out.println("\n===== Create new Order =====");
        Book book;
        int numOfBooks;

        System.out.println("Choose the book you want to order:");
        System.out.println("1. Search by title");
        //System.out.println("2. Search by author");
        //System.out.println("3. Search by ISBN");
        //System.out.println("4. Search by genre");
        //System.out.println("5. Search by price");
        System.out.println("6. Back to Main Menu");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice == 1) {
            book = getBook();
            System.out.print("Enter the quantity you want to order: ");
            numOfBooks = scanner.nextInt();
            scanner.nextLine(); // Consume newline

        } else if (choice == 6) {
            System.out.println("Returning to Main Menu... \n");
            MenuController.runMenu();
            return;
        } else {
            System.out.println("Invalid choice. Please try again.");
            return;

        }

        double bookPrice = book.getPrice();

        double total = bookPrice * numOfBooks;



        Order order = getOrder();
        System.out.println("total price of the order: " + total + " USD");


        int itemOrderId = getItemOrderId() + 1;
        ItemOrder itemOrder = new ItemOrder( book, order, numOfBooks, total);
        itemOrder.setItemOrderId(itemOrderId);
        itemOrderService.saveItemOrder(itemOrder);
        System.out.println("Item Order created successfully! with ID: " + itemOrderId);
        printItemOrder(itemOrder);
        FileLogger.logApp("ItemOrderController - Item Order created: " + itemOrder);

        // Add your logic here
    }

    private void printItemOrder(ItemOrder itemOrder) {
        System.out.println("\n===== Item Order Details =====");
        System.out.println("Item Order ID: " + itemOrder.getItemOrderId());
        System.out.println("Order ID: " + itemOrder.getOrder().getOrderId());
        System.out.println("Customer: " + "( ID - " + itemOrder.getOrder().getCustomer().getCustomerId() + ")" + itemOrder.getOrder().getCustomer().getFirstName() + " " + itemOrder.getOrder().getCustomer().getLastName());
        System.out.println("Book: " + itemOrder.getBook().getBookTitle());
        System.out.println("Quantity: " + itemOrder.getQuantity());
        System.out.println("Unit Price: " + itemOrder.getBook().getPrice());
        System.out.println("Total Price: " + (itemOrder.getBook().getPrice() * itemOrder.getQuantity()));
    }


    private Book getBook() {
        // Implementation for getting a book by ID
        System.out.println("\n===== Select Books ======");

        Book book;
        System.out.print("Enter keyword to search for books: ");
        String keyword = scanner.nextLine();
        List<Book> books = bookService.searchBooksByKeyword(keyword);
        if (books.isEmpty()) {
            System.out.println("No books found. Please try again.");
            return getBook();
        } else {
            System.out.println("Books found:");
            for (Book bookItem : books) {
                System.out.println(bookItem);
            }
        }
        System.out.print("Enter the ID of the book you want to order: ");
        int bookId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        book = bookService.findBookById(bookId);
        if (book == null) {
            System.out.println("Book not found. Please try again.");
            return getBook();
        }
        System.out.println("Book selected: " + book);
        return book;
    }

    public Order getOrder() {
        FileLogger.logApp("ItemOrderController - getOrder");
       Order order = orderController.createOrder(scanner);
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

    private int getItemOrderId() {
        int id = 0;
        try {
            List<ItemOrder> itemOrders = itemOrderService.getAllItemOrders();
            for (int i = 0; i < itemOrders.size(); i ++) {
                id = itemOrders.get(i).getItemOrderId();
            }
            return id;

        }catch (Exception e) {
            FileLogger.logError("ItemOrder - Error getting itemOrder: " + e.getMessage());
            return -1;
        }
    }


}
