package com.niicta.model.factories;

import com.niicta.model.Author;
import com.niicta.model.Book;
import com.niicta.model.Genre;

import java.time.LocalDate;
import java.util.Date;

public interface ModelFactory {
    /**
     * creates Genre
     * @param name is Genre's name
     * @return objects implements <code>Genre</code> interface
     */
    Genre createGenre(String name);

    /**
     * creates Author
     * @param authorCode is author's code
     * @param name ia author's name
     * @param middleName is author's middle name
     * @param lastName is author.s last name
     * @return object implements <code>Author</code> interface
     */
    Author createAuthor(String authorCode, String name, String middleName, String lastName);

    /**
     * creates Book
     * @param genre is book's genre
     * @param name is book's name
     * @param author is book's author
     * @param publisher is book's publisher
     * @param year is book's publish year
     * @param city is book's publish city
     * @param count is not using now
     * @return object implements <code>Book</code> interface
     */
    Book createBook(Genre genre, String name, Author author, Book.Publisher publisher, LocalDate year, String city, int count);
}
