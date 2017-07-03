package com.niicta.data.hsqldb.inprogress.impl;

import com.niicta.data.AuthorDAO;
import com.niicta.data.hsqldb.inprogress.AbstractHSQLDBInProgressDAO;
import com.niicta.exceptions.UniqueConstraintException;
import com.niicta.model.Author;
import com.niicta.model.factories.ModelFactory;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GenericAuthorHSQLDBInProgressDAO extends AbstractHSQLDBInProgressDAO<Author> implements AuthorDAO {
    private static final String AUTHOR_TABLE_NAME = "AUTHOR";
    private static final String CHECK_SCRIPT = "sql/tables_check.sql";
    private static final String CREATE_AUTHOR_TABLE_SCRIPT = "sql/create_authors.sql";
    private static final Logger log = Logger.getLogger(GenericAuthorHSQLDBInProgressDAO.class);
    private boolean authorsTableExists;
    private ModelFactory authorsFactory;

    public GenericAuthorHSQLDBInProgressDAO(ModelFactory authorsFactory){
        this.authorsFactory = authorsFactory;
    }

    /**
     * Inserts new author to DB
     * @param entity is inserting author.
     * If entity is null, than nothing will inserted
     * @throws SQLException if some problems with DB
     * @throws IOException if scripts files not found
     * @throws UniqueConstraintException in case of author with defined code already existing
     */
    @Override
    public synchronized void save(Author entity) throws SQLException, IOException, UniqueConstraintException {
        log.debug("saving author " + entity);
        if (entity == null) {
            log.debug("author is null, nothing to save");
            return;
        }
        try {
            checkExistance();
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM AUTHOR " +
                    "WHERE AUTHOR_CODE = ?");
            statement.setString(1, entity.getAuthorCode());
            ResultSet set = statement.executeQuery();
            if (set.next())throw new UniqueConstraintException("Author with code " + entity.getAuthorCode() + " already existing");
            statement = connection.prepareStatement("INSERT INTO AUTHOR VALUES( " +
                    "NEXT VALUE FOR ID_SEQ," + //getting id from sequence
                    "?," + //Code
                    "?," + //Name
                    "?," + //Middle Name
                    "?" + //Last Name
                    " )");
            statement.setString(1, entity.getAuthorCode());
            statement.setString(2, entity.getName());
            statement.setString(3, entity.getMiddleName());
            statement.setString(4, entity.getLastName());
            statement.executeUpdate();
            statement.close();
            connection.close();
            log.debug("success");
        }
        catch (SQLException | IOException | UniqueConstraintException e) {
            log.debug("failed");
            throw e;
        } catch (RuntimeException e) {
            log.debug("failed");
            log.debug(e);
            throw e;
        }
    }

    /**
     * Updates author in DB
     * @param entity is updating author.
     * If entity is null, than nothing will inserted
     * @throws SQLException if some problems with DB
     * @throws IOException if scripts files not found
     * @throws UniqueConstraintException in case of author with defined code already existing
     */
    @Override
    public synchronized void update(Author entity) throws SQLException, IOException, UniqueConstraintException {
        log.debug("updating author " + entity);
        if (entity == null){
            log.debug("author is null, nothing to update");
            return;
        }
        try{
            checkExistance();
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM AUTHOR " +
                    "WHERE AUTHOR_CODE = ?");
            statement.setString(1, entity.getAuthorCode());
            ResultSet set = statement.executeQuery();
            if (set.next() && set.getLong("AUTHOR_ID") != entity.getId()){
                throw new UniqueConstraintException("Author with code " + entity.getAuthorCode() + " already existing");
            }
            statement = connection.prepareStatement("UPDATE AUTHOR SET " +
                    "AUTHOR_CODE = ?, " +
                    "NAME = ?, " +
                    "MIDDLENAME = ?, " +
                    "LASTNAME = ? " +
                    "WHERE AUTHOR_ID = ? ");
            statement.setString(1 ,entity.getAuthorCode());
            statement.setString(2, entity.getName());
            statement.setString(3, entity.getMiddleName());
            statement.setString(4, entity.getLastName());
            statement.setLong(5, entity.getId());
            statement.executeUpdate();
            statement.close();
            connection.close();
            log.debug("success");
        }
        catch (SQLException | IOException | UniqueConstraintException e) {
            log.debug("failed");
            throw e;
        } catch (RuntimeException e) {
            log.debug("failed");
            log.debug(e);
            throw e;
        }
    }

    /**
     * finds author with defined id
     * @param id is author id
     * @return founded author or <code>null</code>
     * if nothing was found
     * @throws SQLException if some problems with DB
     * @throws IOException if scripts files not found
     */
    @Override
    public synchronized Author findById(long id) throws SQLException, IOException {
        log.debug("finding author with id = " + id);
        try{
            checkExistance();
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM AUTHOR " +
                    "WHERE AUTHOR_ID = ?");
            statement.setLong(1, id);
            ResultSet set = statement.executeQuery();
            Author author = null;
            if (set.next()){
                author = createAuthorFromResultSet(set);
            }
            log.debug("find author: " + author);
            set.close();
            statement.close();
            connection.close();
            log.debug("success");
            return author;
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
     * finds author with defined code
     * @param s is author's code
     * @return founded author or <code>null</code>
     * if nothing was found
     * @throws SQLException if some problems with DB
     * @throws IOException if scripts files not found
     */
    @Override
    public synchronized Author findByCode(String s) throws SQLException, IOException {
        log.debug("finding author with code = " + s);
        try{
            checkExistance();
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM AUTHOR " +
                    "WHERE AUTHOR_CODE = ?");
            statement.setString(1, s);
            ResultSet set = statement.executeQuery();
            Author author = null;
            if (set.next()){
                author = createAuthorFromResultSet(set);
            }
            log.debug("find author: " + author);
            set.close();
            statement.close();
            connection.close();
            log.debug("success");
            return author;
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
     * finds all authors
     * @return list of authors which can be empty
     * (if no author founded)
     * @throws SQLException if some problems with DB
     * @throws IOException if scripts files not found
     */
    @Override
    public synchronized List<Author> findAll() throws SQLException, IOException {
        log.debug("finding all authors...");
        try {
            checkExistance();
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM AUTHOR");
            ResultSet set = statement.executeQuery();
            List<Author> list = new ArrayList<>();
            while (set.next()) {
                Author author = createAuthorFromResultSet(set);
                log.debug("find author: " + author);
                list.add(author);
            }
            set.close();
            statement.close();
            connection.close();
            log.debug("find all authors end, found:\n" + list);
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

    /**
     * removes authors from DB
     * @param entities is authors which will removed
     * @throws SQLException if some problems with DB
     * @throws IOException if scripts files not found
     */
    @Override
    public void delete(Collection<Author> entities) throws SQLException, IOException {
        log.debug("removing authors: " + entities);
        try {
            checkExistance();
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM AUTHOR where AUTHOR_ID = ?");
            for (Author author : entities){
                statement.setLong(1, author.getId());
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

    private void createAuthorsTable() throws IOException, SQLException {
        try {
            log.debug("checking if AUTHOR Table exists..");
            if (!(authorsTableExists = dataBaseObjectExists(CHECK_SCRIPT, AUTHOR_TABLE_NAME))) {
                log.debug("AUTHOR Table not found in DB");
                log.debug("creating AUTHOR Table...");
                executeDDLScriptFromFile(CREATE_AUTHOR_TABLE_SCRIPT);
                log.debug("AUTHOR Table created");
                authorsTableExists = true;
            } else {
                log.debug("AUTHOR Table exists");
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
        if (!authorsTableExists) {
            log.debug("authors table not exists");
            createAuthorsTable();
        }
    }

    private Author createAuthorFromResultSet(ResultSet set) throws SQLException {
        long id = set.getLong("AUTHOR_ID");
        String registrationCode = set.getString("AUTHOR_CODE");
        String name = set.getString("NAME");
        String middleName = set.getString("MIDDLENAME");
        String lastName = set.getString("LASTNAME");
        Author author = authorsFactory.createAuthor(registrationCode, name, middleName, lastName);
        author.setId(id);
        return author;
    }
}
