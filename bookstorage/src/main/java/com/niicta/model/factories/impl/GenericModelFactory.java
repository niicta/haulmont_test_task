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

/**
 * Factory for creating Generic Data Access Objects
 * Creates <code>GenericGenre</code>,
 * <code>GenericAuthor</code>,
 * <code>GenericBook</code>
 */
public class GenericModelFactory implements ModelFactory {
    /**
     * creates Genre
     * @param name is Genre's name
     * @return <code>GenericGenre</code>
     */
    @Override
    public Genre createGenre(String name) {
        return new GenericGenre(name);
    }

    /**
     * creates Author
     * @param authorCode is author's code
     * @param name ia author's name
     * @param middleName is author's middle name
     * @param lastName is author.s last name
     * @return <code>GenericAuthor</code>
     */
    @Override
    public Author createAuthor(String authorCode, String name, String middleName, String lastName) {
        return new GenericAuthor(authorCode, name, middleName, lastName);
    }

    /**
     * creates Book
     * @param genre is book's genre
     * @param name is book's name
     * @param author is book's author
     * @param publisher is book's publisher
     * @param year is book's publish year
     * @param city is book's publish city
     * @param count is not using now
     * @return <code>GenericBook</code>
     */
    @Override
    public Book createBook(Genre genre, String name, Author author, Book.Publisher publisher, LocalDate year, String city, int count) {
        return new GenericBook(genre, name, author, publisher, year, city, count);
    }

}
