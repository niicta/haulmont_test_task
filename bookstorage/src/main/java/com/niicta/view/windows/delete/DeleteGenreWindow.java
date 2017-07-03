package com.niicta.view.windows.delete;

import com.niicta.data.DAO;
import com.niicta.model.Genre;
import com.niicta.view.Updatable;
import com.vaadin.ui.Grid;

import java.util.Set;

public class DeleteGenreWindow extends AbstractDeleteWindow<Genre> {

    public DeleteGenreWindow(Set<Genre> items, DAO<Genre> dao, Updatable root){
        super(items, dao, root);
    }

    @Override
    void initGrid() {
        grid = new Grid<>();
        grid.addColumn(Genre::getId).setCaption("ID");
        grid.addColumn(Genre::getName).setCaption("Name");
        grid.setItems(set);
    }
}
