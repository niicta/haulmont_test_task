package com.niicta.model.factories.impl;

import com.niicta.model.Author;
import com.niicta.model.Book;
import com.niicta.model.Genre;
import com.niicta.model.factories.ModelFactory;
import com.niicta.model.impl.GenericAuthor;
import com.niicta.model.impl.GenericBook;
import com.niicta.model.impl.GenericGenre;

import java.time.LocalDate;
import java.util.Date;

public class GenericModelFactory implements ModelFactory {
    @Override
    public Genre createGenre(String name) {
        return new GenericGenre(name);
    }

    @Override
    public Author createAuthor(String authorCode, String name, String middleName, String lastName) {
        return new GenericAuthor(authorCode, name, middleName, lastName);
    }

    @Override
    public Book createBook(Genre genre, String name, Author author, Book.Publisher publisher, LocalDate year, String city, int count) {
        return new GenericBook(genre, name, author, publisher, year, city, count);
    }

}
