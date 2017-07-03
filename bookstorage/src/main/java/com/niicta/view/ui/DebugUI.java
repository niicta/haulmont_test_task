package com.niicta.view.ui;

import com.niicta.data.DAO;
import com.niicta.data.factories.DAOFactoryProvider;
import com.niicta.model.Author;
import com.niicta.model.Book;
import com.niicta.model.Genre;
import com.niicta.model.factories.ModelFactory;
import com.niicta.model.factories.ModelFactoryProvider;
import com.niicta.view.Updatable;
import com.niicta.view.windows.delete.DeleteBookWindow;
import com.niicta.view.windows.delete.DeleteGenreWindow;
import com.niicta.view.windows.delete.DeleteAuthorWindow;
import com.niicta.view.windows.edit.EditAuthorWindow;
import com.niicta.view.windows.edit.EditBookWindow;
import com.niicta.view.windows.edit.EditGenreWindow;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.ValueProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Theme("mytheme")
@SuppressWarnings("serial")
public class DebugUI extends UI implements Updatable {
    private Grid<Author> authorsGrid = new Grid<>("Authors:");
    private Grid<Genre> genresGrid = new Grid<>("Genres:");
    private Grid<Book> booksGrid = new Grid<>("Books:");
    private VerticalLayout layout = new VerticalLayout();
    private ModelFactory factory = ModelFactoryProvider.getProvider().getModelFactoryByType(ModelFactoryProvider.GENERIC_FACTORY);
    private DAO<Author> authorsDAO;
    private DAO<Genre> genreDAO;
    private DAO<Book> bookDAO;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        layout = new VerticalLayout();
        authorsDAO = DAOFactoryProvider.getProvider().getDefaultDAOFactory().createAuthorDAO(factory);
        genreDAO = DAOFactoryProvider.getProvider().getDefaultDAOFactory().createGenreDAO(factory);
        bookDAO = DAOFactoryProvider.getProvider().getDefaultDAOFactory().createBookDAO(factory, authorsDAO, genreDAO);
        initAuthorsPanel();
        initGenresPanel();
        initBooksPanel();
        layout.setSpacing(true);
        update();
        authorsGrid.getEditor().setEnabled(false);
        genresGrid.getEditor().setEnabled(false);
        setContent(layout);
    }

    @Override
    public void update() {
        List<Author> authors = new ArrayList<>();
        List<Genre> genres = new ArrayList<>();
        List<Book> books = new ArrayList<>();
        try {
            authors = authorsDAO.findAll();
            genres = genreDAO.findAll();
            books = bookDAO.findAll();
        } catch (SQLException | IOException e) {
            Notification.show("Oops! Looks like we have some problems with database\n"
                    + "please, contact your administrator and try again later");
        } catch (RuntimeException e) {
            Notification.show("Unknown error\n"
                    + "please, contact your administrator and try again later");
        }
        authorsGrid.setItems(authors);
        genresGrid.setItems(genres);
        booksGrid.setItems(books);
    }

    private void initAuthorsPanel(){
        Button insertAuthor = new Button("Insert Author");
        Button editAuthor = new Button("Edit");
        Button deleteAuthor = new Button("Delete");
        HorizontalLayout buttons = new HorizontalLayout();
        insertAuthor.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Window createAuthorWindow = new EditAuthorWindow(authorsDAO, factory, DebugUI.this);
                addWindow(createAuthorWindow);
            }
        });
        editAuthor.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Set<Author> selection = authorsGrid.getSelectedItems();
                for (Author author : selection) {
                    Window editAuthorWindow = new EditAuthorWindow(authorsDAO, author, DebugUI.this);
                    addWindow(editAuthorWindow);
                }
            }
        });
        deleteAuthor.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Set<Author> selection = authorsGrid.getSelectedItems();
                if (selection != null && selection.size() > 0)
                    addWindow(new DeleteAuthorWindow(selection, authorsDAO, DebugUI.this));
            }
        });
        authorsGrid.addColumn(Author::getId).setCaption("ID");
        authorsGrid.addColumn(Author::getAuthorCode).setCaption("Code");
        authorsGrid.addColumn(Author::getName).setCaption("Name");
        authorsGrid.addColumn(Author::getMiddleName).setCaption("Middle Name");
        authorsGrid.addColumn(Author::getLastName).setCaption("Last Name");
        authorsGrid.setWidth("100%");
        authorsGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        buttons.addComponents(insertAuthor, editAuthor, deleteAuthor);
        buttons.setSpacing(true);
        layout.addComponents(authorsGrid, buttons);
    }

    private void initGenresPanel() {
        Button insertGenre = new Button("Insert Genre");
        Button editGenre = new Button("Edit");
        Button deleteGenre = new Button("Delete");
        HorizontalLayout buttons = new HorizontalLayout();
        insertGenre.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Window createGenreWindow = new EditGenreWindow(genreDAO, factory, DebugUI.this);
                update();
                addWindow(createGenreWindow);
            }
        });
        editGenre.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Set<Genre> selection = genresGrid.getSelectedItems();
                for (Genre genre : selection) {
                    Window editGenreWindow = new EditGenreWindow(genreDAO, genre, DebugUI.this);
                    addWindow(editGenreWindow);
                }
            }
        });
        deleteGenre.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Set<Genre> selection = genresGrid.getSelectedItems();
                if (selection != null && selection.size() > 0)
                    addWindow(new DeleteGenreWindow(selection, genreDAO, DebugUI.this));
            }
        });
        genresGrid.addColumn(Genre::getId).setCaption("ID");
        genresGrid.addColumn(Genre::getName).setCaption("Name");
        genresGrid.setWidth("100%");
        genresGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        buttons.addComponents(insertGenre, editGenre, deleteGenre);
        buttons.setSpacing(true);
        layout.addComponents(genresGrid, buttons);
    }

    private void initBooksPanel(){
        Button insertBook = new Button("Insert Book");
        Button editBook = new Button("Edit");
        Button deleteBook = new Button("Delete");
        HorizontalLayout buttons = new HorizontalLayout();
        insertBook.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Window createBookWindow = new EditBookWindow(bookDAO, factory, DebugUI.this);
                addWindow(createBookWindow);
            }
        });
        editBook.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Set<Book> selection = booksGrid.getSelectedItems();
                for (Book book : selection) {
                    Window editBookWindow = new EditBookWindow(bookDAO, book, DebugUI.this);
                    addWindow(editBookWindow);
                }
            }
        });
        deleteBook.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Set<Book> selection = booksGrid.getSelectedItems();
                if (selection != null && selection.size() > 0)
                    addWindow(new DeleteBookWindow(selection, bookDAO, DebugUI.this));
            }
        });
        booksGrid.addColumn(Book::getId).setCaption("ID");
        booksGrid.addColumn(new ValueProvider<Book, String>() {
            @Override
            public String apply(Book book) {
                return book.getAuthor().getAuthorCode();
            }
        }).setCaption("Author");
        booksGrid.addColumn(new ValueProvider<Book, String>() {
            @Override
            public String apply(Book book) {
                return book.getGenre().getName();
            }
        }).setCaption("Genre");
        booksGrid.addColumn(new ValueProvider<Book, String>() {
            @Override
            public String apply(Book book) {
                return String.valueOf(book.getYear().getYear());
            }
        }).setCaption("Year");
        booksGrid.addColumn(Book::getCity).setCaption("City");
        booksGrid.addColumn(Book::getPublisher).setCaption("Publisher");
        //booksGrid.addColumn(Book::getCount).setCaption("Count");
        booksGrid.setWidth("100%");
        booksGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        buttons.addComponents(insertBook, editBook, deleteBook);
        buttons.setSpacing(true);
        layout.addComponents(booksGrid, buttons);
    }



    @WebServlet(urlPatterns = "/debug/*", name = "DebugUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = DebugUI.class, productionMode = false)
    public static class DebugUIServlet extends VaadinServlet {
    }
}
