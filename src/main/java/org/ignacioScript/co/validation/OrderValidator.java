package org.ignacioScript.co.validation;

import org.ignacioScript.co.model.Customer;

public class OrderValidator {

    public static void validateTotal(Double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Total cannot be negative");
        }
        if (amount > 10000000) {
            throw new IllegalArgumentException("Total is unrealistically high");
        }
        if (amount < 0.99) { // Minimum total
            throw new IllegalArgumentException("Total must be at least $0.99");
        }
    }

    public static void validateCustomer(Customer customer) {
        if (customer == null) {
            throw new NullPointerException("it can be empty");
        }
    }
}
