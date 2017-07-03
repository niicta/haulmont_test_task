package com.niicta.data.hsqldb.inprogress.impl;

import com.niicta.data.GenreDAO;
import com.niicta.data.hsqldb.inprogress.AbstractHSQLDBInProgressDAO;
import com.niicta.exceptions.UniqueConstraintException;
import com.niicta.model.Genre;
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

public class GenericGenreHSQLDBInProgressDAO extends AbstractHSQLDBInProgressDAO<Genre> implements GenreDAO{
    private static final String GENRE_TABLE_NAME = "GENRE";
    private static final String CHECK_SCRIPT = "sql/tables_check.sql";
    private static final String CREATE_GENRE_TABLE_SCRIPT = "sql/create_genres.sql";
    private static final Logger log = Logger.getLogger(GenericGenreHSQLDBInProgressDAO.class);
    private boolean genresTableExists;
    private ModelFactory genresFactory;

    public GenericGenreHSQLDBInProgressDAO(ModelFactory genresFactory){
        this.genresFactory = genresFactory;
    }

    /**
     * Inserts new genre to DB
     * @param entity is inserting genre.
     * If entity is null, than nothing will inserted
     * @throws SQLException if some problems with DB
     * @throws IOException if scripts files not found
     * @throws UniqueConstraintException in case of genre with defined name already existing
     */
    @Override
    public void save(Genre entity) throws SQLException, IOException, UniqueConstraintException {
        log.debug("saving genre " + entity);
        if (entity == null) {
            log.debug("genre is null, nothing to save");
            return;
        }
        try {
            checkExistance();
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM GENRE " +
                    "WHERE GENRE_NAME = ?");
            statement.setString(1, entity.getName());
            ResultSet set = statement.executeQuery();
            if (set.next())throw new UniqueConstraintException("Genre with name " + entity.getName() + " already existing");
            statement = connection.prepareStatement("INSERT INTO GENRE VALUES( " +
                    "NEXT VALUE FOR ID_SEQ," + //getting id from sequence
                    "?" + //Name
                    " )");
            statement.setString(1, entity.getName());
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
     * Updates genre in DB
     * @param entity is updating genre.
     * If entity is null, than nothing will inserted
     * @throws SQLException if some problems with DB
     * @throws IOException if scripts files not found
     * @throws UniqueConstraintException in case of genre with defined name already existing
     */
    @Override
    public void update(Genre entity) throws SQLException, IOException, UniqueConstraintException {
        log.debug("updating genre " + entity);
        if (entity == null){
            log.debug("genre is null, nothing to update");
            return;
        }
        try{
            checkExistance();
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM GENRE " +
                    "WHERE GENRE_NAME = ?");
            statement.setString(1, entity.getName());
            ResultSet set = statement.executeQuery();
            if (set.next() && set.getLong("GENRE_ID") != entity.getId()){
                throw new UniqueConstraintException("Genre with name " + entity.getName() + " already existing");
            }
            statement = connection.prepareStatement("UPDATE GENRE SET " +
                    "GENRE_NAME = ? " +
                    "WHERE GENRE_ID = ? ");
            statement.setString(1 ,entity.getName());
            statement.setLong(2, entity.getId());
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
     * finds genre with defined id
     * @param id is genre id
     * @return founded genre or <code>null</code>
     * if nothing was found
     * @throws SQLException if some problems with DB
     * @throws IOException if scripts files not found
     */
    @Override
    public Genre findById(long id) throws SQLException, IOException {
        log.debug("finding genre with id = " + id);
        try{
            checkExistance();
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM GENRE " +
                    "WHERE GENRE_ID = ?");
            statement.setLong(1, id);
            ResultSet set = statement.executeQuery();
            Genre genre = null;
            if (set.next()){
                genre = createGenreFromResultSet(set);
            }
            log.debug("find genre: " + genre);
            set.close();
            statement.close();
            connection.close();
            log.debug("success");
            return genre;
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
     * finds genre with defined name
     * @param s is genre's name
     * @return founded genre or <code>null</code>
     * if nothing was found
     * @throws SQLException if some problems with DB
     * @throws IOException if scripts files not found
     */
    @Override
    public Genre findByName(String s) throws SQLException, IOException {
        log.debug("finding genre with name = " + s);
        try{
            checkExistance();
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM GENRE " +
                    "WHERE GENRE_NAME = ?");
            statement.setString(1, s);
            ResultSet set = statement.executeQuery();
            Genre genre = null;
            if (set.next()){
                genre = createGenreFromResultSet(set);
            }
            log.debug("find genre: " + genre);
            set.close();
            statement.close();
            connection.close();
            log.debug("success");
            return genre;
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
     * finds all genres
     * @return list of genres which can be empty
     * (if no genre founded)
     * @throws SQLException if some problems with DB
     * @throws IOException if scripts files not found
     */
    @Override
    public List<Genre> findAll() throws SQLException, IOException {
        log.debug("finding all genres...");
        try {
            checkExistance();
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM GENRE");
            ResultSet set = statement.executeQuery();
            List<Genre> list = new ArrayList<>();
            while (set.next()) {
                Genre genre = createGenreFromResultSet(set);
                log.debug("find genre: " + genre);
                list.add(genre);
            }
            set.close();
            statement.close();
            connection.close();
            log.debug("find all genres end, found:\n" + list);
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
     * removes genres from DB
     * @param entities is genres which will removed
     * @throws SQLException if some problems with DB
     * @throws IOException if scripts files not found
     */
    @Override
    public void delete(Collection<Genre> entities) throws SQLException, IOException {
        log.debug("removing genres: " + entities);
        try {
            checkExistance();
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM GENRE where GENRE_ID = ?");
            for (Genre genre : entities){
                statement.setLong(1, genre.getId());
                statement.executeUpdate();
            }
            statement.close();
            connection.close();
            log.debug("success");
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

    private void createGenresTable() throws IOException, SQLException {
        try {
            log.debug("checking if GENRE Table exists..");
            if (!(genresTableExists = dataBaseObjectExists(CHECK_SCRIPT, GENRE_TABLE_NAME))) {
                log.debug("GENRE Table not found in DB");
                log.debug("creating GENRE Table...");
                executeDDLScriptFromFile(CREATE_GENRE_TABLE_SCRIPT);
                log.debug("GENRE Table created");
                genresTableExists = true;
            } else {
                log.debug("GENRE Table exists");
            }
        }
        catch (SQLException | IOException e) {
            idSequenceExists = false;
            log.debug("failed");
            throw e;
        } catch (RuntimeException e) {
            genresTableExists = false;
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
        if (!genresTableExists) {
            log.debug("genres table not exists");
            createGenresTable();
        }
    }

    private Genre createGenreFromResultSet(ResultSet set) throws SQLException {
        long id = set.getLong("GENRE_ID");
        String name = set.getString("GENRE_NAME");
        Genre genre = genresFactory.createGenre(name);
        genre.setId(id);
        return genre;
    }
}
