package com.niicta.view.views;

import com.niicta.data.BookDAO;
import com.niicta.data.DAO;
import com.niicta.model.Author;
import com.niicta.model.Book;
import com.niicta.model.Genre;
import com.niicta.model.factories.ModelFactory;
import com.niicta.view.Updatable;
import com.niicta.view.windows.delete.DeleteBookWindow;
import com.niicta.view.windows.edit.EditBookWindow;
import com.vaadin.data.ValueProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class BooksView extends VerticalLayout implements View, Updatable {
    public final static String PATH = "books";
    private Collection<Book> books;
    private ModelFactory factory;
    private DAO<Book> bookDAO;
    private Grid<Book> booksGrid;
    private TextField nameTextField;
    private ComboBox<Author> authorBox;
    private ComboBox<Genre> genreBox;
    private Panel panel;
    private Button newBook;
    private Button editBook;
    private Button deleteBook;

    public BooksView(ModelFactory factory, DAO<Book> bookDAO) {
        this.factory = factory;
        this.bookDAO = bookDAO;
        setMargin(new MarginInfo(false, false, false, false));
        setSizeFull();
        initPanel();
        initButtons();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        update();
    }

    @Override
    public void update() {
        try {
            books = bookDAO.findAll();
        } catch (SQLException | IOException e) {
            Notification.show("Oops! Looks like we have some problems with database\n"
                    + "please, contact your administrator and try again later");
        } catch (RuntimeException e) {
            Notification.show("Unknown error\n"
                    + "please, contact your administrator and try again later");
        }
        booksGrid.setItems(books);
        updateFilters();
    }

    private void initPanel(){
        panel = new Panel("Books");
        VerticalLayout panelLayout = new VerticalLayout();
        initFilters(panelLayout);
        initGrid(panelLayout);
        panel.setContent(panelLayout);
        panel.setSizeFull();
        addComponent(panel);
    }

    private void initFilters(VerticalLayout panelLayout) {
        HorizontalLayout filtersLayout = new HorizontalLayout();
        nameTextField = new TextField("Name:");
        authorBox = new ComboBox<>("Author:");
        genreBox = new ComboBox<>("Genre:");
        updateFilters();
        Button apply = new Button("Apply");
        apply.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                performFiltering();
            }
        });
        filtersLayout.addComponents(nameTextField, authorBox, genreBox, apply);
        filtersLayout.setComponentAlignment(apply, Alignment.BOTTOM_CENTER);
        filtersLayout.setSpacing(true);
        panelLayout.addComponent(filtersLayout);
    }

    private void initGrid(VerticalLayout panelLayout) {
        booksGrid = new Grid<>();
        booksGrid.addColumn(Book::getName).setCaption("Name");
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
        panelLayout.addComponent(booksGrid);
    }

    private void initButtons() {
        newBook = new Button("New Book");
        editBook = new Button("Edit");
        deleteBook = new Button("Delete");
        newBook.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Window createBookWindow = new EditBookWindow(bookDAO, factory, BooksView.this);
                UI.getCurrent().addWindow(createBookWindow);
            }
        });
        editBook.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Set<Book> selection = booksGrid.getSelectedItems();
                if (selection.size() == 0){
                    selectNotification();
                    return;
                }
                for (Book book : selection) {
                    Window editBookWindow = new EditBookWindow(bookDAO, book, BooksView.this);
                    UI.getCurrent().addWindow(editBookWindow);
                }
            }
        });
        deleteBook.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Set<Book> selection = booksGrid.getSelectedItems();
                if (selection != null && selection.size() > 0)
                    UI.getCurrent().addWindow(new DeleteBookWindow(selection, bookDAO, BooksView.this));
                else
                    selectNotification();
            }
        });
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.addComponents(newBook, editBook, deleteBook);
        buttons.setMargin(new MarginInfo(false, true, true, true));
        addComponent(buttons);
    }

    private void performFiltering() {
        String name = nameTextField.getValue();
        Author author = authorBox.getValue();
        Genre genre = genreBox.getValue();
        List<Book> result = new ArrayList<>(books);
        if ((name == null || "".equals(name)) && author == null && genre == null) {
            booksGrid.setItems(result);
            return;
        }
        name = name.trim();
        if (!"".equals(name)) {
            List<Book> filteredByNameBooks = new ArrayList<>();
            for (Book book : result) {
                if (book.getName().toLowerCase().contains(name)) {
                    filteredByNameBooks.add(book);
                }
            }
            result = filteredByNameBooks;
        }
        if (author != null){
            List<Book> filteredByAuthorBooks = new ArrayList<>();
            for (Book book : result) {
                if (book.getAuthor().getId().equals(author.getId())) {
                    filteredByAuthorBooks.add(book);
                }
            }
            result = filteredByAuthorBooks;
        }
        if (genre != null){
            List<Book> filteredByGenreBooks = new ArrayList<>();
            for (Book book : result) {
                if (book.getGenre().getId().equals(genre.getId())) {
                    filteredByGenreBooks.add(book);
                }
            }
            result = filteredByGenreBooks;
        }
        booksGrid.setItems(result);
    }

    private void updateFilters() {
        try {
            authorBox.setItems(((BookDAO) bookDAO).getAuthorDAO().findAll());
            genreBox.setItems(((BookDAO) bookDAO).getGenreDAO().findAll());
            authorBox.setItemCaptionGenerator(Author::getAuthorCode);
            genreBox.setItemCaptionGenerator(Genre::getName);
        } catch (SQLException | IOException e) {
            Notification.show("Oops! Looks like we have some problems with database\n"
                    + "please, contact your administrator and try again later");
        }
    }

    private void selectNotification() {
        Notification.show("Please, select at least one item");
    }
}
