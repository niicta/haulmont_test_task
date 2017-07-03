package com.niicta.data;

import com.niicta.exceptions.UniqueConstraintException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public interface DAO<T> {
    void save(T entity) throws SQLException, IOException, UniqueConstraintException;
    void update(T entity) throws SQLException, IOException, UniqueConstraintException;
    T findById(long id) throws SQLException, IOException;
    List<T> findAll() throws SQLException, IOException;
    void delete(Collection<T> entities) throws SQLException, IOException;
}
