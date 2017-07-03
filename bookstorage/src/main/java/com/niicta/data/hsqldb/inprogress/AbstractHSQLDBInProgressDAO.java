package com.niicta.data.hsqldb.inprogress;


import com.niicta.data.DAO;
import org.apache.log4j.Logger;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public abstract class AbstractHSQLDBInProgressDAO<T> implements DAO<T> {
    private static final String DB_LOGIN = "SA";
    private static final String DB_PASSWORD = "";
    private static final String DB_PATH = "db/";
    private static final String DB_NAME = "book_storage";
    private static final String SEQUENCE_NAME = "ID_SEQ";
    private static final String SEQUENCE_CHECK_SCRIPT = "sql/sequence_check.sql";
    private static final String CREATE_SEQUENCE_SCRIPT = "sql/create_sequence.sql";
    private static final Logger log = Logger.getLogger(AbstractHSQLDBInProgressDAO.class);
    protected boolean idSequenceExists;

    static {
        try {
            log.debug("Initialization for HSQLDB In-Progress started");
            Class.forName("org.hsqldb.jdbcDriver");
            log.debug("success");
        } catch (ClassNotFoundException e) {
            log.debug("failed");
            log.debug(e);
        }
    }

    /**
     * creates SEQUENCE for generating entities id if its not exist
     */
    protected void createIdSequence() throws SQLException, IOException {
        try {
            log.debug("checking if IdSequence exists..");
            if (!(idSequenceExists = dataBaseObjectExists(SEQUENCE_CHECK_SCRIPT, SEQUENCE_NAME))) {
                log.debug("IdSequence not found in DB");
                log.debug("creating IdSequence...");
                executeDDLScriptFromFile(CREATE_SEQUENCE_SCRIPT);
                log.debug("IdSequence created");
                idSequenceExists = true;
            } else {
                log.debug("IdSequence exists");
            }
        }
        catch (SQLException e) {
            idSequenceExists = false;
            log.debug("failed");
            throw e;
        }
        catch (IOException e) {
            idSequenceExists = false;
            log.debug("failed");
            throw e;
        }
        catch (RuntimeException e) {
            idSequenceExists = false;
            log.debug("failed");
            log.debug(e);
            throw e;
        }

    }

    protected boolean dataBaseObjectExists(String checkingScriptFileName, String dataBaseObjectName) throws SQLException, IOException {
        String sql = "";
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            BufferedReader input = new BufferedReader(new FileReader(checkingScriptFileName));
            StringBuffer sb = new StringBuffer("");
            String tmp;
            while ((tmp = input.readLine()) != null) {
                sb.append(tmp);
            }
            sql = sb.toString();
            log.debug("script:\n" + sql);
            ResultSet set = statement.executeQuery(sql);
            while (set.next()){
                if (set.getString(1).equals(dataBaseObjectName)){
                    log.debug("found: " + dataBaseObjectName);
                    return true;
                }
            }
            log.debug("not found: " + dataBaseObjectName);
            connection.close();
            return false;
        } catch (SQLException e) {
            log.debug("can't execute script: " + sql);
            log.debug(e);
            throw e;
        } catch (FileNotFoundException e) {
            log.debug("file not found" + checkingScriptFileName);
            log.debug(e);
            throw e;
        } catch (IOException e) {
            log.debug("failed, IO Exception");
            log.debug(e);
            throw e;
        } catch (RuntimeException e) {
            log.debug("failed, Runtime exception");
            log.debug(e);
            throw e;
        }
    }

    /**
     *@param sqlFileName is script file location
     *@throws RuntimeException which can wrap one of
     * java.io.FileNotFoundException,
     * java.io.IOException,
     * java.sql.SQLException
    */
    protected void executeDDLScriptFromFile(String sqlFileName) throws SQLException, IOException {
        String sql = "";
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            BufferedReader input = new BufferedReader(new FileReader(sqlFileName));
            StringBuffer sb = new StringBuffer("");
            String tmp;
            while ((tmp = input.readLine()) != null) {
                sb.append(tmp);
            }
            sql = sb.toString();
            statement.executeUpdate(sql);
            connection.close();
        } catch (SQLException e) {
            log.debug("can't execute script: " + sql);
            log.debug(e);
            throw e;
        } catch (FileNotFoundException e) {
            log.debug("file not found" + sqlFileName);
            log.debug(e);
            throw e;
        } catch (IOException e) {
            log.debug("failed");
            log.debug(e);
            throw e;
        }
    }

    protected Connection getConnection() throws SQLException {
        log.debug("getConnection for HSQLDB In-Progress started");
        try {
            Connection connection;
//            connection = cpds.getConnection();
            String path = DB_PATH;
            String dbname = DB_NAME;
            String connectionString = "jdbc:hsqldb:file:"+path+dbname;
            String login = DB_LOGIN;
            String password = DB_PASSWORD;
            connection = DriverManager.getConnection(connectionString, login, password);
            log.debug("success");
            return connection;
        } catch (SQLException e) {
            log.debug("failed");
            log.debug(e);
            throw e;
        }
    }
}
