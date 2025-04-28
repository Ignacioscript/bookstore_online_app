package org.ignacioScript.co.io;

import org.ignacioScript.co.model.Book;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;


public abstract class FileManager <T> {

    protected final String filePath;

    protected FileManager(String filePath) {
        this.filePath = filePath;
    }

    protected abstract void save(List<T> t);

    // Read object using BufferedReader
    protected abstract List<T> load() throws IOException;

    protected abstract T getById(int id); //TODO create this in other classes and review tests

    // Convert   Object to CSV String
    protected abstract String  objectToString(T t);

    protected abstract T stringToObject(String line);

}
