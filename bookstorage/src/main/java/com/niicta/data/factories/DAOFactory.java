package com.niicta.data.factories;

import com.niicta.data.AuthorDAO;
import com.niicta.data.BookDAO;
import com.niicta.data.DAO;
import com.niicta.data.GenreDAO;
import com.niicta.model.Author;
import com.niicta.model.Genre;
import com.niicta.model.factories.ModelFactory;

public interface DAOFactory {
    AuthorDAO createAuthorDAO(ModelFactory modelFactory);
    GenreDAO createGenreDAO(ModelFactory factory);
    BookDAO createBookDAO(ModelFactory booksFactory, DAO<Author> authorDAO, DAO<Genre> genreDAO);
}
