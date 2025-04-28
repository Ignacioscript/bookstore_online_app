package org.ignacioScript.co.model;

import org.ignacioScript.co.util.IdGenerator;
import org.ignacioScript.co.validation.OrderValidator;

import java.time.LocalDate;

public class Order {
    private int orderId;
    private Customer customer;
    private LocalDate orderDate;
    private double total;
    private String paymentMethod;
    private Status status;

    public Order(Customer customer, LocalDate orderDate, double total, String paymentMethod, Status status) {
        this.orderId = IdGenerator.generateId();
        this.customer = customer;
        this.orderDate = orderDate;
        setTotal(total);
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public double getTotal() {
        return total;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Status getStatus() {
        return status;
    }


    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public void setTotal(double total) {
        OrderValidator.validateTotal(total);
        this.total = total;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append("orderId=").append(orderId);
        sb.append(", customer=").append(customer);
        sb.append(", orderDate=").append(orderDate);
        sb.append(", total=").append(total);
        sb.append(", paymentMethod='").append(paymentMethod).append('\'');
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }

    public String toCsvString() {
        return String.join(",",
                String.valueOf(orderId),
                String.valueOf(customer.getCustomerId()), // Assuming Customer has a getCustomerId method
                orderDate.toString(),
                String.valueOf(total),
                paymentMethod,
                status.name()
        );
    }
}
