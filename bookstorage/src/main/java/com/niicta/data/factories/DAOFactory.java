package com.niicta.data.factories;

import com.niicta.data.AuthorDAO;
import com.niicta.data.BookDAO;
import com.niicta.data.DAO;
import com.niicta.data.GenreDAO;
import com.niicta.model.Author;
import com.niicta.model.Genre;
import com.niicta.model.factories.ModelFactory;

public interface DAOFactory {
    /**
     * provides a DAO for working with <code>Authors</code>
     * @param modelFactory is actual model factory, used in your configuration
     * @return DAO, implementing <code>AuthorDAO</code> interface
     */
    AuthorDAO createAuthorDAO(ModelFactory modelFactory);

    /**
     * provides a DAO for working with <code>Genres</code>
     * @param modelFactory is actual model factory, used in your configuration
     * @return DAO, implementing <code>GenreDAO</code> interface
     */
    GenreDAO createGenreDAO(ModelFactory modelFactory);

    /**
     * provides a DAO for working with <code>Books</code>
     * @param modelFactory is actual model factory, used in your configuration
     * @param authorDAO is actual <code>AuthorDAO</code> used in your configuration
     * @param genreDAO is actual <code>GenreDAO</code> used in your configuration
     * @return DAO, implementing <code>BookDAO</code> interface
     */
    BookDAO createBookDAO(ModelFactory modelFactory, DAO<Author> authorDAO, DAO<Genre> genreDAO);
}
