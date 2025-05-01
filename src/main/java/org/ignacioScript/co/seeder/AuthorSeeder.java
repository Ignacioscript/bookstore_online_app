package org.ignacioScript.co.seeder;

import org.ignacioScript.co.model.Author;
import org.ignacioScript.co.service.AuthorService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class AuthorSeeder {

    public static void seedAuthors(AuthorService authorService) {

        List<Author> authors = new ArrayList<>();
        List<Author> authorList = Arrays.asList(
                new Author("Alan", "Harper", "Co-Author of Two and a Half Man book", "US Citizen"),
                new Author("Charlie", "Harper", "Co-Author of Two and a Half Man book", "US Citizen"),
                new Author("Jake", "Peralta", "Author of Brooklyn Nine-Nine Detective Manual", "US Citizen"),
                new Author("Raymond", "Holt", "Co-Author of Brooklyn Nine-Nine Detective Manual", "US Citizen"),
                new Author("Gina", "Linetti", "Contributor to Brooklyn Nine-Nine Detective Manual", "US Citizen"),
                new Author("Winston", "Churchill", "Author of World War II Memoirs", "British"),
                new Author("Virginia", "Woolf", "Pioneer of Modernist Literature", "British"),
                new Author("Mark", "Twain", "American Humorist and Author of Classic Novels", "US Citizen"),
                new Author("Agatha", "Christie", "Stellar Mystery Novelist and Creator of Poirot", "British"),
                new Author("Ernest", "Hemingway", "Nobel Prize-Winning American Author", "US Citizen"),
                new Author("Leo", "Tolstoy", "Author of War and Peace and Anna Karenina", "Russian"),
                new Author("Fyodor", "Dostoevsky", "Russian Novelist Known for Crime and Punishment", "Russian"),
                new Author("Homer", "Simpson", "Co-Author of Springfield's Guide to Donuts", "US Citizen"),
                new Author("JK", "Rowling", "Author of the World-Famous Harry Potter Series", "British"),
                new Author("George", "Orwell", "Author of 1984 and Animal Farm", "British"),
                new Author("Haruki", "Murakami", "Japanese Novelist Known for Magical Realism", "Japanese"),
                new Author("Isaac", "Asimov", "Author of Foundation Series and Sci-Fi Icon", "US Citizen"),
                new Author("Jane", "Austen", "Author of Timeless Romance Novels", "British"),
                new Author("Maya", "Angelou", "Poet and Civil Rights Activist", "US Citizen"),
                new Author("Margaret", "Atwood", "Author of The Handmaid's Tale", "Canadian")
        );
        authors.addAll(authorList);
        authors.sort((a1, a2) -> a1.getLastName().compareToIgnoreCase(a2.getLastName()));

        for (Author author : authors) {
            authorService.saveAuthor(author);
        }

    }
}
