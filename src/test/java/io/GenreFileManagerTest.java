package io;

import org.ignacioScript.co.io.GenreFileManager;
import org.ignacioScript.co.model.Genre;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GenreFileManagerTest {

    private static final String filePath = "src/test/resources/genre_test_file.txt";
    private GenreFileManager genreFileManagerObject;


    @BeforeEach
    void setGenreFileManagerObject() {
        genreFileManagerObject = new GenreFileManager(filePath);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Path.of(filePath));
    }

    @Test
    @DisplayName("Test saving and loading genres")
    void testSaveAndLoadGenres() throws IOException {
        // Arrange
        List<Genre> genresToSave = Arrays.asList(
                new Genre("Fiction", "Sci-Fi and sobrenatural adventures at least 50 characters to be valid or will throw a exception "),
                new Genre("Terror", "Terror and sobrenatural adventures at least 50 characters to be valid or will throw a exception ")
        );

        //Act
        genreFileManagerObject.save(genresToSave);
        List<Genre> loadGenres = genreFileManagerObject.load();

        //Assert
        assertEquals(genresToSave.size(), loadGenres.size(), "The number of genres should match");
        for (int i = 0; i < genresToSave.size(); i ++) {
            assertEquals(genresToSave.get(i).getGenreName(), loadGenres.get(i).getGenreName());
            assertEquals(genresToSave.get(i).getGenreDescription(), loadGenres.get(i).getGenreDescription());
        }
    }


    @Test
    @DisplayName("Test handling of empty genre list")
    void testSaveAndLoadEmptyGenre() throws IOException {
        //Arrange
        List<Genre> emptyGenres = Collections.emptyList();

        //Act
        genreFileManagerObject.save(emptyGenres);
        List<Genre> loadedGenres = genreFileManagerObject.load();

        //Assert
        assertTrue(loadedGenres.isEmpty(), "loaded genres should be empty");
    }
}
