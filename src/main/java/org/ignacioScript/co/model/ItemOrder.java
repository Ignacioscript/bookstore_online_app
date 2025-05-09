package org.ignacioScript.co.model;

import org.ignacioScript.co.util.IdGenerator;
import org.ignacioScript.co.validation.AuthorBookValidator;
import org.ignacioScript.co.validation.ItemOrderValidator;

public class ItemOrder {

    private int itemOrderId;
    private Book book;
    private Order order;
    private int quantity;
    private double unitPrice;

    public ItemOrder(Book book, Order order, int quantity, double unitPrice) {

        this.itemOrderId = IdGenerator.generateId();
        setBook(book);
        setOrder(order);
        setQuantity(quantity);
        setUnitPrice(unitPrice);
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

    public double getUnitPrice() {
        return unitPrice;
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

    public void setUnitPrice(double unitPrice) {
        ItemOrderValidator.validateUnits(unitPrice, 1.0, 99.99);
        this.unitPrice = unitPrice;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ItemOrder{");
        sb.append("itemOrderId=").append(itemOrderId);
        sb.append(", book=").append(book);
        sb.append(", order=").append(order);
        sb.append(", quantity=").append(quantity);
        sb.append(", unitPrice=").append(unitPrice);
        sb.append('}');
        return sb.toString();
    }
}
