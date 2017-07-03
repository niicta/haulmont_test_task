package com.niicta.view.windows.edit;

import com.niicta.data.BookDAO;
import com.niicta.data.DAO;
import com.niicta.model.Author;
import com.niicta.model.Book;
import com.niicta.model.Genre;
import com.niicta.model.factories.ModelFactory;
import com.niicta.view.Updatable;
import com.vaadin.shared.ui.datefield.DateResolution;
import com.vaadin.ui.*;

import java.io.IOException;
import java.sql.SQLException;

public class EditBookWindow extends AbstractEditWindow<Book> implements Updatable {
    private static final String CAPTION = "Editing Book";
    private TextField nameField;
    private ComboBox<Author> authorBox;
    private Button newAuthor;
    private Button newGenre;
    private ComboBox<Genre> genreBox;
    private DateField dateField;
    private TextField cityField;
    private TextField countField;
    private ComboBox<Book.Publisher> publisherBox;
    private DAO<Author> authorDAO;
    private DAO<Genre> genreDAO;

    public EditBookWindow(DAO<Book>  bookDAO, ModelFactory modelFactory, Updatable root){
        super(bookDAO, modelFactory, root, CAPTION);
    }

    public EditBookWindow(DAO<Book> bookDAO, Book targetEntity, Updatable root){
        super(bookDAO, targetEntity, root, CAPTION);
    }

    @Override
    public void update() {
        try {
            authorBox.setItems(authorDAO.findAll());
            authorBox.setItemCaptionGenerator(Author::getAuthorCode);
            genreBox.setItems(genreDAO.findAll());
            genreBox.setItemCaptionGenerator(Genre::getName);
        } catch (SQLException | IOException e) {
            Notification.show("Oops! Looks like we have some problems with database\n"
                    + "please, contact your administrator and try again later");
        }
    }

    @Override
    void initContent() {
        this.authorDAO = ((BookDAO)DAO).getAuthorDAO();
        this.genreDAO = ((BookDAO)DAO).getGenreDAO();
        nameField = new TextField("Name: ");
        authorBox = new ComboBox<>("Author: ");
        newAuthor = new Button("New Author", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EditAuthorWindow editAuthorWindow = new EditAuthorWindow(authorDAO, modelFactory, EditBookWindow.this);
                UI.getCurrent().addWindow(editAuthorWindow);
            }
        });
        genreBox = new ComboBox<>("Genre: ");
        newGenre = new Button("New Genre", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EditGenreWindow editGenreWindow = new EditGenreWindow(genreDAO, modelFactory, EditBookWindow.this);
                UI.getCurrent().addWindow(editGenreWindow);
            }
        });
        dateField = new DateField("Publish Year: ");
        cityField = new TextField("City: ");
        //countField = new TextField("Count: ");
        publisherBox = new ComboBox<>("Publisher");
        publisherBox.setItems(Book.Publisher.values());
        publisherBox.setItemCaptionGenerator(Book.Publisher::getValue);
        update();
        dateField.setResolution(DateResolution.YEAR);
        layout.addComponents(nameField, authorBox, newAuthor, genreBox, newGenre, dateField, cityField, publisherBox, dateField);
    }

    @Override
    void initBinder() {
        binder.forField(nameField)
                .asRequired("Book's name can't be empty")
                .bind(Book::getName, Book::setName);
        binder.forField(authorBox)
                .asRequired("Book's author can't be empty")
                .bind(Book::getAuthor, Book::setAuthor);
        binder.forField(genreBox)
                .asRequired("Book's genre can't be empty")
                .bind(Book::getGenre, Book::setGenre);
        binder.forField(dateField)
                .asRequired("Book's publish date can't be empty")
                .bind(Book::getYear, Book::setYear);
        binder.forField(cityField)
                .asRequired("Book's city can't be empty")
                .bind(Book::getCity, Book::setCity);
//        binder.forField(countField)
//                .asRequired("Book's count can't be empty")
//                .withConverter(new StringToLongConverter("Must be a number"))
//                .bind(Book::getCount, Book::setCount);
        binder.forField(publisherBox)
                .asRequired("Book's publisher can't be empty")
                .bind(Book::getPublisher, Book::setPublisher);

    }

    @Override
    Book createEntity() {
        //creating a stub genre for further setting properties
        return modelFactory.createBook(null, null, null, null, null, null, 0);
    }
}
