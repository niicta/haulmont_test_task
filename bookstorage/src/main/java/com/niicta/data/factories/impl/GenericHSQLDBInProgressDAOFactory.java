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

public class GenericHSQLDBInProgressDAOFactory implements DAOFactory {
    @Override
    public AuthorDAO createAuthorDAO(ModelFactory modelFactory) {
        return new GenericAuthorHSQLDBInProgressDAO(modelFactory);
    }

    @Override
    public GenreDAO createGenreDAO(ModelFactory modelFactory) {
        return new GenericGenreHSQLDBInProgressDAO(modelFactory);
    }

    @Override
    public BookDAO createBookDAO(ModelFactory booksFactory, DAO<Author> authorDAO, DAO<Genre> genreDAO) {
        return new GenericBookHSQLDBInProgressDAO(booksFactory, authorDAO, genreDAO);
    }
}
