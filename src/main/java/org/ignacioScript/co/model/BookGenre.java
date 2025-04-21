package org.ignacioScript.co.model;

public class BookGenre {
    private Book book;
    private Genre genre;

    public BookGenre(Book book, Genre genre) {
        this.book = book;
        this.genre = genre;
    }

    public Book getBook() {
        return book;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BookGenre{");
        sb.append("book=").append(book);
        sb.append(", genre=").append(genre);
        sb.append('}');
        return sb.toString();
    }
}
