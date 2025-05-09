package org.ignacioScript.co.model;


import org.ignacioScript.co.validation.AuthorBookValidator;

public class AuthorBook {
    private Book book;
    private Author author;

    public AuthorBook(Book book, Author author) {
        this.book = book;
        this.author = author;
    }

    public Book getBook() {
        return book;
    }

    public Author getAuthor() {
        return author;
    }

    public void setBook(Book book) {

        this.book = book;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AuthorBook{");
        sb.append("book=").append(book);
        sb.append(", author=").append(author);
        sb.append('}');
        return sb.toString();
    }
}
