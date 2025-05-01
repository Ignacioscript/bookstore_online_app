package service;

import org.ignacioScript.co.io.AuthorFileManager;
import org.ignacioScript.co.model.Author;
import org.ignacioScript.co.service.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AuthorServiceTest {

    private AuthorService authorServiceTest;
    private AuthorFileManager authorFileManagerTest;
    private String tempfile = "src/test/resources/test_author.csv";
    private List<Author> authors;

    @BeforeEach
    void setUp() {
        authorFileManagerTest = new AuthorFileManager(tempfile);
        authorServiceTest = new AuthorService(authorFileManagerTest);
        authors = Arrays.asList(
                new Author("Alan", "Harper", "Co-Author of Two and a Half Man book", "US Citizen"),
                new Author("Charlie", "Harper", "Co-Author of Two and a Half Man book", "US Citizen"),
                new Author("Jake", "Peralta", "Author of Brooklyn Nine-Nine Detective Manual", "US Citizen"),
                new Author("Raymond", "Holt", "Co-Author of Brooklyn Nine-Nine Detective Manual", "US Citizen")


        );

        authorFileManagerTest.save(authors);

    }

    @Test
    void checkIfCustomerServiceItsNull() {
        assertNotNull(authorServiceTest);
    }


    @Test
    @DisplayName("Test appending a new author and retrieve it")
    void testSavingNewCustomer() throws IOException {
        Author authorToAppend = new Author("Gina", "Linetti", "Contributor to Brooklyn Nine-Nine Detective Manual", "US Citizen");
        authorServiceTest.saveAuthor(authorToAppend);

        List<Author> loadAuthors = authorFileManagerTest.load();
        assertEquals(5, loadAuthors.size(), "The total id authors should now be 5");
        assertEquals("Gina", loadAuthors.get(4 ).getFirstName());
    }

    //TODO finish tests

    @Test
    @DisplayName("Test updating an existing author")
    void testUpdateAuthor() throws IOException{
        //Arrange: Create an updated Author
        Author authorUpdated = new Author("Elan", "Harper", "Co-Author of Two and a Half Man book", "US Citizen");
        authorUpdated.setAuthorId(1);

        //Act: update the customer
        authorServiceTest.updateAuthor(authorUpdated);

        //Assert: verify the update is reflected in the file
        Author loadedAuthor = authorServiceTest.findAuthorById(1);
        assertEquals("Elan", loadedAuthor.getFirstName(), "The first name should match");
    }

    @Test
    @DisplayName("Test finding a author by ID")
    void testFindAuthorByID() {
        Author loadedAuthor = authorServiceTest.findAuthorById(1);
        assertEquals("Alan", loadedAuthor.getFirstName(), "first name should match");
    }

    @Test
    @DisplayName("Test retrieving all authors")
    void testRetrievingAllAuthors() throws IOException {
        //Act: retrieve all authors
        List<Author> allAuthors = authorServiceTest.getAllAuthors();

        //Assert: Verify all authors are loaded
        assertNotNull(allAuthors, "The list of authors should not be null.");
        assertEquals(4, allAuthors.size(), "the number of authors should be 5");
     }

    @Test
    @DisplayName("Test deleting an existing author")
    void testDeleteAuthor() throws IOException {
        //Act: find the author to be deleted
        authorServiceTest.deleteAuthor(1);

        //Assert: verify if the author was removed
        List<Author> loadAuthors = authorFileManagerTest.load();
        assertEquals(3, loadAuthors.size(), "Total number should be 3 now" );
        assertThrows(IllegalArgumentException.class, () -> authorServiceTest.findAuthorById(1), "Author with id no longer exist");
    }

    @ParameterizedTest
    @DisplayName("Test finding a author with invalid ID")
    @ValueSource(ints = {999, 0, -1})
    void testFindAuthorWithInvalidID(int id) {
        //Act & Assert: Attempt to find an author with an invalid ID
        assertThrows(IllegalArgumentException.class, () -> authorServiceTest.findAuthorById(id), "Should thrown an exception");
    }



    @Test
    @DisplayName("Test validation failure for null author")
    void testSaveNullAuthor() {
        //Act & Assert: Attempt to save a null author
        assertThrows(IllegalArgumentException.class, () -> authorServiceTest.saveAuthor(null), "Saving a null author should throws an exception");
    }

    @Test
    @DisplayName("Test updting an author that does not exist")
        void testUpdateNonExistentAuthor() {
            //Arrange: Create an author that does not exist in the file
        Author nonExistentAuthor = new Author("Non", "Existent", "Author that not exist in the database so can not be updated", "nonExistLand");
        assertThrows(IllegalArgumentException.class, () -> authorServiceTest.updateAuthor(nonExistentAuthor));
        }

}
