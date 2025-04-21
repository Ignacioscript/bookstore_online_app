package io;

import org.ignacioScript.co.io.TextFileManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TextFileManagerTest {


    private static final String TEST_FILE = "src/test/resources/test_file.txt";
    private TextFileManager fileManager;

    @BeforeEach
    public void setTestFile() {
        fileManager = new TextFileManager(TEST_FILE);
    }

    @Test
    public void shouldReturnNotNull() {
        assertNotNull(fileManager);
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(Path.of(TEST_FILE));
    }


    @Test
    void writeSingleLineAndReadBack() throws IOException {
        String testLine = "Hello, buffered I/O";
        fileManager.writeLine(testLine);
        List<String> lines = fileManager.readAllLines();

        assertEquals(1, lines.size());
        assertEquals(testLine, lines.get(0));
    }

    @ParameterizedTest
    @ValueSource(strings = {"append line 1", "append line 2", "other line"})
    void shouldThrowExceptionForInvalidLines(String appendLine) throws  IOException {

        fileManager.appendLine(appendLine);
        List<String> lines = fileManager.readAllLines();

       assertDoesNotThrow(() -> lines.get(0));
    }



    @Test
    void appendSingleLinesAndReadBack() throws  IOException {
        fileManager.writeLine("First line");
        fileManager.appendLine("Second line");

        List<String> lines = fileManager.readAllLines();
        assertEquals(2, lines.size());
        assertEquals("Second line", lines.get(1));
    }


    @Test
    void readNonExistentFileThrows() {
        assertThrows(IOException.class, () -> new TextFileManager("nonexistent.txt").readAllLines());
    }

}
