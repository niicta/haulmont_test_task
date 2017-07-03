package com.niicta.view.views;

import com.niicta.data.DAO;
import com.niicta.model.Book;
import com.niicta.model.Genre;
import com.niicta.model.factories.ModelFactory;
import com.niicta.view.Updatable;
import com.niicta.view.windows.delete.DeleteGenreWindow;
import com.niicta.view.windows.edit.EditGenreWindow;
import com.niicta.view.windows.statistic.BookByGenreStatisticWindow;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;

public class GenresView extends VerticalLayout implements View, Updatable {
    public final static String PATH = "genres";
    private Collection<Genre> genres;
    private ModelFactory factory;
    private DAO<Genre> genreDAO;
    private DAO<Book> bookDAO;
    private Grid<Genre> genresGrid;
    private Panel panel;
    private Button newGenre;
    private Button editGenre;
    private Button deleteGenre;
    private Button showStatistic;

    public GenresView(ModelFactory factory, DAO<Genre> genreDAO, DAO<Book> bookDAO) {
        this.factory = factory;
        this.genreDAO = genreDAO;
        this.bookDAO = bookDAO;
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
            genres = genreDAO.findAll();
        } catch (SQLException | IOException e) {
            Notification.show("Oops! Looks like we have some problems with database\n"
                    + "please, contact your administrator and try again later");
        } catch (RuntimeException e) {
            Notification.show("Unknown error\n"
                    + "please, contact your administrator and try again later");
        }
        genresGrid.setItems(genres);
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
        genresGrid = new Grid<>("Genres:");
        genresGrid.addColumn(Genre::getName).setCaption("Name");
        genresGrid.setWidth("100%");
        genresGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        genresGrid.setWidth("100%");
        genresGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        panelLayout.addComponent(genresGrid);
    }

    private void initButtons(){
        newGenre = new Button("New Genre");
        editGenre = new Button("Edit");
        deleteGenre = new Button("Delete");
        showStatistic = new Button("Show Statistic");
        newGenre.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Window createGenreWindow = new EditGenreWindow(genreDAO, factory, GenresView.this);
                UI.getCurrent().addWindow(createGenreWindow);
            }
        });
        editGenre.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Set<Genre> selection = genresGrid.getSelectedItems();
                if (selection.size() == 0){
                    selectNotification();
                    return;
                }
                for (Genre genre : selection) {
                    Window editGenreWindow = new EditGenreWindow(genreDAO, genre, GenresView.this);
                    UI.getCurrent().addWindow(editGenreWindow);
                }
            }
        });
        deleteGenre.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Set<Genre> selection = genresGrid.getSelectedItems();
                if (selection != null && selection.size() > 0)
                    UI.getCurrent().addWindow(new DeleteGenreWindow(selection, genreDAO, GenresView.this));
                else
                    selectNotification();
            }
        });
        showStatistic.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Set<Genre> selection = genresGrid.getSelectedItems();
                if (selection.size() == 0){
                    selectNotification();
                    return;
                }
                Window bookByGenreWindow = new BookByGenreStatisticWindow(bookDAO, selection);
                UI.getCurrent().addWindow(bookByGenreWindow);

            }
        });
        HorizontalLayout buttons = new HorizontalLayout();
        HorizontalLayout cud = new HorizontalLayout(); //create, update, delete buttons
        HorizontalLayout statistic = new HorizontalLayout();
        cud.addComponents(newGenre, editGenre, deleteGenre);
        statistic.addComponent(showStatistic);
        buttons.setWidth("100%");
        buttons.addComponents(cud, statistic);
        buttons.setComponentAlignment(statistic, Alignment.MIDDLE_RIGHT);
        buttons.setMargin(new MarginInfo(false, true, true, true));
        addComponent(buttons);
    }

    private void selectNotification() {
        Notification.show("Please, select at least one item");
    }
}
