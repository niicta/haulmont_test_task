package com.niicta.data.hsqldb.inprogress.impl;

import com.niicta.data.BookDAO;
import com.niicta.data.DAO;
import com.niicta.data.hsqldb.inprogress.AbstractHSQLDBInProgressDAO;
import com.niicta.exceptions.UniqueConstraintException;
import com.niicta.model.Author;
import com.niicta.model.Book;
import com.niicta.model.Genre;
import com.niicta.model.factories.ModelFactory;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GenericBookHSQLDBInProgressDAO extends AbstractHSQLDBInProgressDAO<Book> implements BookDAO {
    private static final String BOOK_TABLE_NAME = "BOOK";
    private static final String CHECK_SCRIPT = "sql/tables_check.sql";
    private static final String CREATE_BOOK_TABLE_SCRIPT = "sql/create_books.sql";
    private static final Logger log = Logger.getLogger(GenericBookHSQLDBInProgressDAO.class);
    private boolean booksTableExists;
    private ModelFactory booksFactory;
    private DAO<Author> authorDAO;
    private DAO<Genre> genreDAO;

    public GenericBookHSQLDBInProgressDAO(ModelFactory booksFactory, DAO<Author> authorDAO, DAO<Genre> genreDAO){
        this.booksFactory = booksFactory;
        this.authorDAO = authorDAO;
        this.genreDAO = genreDAO;
    }

    /**
     * Inserts new book to DB
     * @param entity is inserting book.
     * If entity is null, than nothing will inserted
     * @throws SQLException if some problems with DB
     * @throws IOException if scripts files not found
     * @throws UniqueConstraintException in case of book with defined code already existing
     */
    @Override
    public synchronized void save(Book entity) throws SQLException, IOException, UniqueConstraintException {
        log.debug("saving book " + entity);
        if (entity == null) {
            log.debug("book is null, nothing to save");
            return;
        }
        try {
            checkExistance();
            Connection connection = getConnection();
            PreparedStatement statement;
            statement = connection.prepareStatement("INSERT INTO BOOK VALUES( " +
                    "NEXT VALUE FOR ID_SEQ," + //getting id from sequence
                    "?," + //AUTHOR_ID
                    "?," + //GENRE_ID
                    "?," + //NAME
                    "?," + //PUBLISH_YEAR
                    "?," + //CITY
                    "?" + //PUBLISHER
                    //"?" + //BOOK_COUNT Лол, она просто не дает его вставлять, пробовал и int - все равно в базе всегда оказвается ноль
                    " )");
            statement.setLong(1, entity.getAuthor().getId());
            statement.setLong(2, entity.getGenre().getId());
            statement.setString(3, entity.getName());
            statement.setDate(4, java.sql.Date.valueOf(entity.getYear()));
            statement.setString(5, entity.getCity());
            statement.setString(6, entity.getPublisher().getValue());
            //statement.setLong(7, entity.getCount());
            statement.executeUpdate();
            statement.close();
            connection.close();
            log.debug("success");
        }
        catch (SQLException | IOException e) {
            log.debug("failed");
            log.debug(e);
            throw e;
        } catch (RuntimeException e) {
            log.debug("failed");
            log.debug(e);
            throw e;
        }
    }

    /**
     * Updates book in DB
     * @param entity is updating book.
     * If entity is null, than nothing will inserted
     * @throws SQLException if some problems with DB
     * @throws IOException if scripts files not found
     */
    @Override
    public synchronized void update(Book entity) throws SQLException, IOException, UniqueConstraintException {
        log.debug("updating book " + entity);
        if (entity == null){
            log.debug("book is null, nothing to update");
            return;
        }
        try{
            long count = entity.getCount();
            log.debug(count);
            checkExistance();
            Connection connection = getConnection();
            PreparedStatement statement;
            statement = connection.prepareStatement("UPDATE BOOK SET " +
                    "\n BOOK_ID = ?, " +
                    "\n AUTHOR_ID = ?," +
                    "\n GENRE_ID = ?," +
                    "\n NAME = ?," +
                    "\n PUBLISH_YEAR = ?," +
                    "\n CITY = ?," +
                    "\n PUBLISHER = ?" +
                    //"\n BOOK_COUNT = ?"+
                    "\n WHERE BOOK_ID = ? ");
            statement.setLong(1 ,entity.getId());
            statement.setLong(2, entity.getAuthor().getId());
            statement.setLong(3, entity.getGenre().getId());
            statement.setString(4, entity.getName());
            statement.setDate(5, java.sql.Date.valueOf(entity.getYear()));
            statement.setString(6, entity.getCity());
            statement.setString(7, entity.getPublisher().getValue());
            //statement.setLong(8, count);
            statement.setLong(8, entity.getId());
            statement.executeUpdate();
            statement.close();
            connection.close();
            log.debug("success");
        }
        catch (SQLException | IOException e) {
            log.debug("failed");
            log.debug(e);
            throw e;
        } catch (RuntimeException e) {
            log.debug("failed");
            log.debug(e);
            throw e;
        }
    }

    /**
     * finds book with defined id
     * @param id is book id
     * @return founded book or <code>null</code>
     * if nothing was found
     * @throws SQLException if some problems with DB
     * @throws IOException if scripts files not found
     */
    @Override
    public synchronized Book findById(long id) throws SQLException, IOException {
        log.debug("finding book with id = " + id);
        try{
            checkExistance();
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM BOOK " +
                    "WHERE BOOK_ID = ?");
            statement.setLong(1, id);
            ResultSet set = statement.executeQuery();
            Book book = null;
            connection.close();
            if (set.next()){
                book = createBookFromResultSet(set);
            }
            set.close();
            statement.close();
            log.debug("find book: " + book);
            log.debug("success");
            return book;
        }
        catch (SQLException | IOException e) {
            log.debug("failed");
            throw e;
        } catch (RuntimeException e) {
            log.debug("failed");
            log.debug(e);
            throw e;
        }
    }

    /**
     * finds books with defined author id
     * @param id is author's id
     * @return collection of founded book or <code>null</code>
     * if nothing was found
     * @throws SQLException if some problems with DB
     * @throws IOException if scripts files not found
     */
    @Override
    public synchronized Collection<Book> findByAuthorID(long id) throws SQLException, IOException {
        log.debug("finding books with author's id = " + id);
        try{
            checkExistance();
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM BOOK " +
                    "WHERE AUTHOR_ID = ?");
            statement.setLong(1, id);
            ResultSet set = statement.executeQuery();
            Collection<Book> books = new ArrayList<>();
            connection.close();
            if (set.next()){
                Book b = createBookFromResultSet(set);
                log.debug("find book: " + b);
                books.add(b);
            }
            log.debug("find books: " + books);
            set.close();
            statement.close();
            log.debug("success");
            return books;
        }
        catch (SQLException | IOException e) {
            log.debug("failed");
            throw e;
        } catch (RuntimeException e) {
            log.debug("failed");
            log.debug(e);
            throw e;
        }
    }

    /**
     * finds books with defined genre id
     * @param id is genre id
     * @return collection of founded book or <code>null</code>
     * if nothing was found
     * @throws SQLException if some problems with DB
     * @throws IOException if scripts files not found
     */
    @Override
    public synchronized Collection<Book> findByGenreID(long id) throws SQLException, IOException {
        log.debug("finding books with genre's id = " + id);
        try{
            checkExistance();
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM BOOK " +
                    "WHERE GENRE_ID = ?");
            statement.setLong(1, id);
            ResultSet set = statement.executeQuery();
            Collection<Book> books = new ArrayList<>();
            connection.close();
            if (set.next()){
                Book b = createBookFromResultSet(set);
                log.debug("find book: " + b);
                books.add(b);
            }
            log.debug("find books: " + books);
            set.close();
            statement.close();
            log.debug("success");
            return books;
        }
        catch (SQLException | IOException e) {
            log.debug("failed");
            throw e;
        } catch (RuntimeException e) {
            log.debug("failed");
            log.debug(e);
            throw e;
        }
    }

    /**
     * finds all books
     * @return list of books which can be empty
     * (if no books founded)
     * @throws SQLException if some problems with DB
     * @throws IOException if scripts files not found
     */
    @Override
    public synchronized List<Book> findAll() throws SQLException, IOException {
        log.debug("finding all books...");
        try {
            checkExistance();
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM BOOK");
            ResultSet set = statement.executeQuery();
            List<Book> list = new ArrayList<>();
            while (set.next()) {
                Book book = createBookFromResultSet(set);
                log.debug("find book: " + book);
                list.add(book);
            }
            set.close();
            statement.close();
            connection.close();
            log.debug("find all books end, found:\n" + list);
            return list;
        }
        catch (SQLException | IOException e) {
            log.debug("failed");
            throw e;
        } catch (RuntimeException e) {
            log.debug("failed");
            log.debug(e);
            throw e;
        }
    }

    @Override
    public DAO<Author> getAuthorDAO() {
        return authorDAO;
    }

    @Override
    public DAO<Genre> getGenreDAO() {
        return genreDAO;
    }

    /**
     * removes books from DB
     * @param entities is books which will removed
     * @throws SQLException if some problems with DB
     * @throws IOException if scripts files not found
     */
    @Override
    public void delete(Collection<Book> entities) throws SQLException, IOException {
        log.debug("removing books: " + entities);
        try {
            checkExistance();
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM BOOK where BOOK_ID = ?");
            for (Book book : entities){
                statement.setLong(1, book.getId());
                statement.executeUpdate();
            }
            statement.close();
            connection.close();
            log.debug("success");
        }
        catch (SQLException | IOException e) {
            log.debug("failed");
            log.debug(e);
            throw e;
        } catch (RuntimeException e) {
            log.debug("failed");
            log.debug(e);
            throw e;
        }
    }

    private void createBooksTable() throws IOException, SQLException {
        try {
            log.debug("checking if AUTHOR Table exists..");
            if (!(booksTableExists = dataBaseObjectExists(CHECK_SCRIPT, BOOK_TABLE_NAME))) {
                log.debug("BOOK Table not found in DB");
                log.debug("creating BOOK Table...");
                executeDDLScriptFromFile(CREATE_BOOK_TABLE_SCRIPT);
                log.debug("BOOK Table created");
                booksTableExists = true;
            } else {
                log.debug("BOOK Table exists");
            }
        }
        catch (SQLException | IOException e) {
            log.debug("failed");
            throw e;
        } catch (RuntimeException e) {
            log.debug("failed");
            log.debug(e);
            throw e;
        }
    }

    private void checkExistance() throws IOException, SQLException {
        if (!idSequenceExists) {
            log.debug("idSequence not exists");
            createIdSequence();
        }
        if (!booksTableExists) {
            log.debug("authors table not exists");
            createBooksTable();
        }
    }

    private Book createBookFromResultSet(ResultSet set) throws SQLException, IOException {
        long id = set.getLong("BOOK_ID");
        long authorId = set.getLong("AUTHOR_ID");
        Author a = authorDAO.findById(authorId);
        long genreId = set.getLong("GENRE_ID");
        Genre g = genreDAO.findById(genreId);
        String name = set.getString("NAME");
        String publisherName = set.getString("PUBLISHER");
        LocalDate publishYear = set.getDate("PUBLISH_YEAR").toLocalDate();
        String city = set.getString("CITY");
        Book.Publisher publisher = null;
        switch (publisherName){
            case("Moskow"):
                publisher = Book.Publisher.MOSKOW; break;
            case("Saint Petersburg"):
                publisher = Book.Publisher.S_PETERSBURG; break;
            case("O\'Relly"):
                publisher = Book.Publisher.O_RELLY; break;
       }
//        int count = set.getInt("BOOK_COUNT");
        Book book = booksFactory.createBook(g, name, a, publisher, publishYear, city, 0);
        book.setId(id);
        return book;
    }
}
