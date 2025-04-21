package org.ignacioScript.co.validation;


import org.ignacioScript.co.model.Book;
import org.ignacioScript.co.model.Order;


public class ItemOrderValidator extends Validator{




    public static void validateBookNullParameter(Book book) {
            if (book == null) {
                throw new NullPointerException("No book founded");
            }
    }

    public static void validateOrderNullParameter(Order order) {
            if (order == null) {
                throw new NullPointerException("No order founded");
            }
    }



}
