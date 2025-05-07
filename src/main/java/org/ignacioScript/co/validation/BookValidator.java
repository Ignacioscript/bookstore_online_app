package org.ignacioScript.co.validation;

import java.time.LocalDate;

public class BookValidator extends Validator{

    public static void validateIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be empty");
        }
        if (!isbn.matches("^[A-Za-z0-9\\s:,'-]+$")) {
            throw new IllegalArgumentException("Invalid ISBN format");
        }
        // Optional: Add checksum validation for ISBN-10/13
    }

    public static void validatePublicationDate(LocalDate publicationDate) {
        if (publicationDate == null) {
            throw new IllegalArgumentException("Publication date cannot be null");
        }
        if (publicationDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Publication date cannot be in the future");
        }
        if (publicationDate.getYear() < 1000) {
            throw new IllegalArgumentException("Invalid publication year (must be after year 1000)");
        }
    }



    public static void validatePrice(Double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (price > 10000000) {
            throw new IllegalArgumentException("Price is unrealistically high");
        }
        if (price < 0.99) { // Minimum price
            throw new IllegalArgumentException("Price must be at least $0.99");
        }
    }

    public static void validateStock(Integer stock) {
        if (stock == null || stock <= 0) {
            throw new IllegalArgumentException("Stock must be greater than zero");
        }
        if (stock > 1000) { // Maximum stock
            throw new IllegalArgumentException("Stock exceeds the maximum allowed limit");
        }
    }

    public static void validateTitle(String bookTitle) {
        if (bookTitle == null || bookTitle.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (bookTitle.length() > 100) {
            throw new IllegalArgumentException("Title is too long. Max 100 characters");
        }
        if (!bookTitle.matches("^[A-Za-z0-9\\s:,'-]+$")) {
            throw new IllegalArgumentException("Title contains invalid characters");
        }
    }

    public static void validatePublisher(String publisher) {
        if (publisher == null || publisher.trim().isEmpty()) {
            throw new IllegalArgumentException("Publisher cannot be empty");
        }
        if (publisher.length() > 100) {
            throw new IllegalArgumentException("Publisher name is too long. Max 100 characters");
        }
        if (!publisher.matches("^[A-Za-z0-9\\s&,'-]+$")) {
            throw new IllegalArgumentException("Publisher name contains invalid characters");
        }
    }



}
