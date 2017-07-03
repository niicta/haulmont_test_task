package com.niicta.view.ui;

//import javax.inject.Inject;

import com.niicta.data.DAO;
import com.niicta.data.factories.DAOFactory;
import com.niicta.data.factories.DAOFactoryProvider;
import com.niicta.model.Author;
import com.niicta.model.Book;
import com.niicta.model.Genre;
import com.niicta.model.factories.ModelFactory;
import com.niicta.model.factories.ModelFactoryProvider;
import com.niicta.view.views.AuthorsView;
import com.niicta.view.views.BooksView;
import com.niicta.view.views.GenresView;
import com.niicta.view.windows.About;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.MenuBar;

import javax.servlet.annotation.WebServlet;
import java.awt.*;

//import com.vaadin.cdi.CDIUI;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
@SuppressWarnings("serial")
public class MyUI extends UI {
    VerticalLayout mainLayout;
    MenuBar menuBar;
    VerticalLayout viewLayout;
    Navigator navigator;
    ModelFactory modelFactory;
    DAOFactory daoFactory;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        modelFactory = ModelFactoryProvider.getProvider().getDefaultModelFactory();
        daoFactory = DAOFactoryProvider.getProvider().getDefaultDAOFactory();
        DAO<Author> authorDAO = daoFactory.createAuthorDAO(modelFactory);
        DAO<Genre> genreDAO = daoFactory.createGenreDAO(modelFactory);
        DAO<Book> bookDAO = daoFactory.createBookDAO(modelFactory, authorDAO , genreDAO);
        mainLayout = new VerticalLayout();
        viewLayout = new VerticalLayout();
        viewLayout.setMargin(new MarginInfo(false, false, false, false));

//        Button debug = new Button("debugging");
//        debug.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent clickEvent) {
//                Page.getCurrent().setLocation("/debug");
//            }
//        });
//        mainLayout.addComponent(debug);
        mainLayout.setWidth("100%");
        mainLayout.setMargin(new MarginInfo(false, false, false, false));
        setContent(mainLayout);
        AuthorsView authorsView = new AuthorsView(modelFactory, authorDAO);
        GenresView genresView = new GenresView(modelFactory, genreDAO, bookDAO);
        BooksView booksView =new BooksView(modelFactory, bookDAO);
        navigator = new Navigator(this, viewLayout);
        navigator.addView(AuthorsView.PATH, authorsView);
        navigator.addView(GenresView.PATH, genresView);
        navigator.addView(BooksView.PATH, booksView);
        navigator.addView("", authorsView);
        initMenu();
        mainLayout.addComponent(viewLayout);
        mainLayout.setComponentAlignment(menuBar, Alignment.TOP_CENTER);
    }

    private void initMenu(){
        menuBar = new MenuBar();
        MenuBar.MenuItem navigation = menuBar.addItem("Navigation", null, null);
        navigation.addItem("Authors", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                navigator.navigateTo(AuthorsView.PATH);
            }
        });
        navigation.addItem("Genres", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                navigator.navigateTo(GenresView.PATH);
            }
        });
        navigation.addItem("Books", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                navigator.navigateTo(BooksView.PATH);
            }
        });
        menuBar.addItem("About...", null, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                UI.getCurrent().addWindow(new About());
            }
        });
        menuBar.setWidth("100%");
        mainLayout.addComponent(menuBar);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
