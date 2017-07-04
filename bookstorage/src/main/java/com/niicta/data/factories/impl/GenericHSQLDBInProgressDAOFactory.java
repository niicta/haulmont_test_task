package com.niicta.data.factories.impl;

import com.niicta.data.AuthorDAO;
import com.niicta.data.BookDAO;
import com.niicta.data.DAO;
import com.niicta.data.GenreDAO;
import com.niicta.data.factories.DAOFactory;
import com.niicta.data.hsqldb.inprogress.impl.GenericAuthorHSQLDBInProgressDAO;
import com.niicta.data.hsqldb.inprogress.impl.GenericBookHSQLDBInProgressDAO;
import com.niicta.data.hsqldb.inprogress.impl.GenericGenreHSQLDBInProgressDAO;
import com.niicta.model.Author;
import com.niicta.model.Genre;
import com.niicta.model.factories.ModelFactory;

/**
 * Factory for creating Generic Data Access Objects
 * Creates <code>GenericAuthorHSQLDBInProgressDAO</code>,
 * <code>GenericGenreHSQLDBInProgressDAO</code>,
 * <code>GenericBookHSQLDBInProgressDAO</code>
 */
public class GenericHSQLDBInProgressDAOFactory implements DAOFactory {
    /**
     * provides a DAO for working with <code>Authors</code>
     * @param modelFactory is actual model factory, used in your configuration
     * @return <code>GenericAuthorHSQLDBInProgressDAO</code>, implementing <code>AuthorDAO</code> interface
     */
    @Override
    public AuthorDAO createAuthorDAO(ModelFactory modelFactory) {
        return new GenericAuthorHSQLDBInProgressDAO(modelFactory);
    }

    /**
     * provides a DAO for working with <code>Genres</code>
     * @param modelFactory is actual model factory, used in your configuration
     * @return <code>GenericGenreHSQLDBInProgressDAO</code>, implementing <code>GenreDAO</code> interface
     */
    @Override
    public GenreDAO createGenreDAO(ModelFactory modelFactory) {
        return new GenericGenreHSQLDBInProgressDAO(modelFactory);
    }

    /**
     * provides a DAO for working with <code>Books</code>
     * @param modelFactory is actual model factory, used in your configuration
     * @param authorDAO is actual <code>AuthorDAO</code> used in your configuration
     * @param genreDAO is actual <code>GenreDAO</code> used in your configuration
     * @return <code>GenericBookHSQLDBInProgressDAO</code>, implementing <code>BookDAO</code> interface
     */
    @Override
    public BookDAO createBookDAO(ModelFactory modelFactory, DAO<Author> authorDAO, DAO<Genre> genreDAO) {
        return new GenericBookHSQLDBInProgressDAO(modelFactory, authorDAO, genreDAO);
    }
}
