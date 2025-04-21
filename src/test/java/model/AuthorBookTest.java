package model;

import org.ignacioScript.co.model.Author;
import org.ignacioScript.co.model.AuthorBook;
import org.ignacioScript.co.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class AuthorBookTest {

    AuthorBook authorBookTestObject;




    @BeforeEach
    public void setAuthorBookTestObject() {
        Book bookTestObject =  new Book(
                "9783161484100",
                "The Lost City",
                "Uncover ancient secrets in the Amazon",
                "HarperCollins Publishers",
                LocalDate.of(2021, 04, 20),
                12.99,
                20);
        Author authorTestObject = new Author(
                "Emily",
                "Miller",
                "Award-winning author known for her adventurous novels that explore the depths of " +
                        "human courage and resilience. Born and raised in the UK, she draws " +
                        "inspiration from her travels and love for history.",
                "British");
        authorBookTestObject = new AuthorBook(bookTestObject, authorTestObject);
    }


    @Test
    public void shouldReturnAnAuthorBookObject() {
        assertNotNull(authorBookTestObject);
    }

    @Test
    public void shouldRetrieveBookTitle() {
        assertEquals("The Lost City", authorBookTestObject.getBook().getBookTitle());
    }
}
