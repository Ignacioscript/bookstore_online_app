package org.ignacioScript.co.model;

import org.ignacioScript.co.util.IdGenerator;
import org.ignacioScript.co.validation.BookValidator;

import java.time.LocalDate;
import java.util.List;

public class Book {



    private int bookId;
    private String isbn;
    private String bookTitle;
    private String description;
    private String publisher;
    private LocalDate publicationDate;
    private Double price;
    private Integer stock;


    public Book(String isbn, String bookTitle, String description, String publisher, LocalDate publicationDate, Double price, Integer stock) {
        this.bookId = getBookId();
        setIsbn(isbn); // Calls the setter, which includes validation
        setBookTitle(bookTitle); // Calls the setter, which includes validation
        setDescription(description); // Calls the setter, which includes validation
        setPublisher(publisher); // Calls the setter, which includes validation
        setPublicationDate(publicationDate); // No validation needed
        setPrice(price); // Calls the setter, which includes validation
        setStock(stock); // Calls the setter, which includes validation
    }



    public Book() {
    }

    public Book(int bookId, String isbn, String bookTitle, String description, String publisher, LocalDate publicationDate, Double price, Integer stock) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.bookTitle = bookTitle;
        this.description = description;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
        this.price = price;
        this.stock = stock;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getBookId() {
        return bookId;
    }


    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        BookValidator.validateIsbn(isbn);
        this.isbn = isbn;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        BookValidator.validateTitle(bookTitle);
        this.bookTitle = bookTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        BookValidator.validateDescription(description, 500, 10);
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        BookValidator.validatePublisher(publisher);
        this.publisher = publisher;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        BookValidator.validatePublicationDate(publicationDate);
        this.publicationDate = publicationDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        BookValidator.validatePrice(price);
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        BookValidator.validateStock(stock);
        this.stock = stock;
    }



    // Validation methods


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Book-> ");
        sb.append("Id= ").append(getBookId());
        sb.append(", isbn='").append(isbn).append('\'');
        sb.append(", bookTitle='").append(bookTitle).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", publisher='").append(publisher).append('\'');
        sb.append(", publicationDate=").append(publicationDate);
        sb.append(", price=").append(price);
        sb.append(", stock=").append(stock);
        sb.append('}');
        return sb.toString();
    }

    public String toCsvString() {
        return String.join(",",
                String.valueOf(getBookId()),
                isbn,
                bookTitle,
                description,
                publisher,
                publicationDate.toString(),
                String.valueOf(price),
                String.valueOf(stock)
        );
    }

}