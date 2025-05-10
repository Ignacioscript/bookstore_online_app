package org.ignacioScript.co.model;

import org.ignacioScript.co.util.IdGenerator;
import org.ignacioScript.co.validation.AuthorBookValidator;
import org.ignacioScript.co.validation.ItemOrderValidator;

public class ItemOrder {

    private int itemOrderId;
    private Book book;
    private Order order;
    private int quantity;
    private double total;

    public ItemOrder(Book book, Order order, int quantity, double total) {

        this.itemOrderId = getItemOrderId();
        setBook(book);
        setOrder(order);
        setQuantity(quantity);
        setTotal(total);
    }

    public ItemOrder(int itemOrderId, Book book, Order order, int quantity, double total) {
        this.itemOrderId = itemOrderId;
        this.book = book;
        this.order = order;
        this.quantity = quantity;
        this.total = total;
    }

    public void setItemOrderId(int itemOrderId) {
        this.itemOrderId = itemOrderId;
    }

    public int getItemOrderId() {
        return itemOrderId;
    }

    public Book getBook() {
        return book;
    }

    public Order getOrder() {
        return order;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotal() {
        return total;
    }

    public void setBook(Book book) {
        ItemOrderValidator.validateBookNullParameter(book);
        this.book = book;
    }

    public void setOrder(Order order) {
        ItemOrderValidator.validateOrderNullParameter(order);
        this.order = order;
    }

    public void setQuantity(int quantity) {
        ItemOrderValidator.validateUnits(quantity, 1, 100);

        this.quantity = quantity;
    }

    public void setTotal(double total) {
        ItemOrderValidator.validateUnits(total, 1.0, 9999999);
        this.total = total;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ItemOrder{");
        sb.append("itemOrderId=").append(itemOrderId);
        sb.append(", book=").append(book.getBookId());
        sb.append(", order=").append(order.getOrderId());
        sb.append(", quantity=").append(quantity);
        sb.append(", total=").append(total);
        sb.append('}');
        return sb.toString();
    }

    public String toCsvString() {
        return String.join(",",
                String.valueOf(this.itemOrderId),
                String.valueOf(this.getBook().getBookId()),
                String.valueOf(this.getOrder().getOrderId()),
                String.valueOf(this.getQuantity()),
                String.valueOf(this.getTotal())
                );
    }
}
