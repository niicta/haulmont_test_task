package com.niicta.view.windows.edit;

import com.niicta.data.DAO;
import com.niicta.model.Author;
import com.niicta.model.factories.ModelFactory;
import com.niicta.view.Updatable;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.ui.TextField;

public class EditAuthorWindow extends AbstractEditWindow<Author> {
    private static final String CAPTION = "Editing author";
    private TextField codeField;
    private TextField nameField;
    private TextField middleNameField;
    private TextField lastNameField;

    public EditAuthorWindow(DAO<Author> DAO, ModelFactory modelFactory, Updatable root){
        super(DAO, modelFactory, root, CAPTION);
    }

    public EditAuthorWindow(DAO<Author> DAO, Author targetEntity, Updatable root){
        super(DAO, targetEntity, root, CAPTION);
    }

    @Override
    void initContent() {
        codeField = new TextField("Code: ");
        nameField = new TextField("Name: ");
        middleNameField = new TextField("Middle Name: ");
        lastNameField = new TextField("Last Name");
        layout.addComponents(codeField, nameField, middleNameField, lastNameField);
    }

    @Override
    void initBinder() {
        binder.forField(codeField)
                .asRequired("Author's code can't be empty")
                .bind(Author::getAuthorCode, Author::setAuthorCode);
        binder.forField(nameField)
                .asRequired("Author's name can't be empty")
                .withValidator(new FieldLettersValidator())
                .bind(Author::getName, Author::setName);
        binder.forField(middleNameField)
                .withValidator(new FieldLettersValidator())
                .bind(Author::getMiddleName, Author::setMiddleName);
        binder.forField(lastNameField)
                .asRequired("Author's middle name can't be empty")
                .withValidator(new FieldLettersValidator())
                .bind(Author::getLastName, Author::setLastName);
    }

    @Override
    Author createEntity() {
        //creating a stub author for further setting properties
        return modelFactory.createAuthor(null, null, null, null);
    }
}
