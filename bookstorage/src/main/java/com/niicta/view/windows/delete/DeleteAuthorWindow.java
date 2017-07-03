package com.niicta.view.windows.delete;

import com.niicta.data.DAO;
import com.niicta.model.Author;
import com.niicta.view.Updatable;
import com.vaadin.ui.Grid;

import java.util.Set;

public class DeleteAuthorWindow extends AbstractDeleteWindow<Author>{

    public DeleteAuthorWindow(Set<Author> items, DAO<Author> dao, Updatable root){
        super(items, dao, root);
    }

    @Override
    void initGrid() {
        grid = new Grid<>();
        grid.addColumn(Author::getId).setCaption("ID");
        grid.addColumn(Author::getAuthorCode).setCaption("Code");
        grid.addColumn(Author::getName).setCaption("Name");
        grid.addColumn(Author::getMiddleName).setCaption("Middle Name");
        grid.addColumn(Author::getLastName).setCaption("Last Name");
        grid.setItems(set);
    }
}
