package com.niicta.data;

import com.niicta.model.Author;
import com.niicta.model.Book;
import com.niicta.model.Genre;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

public interface BookDAO extends DAO<Book> {
    Collection<Book> findByAuthorID(long id) throws SQLException, IOException;
    Collection<Book> findByGenreID(long id)  throws SQLException, IOException;
    DAO<Author> getAuthorDAO();
    DAO<Genre> getGenreDAO();
}
