package com.niicta.view.windows.edit;

import com.niicta.data.DAO;
import com.niicta.model.Genre;
import com.niicta.model.factories.ModelFactory;
import com.niicta.view.Updatable;
import com.vaadin.ui.TextField;

public class EditGenreWindow extends AbstractEditWindow<Genre> {
    private static final String CAPTION = "Editing genre";
    private TextField nameField;

    public EditGenreWindow(com.niicta.data.DAO<Genre> DAO, ModelFactory modelFactory, Updatable root){
        super(DAO, modelFactory, root, CAPTION);
    }

    public EditGenreWindow(DAO<Genre> DAO, Genre targetEntity, Updatable root){
        super(DAO, targetEntity, root, CAPTION);
    }

    @Override
    void initContent() {
        nameField = new TextField("Name: ");
        layout.addComponent(nameField);
    }

    @Override
    void initBinder() {
        binder.forField(nameField)
                .asRequired("Genre's name can't be empty")
                .bind(Genre::getName, Genre::setName);
    }

    @Override
    Genre createEntity() {
        //creating a stub genre for further setting properties
        return modelFactory.createGenre(null);
    }
}
