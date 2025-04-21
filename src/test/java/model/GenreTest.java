package model;

import org.ignacioScript.co.model.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class GenreTest {

    Genre genreTestObject;

    @BeforeEach
    public void setGenreTestObjectUp () {
        genreTestObject = new Genre(
                "Fiction",
                "Fictitious stories with epic, unreal, mystical characters and arguments"
        );
    }

    @Test
    public void shouldReturnAnInstanceOfGenre() {
        assertNotNull(genreTestObject);
    }

    @Test
    public void shouldReturnGenre() {
        assertEquals(1, genreTestObject.getGenreId());
        assertEquals("Fiction", genreTestObject.getGenreName());
        assertEquals("Fictitious stories with epic, unreal, mystical characters and arguments", genreTestObject.getGenreDescription());
    }



    // TEST ID
    @ParameterizedTest
    @DisplayName("Should throw IllegalArgumentException for invalid ID inputs")
    @ValueSource(ints = {0, 10000000, -1})
    public void shouldReturnExceptionForInvalidId(int invalidId) {
        assertThrows(IllegalArgumentException.class, () -> genreTestObject.getGenreId());
    }
    @ParameterizedTest
    @DisplayName("Should not throw exception for valid ID input")
    @ValueSource(ints = {1, 35, 100})
    public void shouldNotReturnExceptionForvalidId(int validId) {
        assertDoesNotThrow( () -> genreTestObject.getGenreId());
    }

    //  TEST Genre Name
    @ParameterizedTest
    @DisplayName("should return Exception for invalid input genre name")
    @NullAndEmptySource
    @ValueSource(strings = {"12234", "-^&%$", " "})
    public void shouldReturnExceptionForInvalidGenre(String invalidGenre) {
        assertThrows(IllegalArgumentException.class, () -> genreTestObject.setGenreName(invalidGenre));
    }
    @ParameterizedTest
    @DisplayName("should Not return Exception for valid input genre name")
    @ValueSource(strings = {"fiction", "Drama", "ThRiller ", "EPIC", "terror", "sci-fi"})
    public void shouldReturnExceptionForValidGenre(String validGenre) {
        assertDoesNotThrow( () -> genreTestObject.setGenreName(validGenre));
    }

    // Test Genre description
    @ParameterizedTest
    @DisplayName("should return an Exception for invalid description input")
    @ValueSource(strings = {"aaa", "1234", "@&*^%7", "too short description"})
    @NullAndEmptySource
    public void shouldReturnExceptionFor(String invalidDescription) {
        assertThrows(IllegalArgumentException.class, () -> genreTestObject.setGenreDescription(invalidDescription));
    }
    @ParameterizedTest
    @DisplayName("should Not return an Exception for valid description input")
    @ValueSource(strings = {"this is a properly size of a description to gives an overall of the genre"})
    public void shouldNotReturnExceptionFor(String validDescription) {
        assertDoesNotThrow(() -> genreTestObject.setGenreDescription(validDescription));
    }


}
