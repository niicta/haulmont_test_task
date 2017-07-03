package com.niicta.view.views;

import com.niicta.data.DAO;
import com.niicta.model.Author;
import com.niicta.model.factories.ModelFactory;
import com.niicta.model.factories.ModelFactoryProvider;
import com.niicta.view.Updatable;
import com.niicta.view.windows.delete.DeleteAuthorWindow;
import com.niicta.view.windows.edit.EditAuthorWindow;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;

public class AuthorsView extends VerticalLayout implements View, Updatable {
    public final static String PATH = "authors";
    private Collection<Author> authors;
    private ModelFactory factory;
    private DAO<Author> authorsDAO;
    private Grid<Author> authorsGrid;
    private Panel panel;
    private Button newAuthor;
    private Button editAuthor;
    private Button deleteAuthor;

    public AuthorsView(ModelFactory factory, DAO<Author> authorsDAO) {
        this.factory = factory;
        this.authorsDAO = authorsDAO;
        setMargin(new MarginInfo(false, false, false, false));
        setSpacing(true);
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
            authors = authorsDAO.findAll();
        } catch (SQLException | IOException e) {
            Notification.show("Oops! Looks like we have some problems with database\n"
                    + "please, contact your administrator and try again later");
        } catch (RuntimeException e) {
            Notification.show("Unknown error\n"
                    + "please, contact your administrator and try again later");
        }
        authorsGrid.setItems(authors);
    }

    private void initPanel(){
        panel = new Panel("Authors");
        VerticalLayout panelLayout = new VerticalLayout();
        initGrid(panelLayout);
        panel.setContent(panelLayout);
        panel.setSizeFull();
        addComponent(panel);
    }

    private void initGrid(VerticalLayout panelLayout){
        authorsGrid = new Grid<>();
        authorsGrid.addColumn(Author::getAuthorCode).setCaption("Code");
        authorsGrid.addColumn(Author::getName).setCaption("Name");
        authorsGrid.addColumn(Author::getMiddleName).setCaption("Middle Name");
        authorsGrid.addColumn(Author::getLastName).setCaption("Last Name");
        authorsGrid.setWidth("100%");
        authorsGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        panelLayout.addComponent(authorsGrid);
    }

    private void initButtons(){
        newAuthor = new Button("New Author");
        editAuthor = new Button("Edit");
        deleteAuthor = new Button("Delete");
        newAuthor.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Window createAuthorWindow = new EditAuthorWindow(authorsDAO, factory, AuthorsView.this);
                UI.getCurrent().addWindow(createAuthorWindow);
            }
        });
        editAuthor.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Set<Author> selection = authorsGrid.getSelectedItems();
                if (selection.size() == 0){
                    selectNotification();
                    return;
                }
                for (Author author : selection) {
                    Window editAuthorWindow = new EditAuthorWindow(authorsDAO, author, AuthorsView.this);
                    UI.getCurrent().addWindow(editAuthorWindow);
                }
            }
        });
        deleteAuthor.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Set<Author> selection = authorsGrid.getSelectedItems();
                if (selection != null && selection.size() > 0)
                    UI.getCurrent().addWindow(new DeleteAuthorWindow(selection, authorsDAO, AuthorsView.this));
                else
                    selectNotification();
            }
        });
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.addComponents(newAuthor, editAuthor, deleteAuthor);
        buttons.setMargin(new MarginInfo(false, true, true, true));
        addComponent(buttons);
    }

    private void selectNotification() {
        Notification.show("Please, select at least one item");
    }
}
