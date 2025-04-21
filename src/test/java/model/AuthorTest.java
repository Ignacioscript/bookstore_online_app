package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.ignacioScript.co.model.Author;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class AuthorTest {

    Author authorTestObject;

    @BeforeEach
    public void setAuthorTestObject() {
        authorTestObject = new Author(
                "Emily",
                "Miller",
                "Award-winning author known for her adventurous novels that explore the depths of " +
                        "human courage and resilience. Born and raised in the UK, she draws " +
                        "inspiration from her travels and love for history.",
                "British");
    }

    @Test
    @DisplayName("Checking if Object Author exist")
    public void shouldCreateAuthorInstance() {
        assertNotNull(authorTestObject);
    }

    @Test
    @DisplayName("Return correct ID")
    public void shouldReturnCorrectID() {
        assertEquals(1, authorTestObject.getAuthorId());
    }

    @Test
    @DisplayName("Return correct author name")
    public void shouldReturnCorrectName() {
        String nameTestObject = authorTestObject.getFirstName() + " "+  authorTestObject.getLastName();
        assertEquals("Emily Miller", nameTestObject);
    }

    @Test
    @DisplayName("Return correct author bio")
    public void shouldReturnCorrectBio() {
        String bio = "Award-winning author known for her adventurous novels that explore the depths of " +
                "human courage and resilience. Born and raised in the UK, she draws " +
                "inspiration from her travels and love for history.";
        assertEquals(bio, authorTestObject.getBio());
    }

    @Test
    @DisplayName("Return correct nationality")
    public void shouldReturnCorrectNationality() {
        assertEquals("British", authorTestObject.getNationality());
    }


    //Validations
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "1234", ",-%$"})
    @DisplayName("Return an Exception with invalid input")
    public void shouldReturnExceptionWithInvalidName(String invalidInput) {

        assertThrows(IllegalArgumentException.class, () -> authorTestObject.setFirstName(invalidInput));
        assertThrows(IllegalArgumentException.class, () -> authorTestObject.setLastName(invalidInput));
        assertThrows(IllegalArgumentException.class, () -> authorTestObject.setNationality(invalidInput));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @DisplayName("Return an Exception with invalid or offensive words on bio")
    public void shouldReturnExceptionWithInvalidBio(String invalidBio) {
        String longText = "hello".repeat(300);
        assertThrows(IllegalArgumentException.class, () -> authorTestObject.setBio(invalidBio));
        assertThrows(IllegalArgumentException.class, () -> authorTestObject.setBio(longText));

    }



}
