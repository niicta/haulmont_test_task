package com.niicta.view.windows.statistic;

import com.niicta.data.BookDAO;
import com.niicta.data.DAO;
import com.niicta.exceptions.UniqueConstraintException;
import com.niicta.model.Book;
import com.niicta.model.Genre;
import com.vaadin.data.ValueProvider;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;

public class BookByGenreStatisticWindow extends AbstractStaticticWindow<Genre, Book> {

    public BookByGenreStatisticWindow(DAO<Book> dao, Set<Genre> items){
        super(items, dao, "Books by genres");
    }

    @Override
    protected void initGrids() {
        for (Genre genre: items){
            Grid<Book> grid = new Grid<>("Books of genre \"" + genre.getName() + "\"");
            grid.addColumn(Book::getName).setCaption("Name");
            grid.addColumn(new ValueProvider<Book, String>() {
                @Override
                public String apply(Book book) {
                    return book.getAuthor().getAuthorCode();
                }
            }).setCaption("Author");
            grid.addColumn(new ValueProvider<Book, String>() {
                @Override
                public String apply(Book book) {
                    return book.getGenre().getName();
                }
            }).setCaption("Genre");
            Collection<Book> books = null;
            try {
                books = ((BookDAO)dao).findByGenreID(genre.getId());
                grid.setItems(books);

            } catch (SQLException | IOException e) {
                Notification.show("Oops! Looks like we have some problems with database\n"
                        + "please, contact your administrator and try again later");
            }
            Label count = new Label("Total: " + books.size());
            layout.addComponents(grid, count);
            grid.setHeightByRows(3);
            layout.setComponentAlignment(count, Alignment.BOTTOM_RIGHT);
        }
    }
}
