package org.ignacioScript.co.io;

import org.ignacioScript.co.model.Genre;
import org.ignacioScript.co.util.FileLogger;
import org.ignacioScript.co.validation.FileManagerValidator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GenreFileManager extends FileManager <Genre> {

    public GenreFileManager(String filePath) {
        super(filePath);
        FileLogger.log("Initializing genre: " + filePath );
    }

    @Override
    public void save(List<Genre> genres) {

        FileLogger.log("Starting to save: " + genres.size() + " genres");
        FileManagerValidator.validateExistingFile(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
        for (Genre genre : genres) {
            writer.write(objectToString(genre));
            writer.newLine();
            FileLogger.log("Save genre: " + genre.toString());
        }
            FileLogger.log("Successfully saved all genres");
        }catch (IOException e) {
            FileLogger.log("ERROR saving genres:" + e.getMessage());
            throw new RuntimeException("Save operation failed", e);
        }

    }

    @Override
    public List<Genre> load() throws IOException {
        List<Genre> genres = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                genres.add(stringToObject(line));
            }
        }
        return genres;
    }

    @Override
    protected Genre getById(int id) {
        try {
            for (Genre genre : load()) {
                if (genre.getGenreId() == id) {
                    return genre;
                }
            }

        }catch (IOException e) {
            throw new RuntimeException("Object not found");
        }

        return null;
    }

    @Override
    protected String objectToString(Genre genre) {
        return String.join(",",
                String.valueOf(genre.getGenreId()),
                genre.getGenreName(),
                genre.getGenreDescription()
        );
    }

    @Override
    protected Genre stringToObject(String line) {
        String[] parts = line.split(",");
        return new Genre(
                parts[1],
                parts[2]
        );
    }
}
