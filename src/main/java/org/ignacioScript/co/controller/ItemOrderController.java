package org.ignacioScript.co.controller;

import org.ignacioScript.co.service.BookService;
import org.ignacioScript.co.service.CustomerService;
import org.ignacioScript.co.service.ItemOrderService;

public class ItemOrderController {

    private final ItemOrderService itemOrderService;
    private final CustomerService customerService;
    private final BookService bookService;

    public ItemOrderController(ItemOrderService itemOrderService, CustomerService customerService, BookService bookService) {
        this.itemOrderService = itemOrderService;
        this.customerService = customerService;
        this.bookService = bookService;
    }

    public ItemOrderController() {
        this.itemOrderService = new ItemOrderService();
        this.customerService = new CustomerService();
        this.bookService = new BookService();
    }
    
}
