package com.niicta.view.windows.delete;

import com.niicta.data.DAO;
import com.niicta.model.Book;
import com.niicta.view.Updatable;
import com.vaadin.data.ValueProvider;
import com.vaadin.ui.Grid;

import java.util.Set;

public class DeleteBookWindow extends AbstractDeleteWindow<Book> {

    public DeleteBookWindow(Set<Book> items, DAO<Book> dao, Updatable root){
        super(items, dao, root);
    }

    @Override
    void initGrid() {
        grid = new Grid<>();
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
        grid.addColumn(new ValueProvider<Book, String>() {
            @Override
            public String apply(Book book) {
                return String.valueOf(book.getYear().getYear());
            }
        }).setCaption("Year");
        grid.addColumn(Book::getCity).setCaption("City");
        grid.addColumn(Book::getPublisher).setCaption("Publisher");
        //grid.addColumn(Book::getCount).setCaption("Count");
        grid.setItems(set);
    }
}
