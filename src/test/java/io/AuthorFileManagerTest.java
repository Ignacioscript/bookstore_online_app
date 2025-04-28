package io;

import org.ignacioScript.co.io.AuthorFileManager;
import org.ignacioScript.co.model.Author;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AuthorFileManagerTest {

    private static final String tempFile = "src/test/resources/test_authors_file.txt";
    private static AuthorFileManager authorFileManagerTestObject;

    @BeforeEach
    void setUp() {
        authorFileManagerTestObject = new AuthorFileManager(tempFile);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Path.of(tempFile));
    }

    @Test
    @DisplayName("test saving and loading authors")
     void testSaveAndLoadAuthors() throws IOException {
        //Arrange
        List<Author> authorsToSave = Arrays.asList(
                new Author("Alan", "Harper", "Co-Author of Two and a Half Man book", "US Citizen"),
                new Author("Charlie", "Harper", "Co-Author of Two and a Half Man book", "US Citizen")
        );

        //Act
        authorFileManagerTestObject.save(authorsToSave);
        List<Author> loadedAuthors = authorFileManagerTestObject.load();

        //Assert
        assertEquals(authorsToSave.size(), loadedAuthors.size(), "The number of authors should match");
        for (int i = 0; i < authorsToSave.size(); i ++) {
            assertEquals(authorsToSave.get(i).getFirstName(), loadedAuthors.get(i).getFirstName(), "First name should match");
            assertEquals(authorsToSave.get(i).getLastName(), loadedAuthors.get(i).getLastName(), "Last name should match");
            assertEquals(authorsToSave.get(i).getBio(), loadedAuthors.get(i).getBio(), "Bio should match");
            assertEquals(authorsToSave.get(i).getNationality(), loadedAuthors.get(i).getNationality(), "Nationality should match");
        }
    }

    @Test
    @DisplayName("Test handling of empty book list")
    void testSaveAndLoadEmtpyAuthors() throws IOException {
        //Arrange
        List<Author> emptyAuthors = Collections.emptyList();

        //Act
        authorFileManagerTestObject.save(emptyAuthors);
        List<Author> loadedAuthors = authorFileManagerTestObject.load();

        //Assert
        assertTrue(loadedAuthors.isEmpty(), "Loaded authors should be empty");
    }


}
