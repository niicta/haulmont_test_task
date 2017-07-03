package com.niicta.data;

import com.niicta.model.Genre;

import java.io.IOException;
import java.sql.SQLException;

public interface GenreDAO extends DAO<Genre> {
    Genre findByName(String s)  throws SQLException, IOException;
}
