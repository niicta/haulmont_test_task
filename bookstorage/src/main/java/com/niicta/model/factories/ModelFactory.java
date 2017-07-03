package com.niicta.model.factories;

import com.niicta.model.Author;
import com.niicta.model.Book;
import com.niicta.model.Genre;

import java.time.LocalDate;
import java.util.Date;

public interface ModelFactory {
    Genre createGenre(String name);
    Author createAuthor(String authorCode, String name, String middleName, String lastName);
    Book createBook(Genre genre, String name, Author author, Book.Publisher publisher, LocalDate year, String city, int count);
}
