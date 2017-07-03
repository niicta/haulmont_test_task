package com.niicta.view.windows.delete;

import com.niicta.data.DAO;
import com.niicta.view.Updatable;
import com.vaadin.ui.*;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Set;

public abstract class AbstractDeleteWindow<T> extends Window {
    private Button delete;
    private Button cancel;
    private DAO<T> dao;
    private Updatable root;
    private boolean rootNeedsToUpdate;
    Set<T> set;
    Grid<T> grid;

    AbstractDeleteWindow(Set<T> items, DAO<T> dao, Updatable root){
        super("Confirm deleting ");
        this.set = items;
        this.dao = dao;
        this.root = root;
        initGrid();
        initContent();
        initButtons();
        setModal(true);
        setClosable(false);
        setResizable(false);
    }

    private void initContent() {
        VerticalLayout layout = new VerticalLayout();
        grid.getEditor().setEnabled(false);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        Label description = new Label("Please, confirm deleting next objects:");
        delete = new Button("Delete");
        cancel = new Button("Cancel");
        addCloseListener(new CloseListener() {
            @Override
            public void windowClose(CloseEvent closeEvent) {
                if (rootNeedsToUpdate){
                    root.update();
                }
            }
        });
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setWidth("100%");
        buttons.addComponents(delete, cancel);
        buttons.setComponentAlignment(delete, Alignment.BOTTOM_LEFT);
        buttons.setComponentAlignment(cancel, Alignment.MIDDLE_RIGHT);
        layout.addComponents(description, grid, buttons);
        layout.setSpacing(true);
        setContent(layout);
        center();
    }

    private void initButtons() {
        delete.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    dao.delete(set);
                    rootNeedsToUpdate = true;
                    close();
                } catch (SQLIntegrityConstraintViolationException e){
                    Notification.show("Can't delete this...\n"
                            + "maybe, there are some objects referencing the deleting one");
                } catch (SQLException | IOException e) {
                    Notification.show("Oops! Looks like we have some problems with database\n"
                            + "please, contact your administrator and try again later");
                } catch (RuntimeException e) {
                    Notification.show("Unknown error\n"
                            + "please, contact your administrator and try again later");
                }
            }
        });
        cancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                rootNeedsToUpdate = false;
                close();
            }
        });
    }

    abstract void initGrid();
}
