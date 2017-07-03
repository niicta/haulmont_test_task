package com.niicta.data;

import com.niicta.model.Author;

import java.io.IOException;
import java.sql.SQLException;

public interface AuthorDAO extends DAO<Author> {
    Author findByCode(String s) throws SQLException, IOException;
}
