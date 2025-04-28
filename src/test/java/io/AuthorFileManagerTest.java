package io;

import org.ignacioScript.co.io.AuthorFileManager;
import org.ignacioScript.co.model.Author;
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
    @DisplayName("Test saving and loading authors")
    void testSaveAndLoadAuthors() throws IOException {
        List<Author> authorsToSave = Arrays.asList(
                new Author("Alan", "Harper", "Co-Author of Two and a Half Man book", "US Citizen"),
                new Author("Charlie", "Harper", "Co-Author of Two and a Half Man book", "US Citizen")
        );

        authorFileManagerTestObject.save(authorsToSave);
        List<Author> loadedAuthors = authorFileManagerTestObject.load();

        assertEquals(authorsToSave.size(), loadedAuthors.size(), "The number of authors should match");
        for (int i = 0; i < authorsToSave.size(); i++) {
            assertEquals(authorsToSave.get(i).getFirstName(), loadedAuthors.get(i).getFirstName(), "First name should match");
            assertEquals(authorsToSave.get(i).getLastName(), loadedAuthors.get(i).getLastName(), "Last name should match");
            assertEquals(authorsToSave.get(i).getBio(), loadedAuthors.get(i).getBio(), "Bio should match");
            assertEquals(authorsToSave.get(i).getNationality(), loadedAuthors.get(i).getNationality(), "Nationality should match");
        }
    }

    @Test
    @DisplayName("Test handling of empty author list")
    void testSaveAndLoadEmptyAuthors() throws IOException {
        List<Author> emptyAuthors = Collections.emptyList();

        authorFileManagerTestObject.save(emptyAuthors);
        List<Author> loadedAuthors = authorFileManagerTestObject.load();

        assertTrue(loadedAuthors.isEmpty(), "Loaded authors should be empty");
    }

    @Test
    @DisplayName("Test deleting an author by ID")
    void testDeleteAuthor() throws IOException {
        List<Author> authorsToSave = Arrays.asList(
                new Author("Alan", "Harper", "Co-Author of Two and a Half Man book", "US Citizen"),
                new Author("Charlie", "Harper", "Co-Author of Two and a Half Man book", "US Citizen")
        );

        authorFileManagerTestObject.save(authorsToSave);
        Author authorToDelete = authorsToSave.get(0);

        authorFileManagerTestObject.delete(authorToDelete.getAuthorId());
        List<Author> loadedAuthors = authorFileManagerTestObject.load();

        assertEquals(1, loadedAuthors.size(), "One author should remain after deletion");
        assertNotEquals(authorToDelete.getFirstName(), loadedAuthors.get(0).getFirstName(), "Deleted author should not exist");
    }

    @Test
    @DisplayName("Test updating an author")
    void testUpdateAuthor() throws IOException {
        List<Author> authorsToSave = Arrays.asList(
                new Author("Alan", "Harper", "Co-Author of Two and a Half Man book", "US Citizen"),
                new Author("Charlie", "Harper", "Co-Author of Two and a Half Man book", "US Citizen")
        );

        authorFileManagerTestObject.save(authorsToSave);
        Author updatedAuthor = new Author("Alan", "Harper", "Updated Bio Co-Author of Two and a Half Man book", "Updated Nationality");
        updatedAuthor.setAuthorId(authorsToSave.get(0).getAuthorId());

        authorFileManagerTestObject.update(updatedAuthor);
        List<Author> loadedAuthors = authorFileManagerTestObject.load();

        assertEquals(authorsToSave.size(), loadedAuthors.size(), "The number of authors should remain the same");
        assertEquals("Updated Bio Co-Author of Two and a Half Man book", loadedAuthors.get(0).getBio(), "Bio should be updated");
        assertEquals("Updated Nationality", loadedAuthors.get(0).getNationality(), "Nationality should be updated");
    }

    @Test
    @DisplayName("Test getting an author by ID")
    void testGetAuthorById() throws IOException {
        List<Author> authorsToSave = Arrays.asList(
                new Author("Alan", "Harper", "Co-Author of Two and a Half Man book", "US Citizen"),
                new Author("Charlie", "Harper", "Co-Author of Two and a Half Man book", "US Citizen")
        );

        authorFileManagerTestObject.save(authorsToSave);
        Author expectedAuthor = authorsToSave.get(1);

        Author retrievedAuthor = authorFileManagerTestObject.getById(expectedAuthor.getAuthorId());

        assertNotNull(retrievedAuthor, "Retrieved author should not be null");
        assertEquals(expectedAuthor.getFirstName(), retrievedAuthor.getFirstName(), "First name should match");
        assertEquals(expectedAuthor.getLastName(), retrievedAuthor.getLastName(), "Last name should match");
        assertEquals(expectedAuthor.getBio(), retrievedAuthor.getBio(), "Bio should match");
        assertEquals(expectedAuthor.getNationality(), retrievedAuthor.getNationality(), "Nationality should match");
    }
}