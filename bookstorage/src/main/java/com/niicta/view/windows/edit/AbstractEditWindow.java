package com.niicta.view.windows.edit;

import com.niicta.data.DAO;
import com.niicta.exceptions.UniqueConstraintException;
import com.niicta.model.factories.ModelFactory;
import com.niicta.view.Updatable;
import com.vaadin.data.Binder;
import com.vaadin.data.BindingValidationStatus;
import com.vaadin.data.ValidationException;
import com.vaadin.server.UserError;
import com.vaadin.ui.*;

import java.io.IOException;
import java.sql.SQLException;

public abstract class AbstractEditWindow<T> extends Window {

    private boolean rootNeedsToUpdate;
    private Updatable root;
    private Button confirmButton = new Button("Confirm");
    private Button cancelButton = new Button("Cancel");
    Binder<T> binder = new Binder<>();
    ModelFactory modelFactory;
    DAO<T> DAO;
    T targetEntity;
    FormLayout layout = new FormLayout();

    public AbstractEditWindow(DAO<T> DAO, ModelFactory modelFactory, Updatable root, String caption) {
        super(caption);
        this.modelFactory = modelFactory;
        this.DAO = DAO;
        this.targetEntity = null;
        this.root = root;
        init();
    }

    public AbstractEditWindow(DAO<T> DAO, T targetEntity, Updatable root, String caption) {
        super(caption);
        this.DAO = DAO;
        this.targetEntity = targetEntity;
        this.root = root;
        init();
    }

    private void init(){
        initContent();
        initButtons();
        initBinder();
        layout.setSpacing(true);
        layout.setSizeUndefined();
        layout.setMargin(true);
        setContent(layout);
        center();
        addCloseListener(new CloseListener() {
            @Override
            public void windowClose(CloseEvent closeEvent) {
                if (rootNeedsToUpdate){
                    root.update();
                }
            }
        });
        if (targetEntity != null)
            binder.readBean(targetEntity);
        setModal(true);
        setClosable(false);
        setResizable(false);
    }

    private void initButtons() {
        if (targetEntity != null) {
            confirmButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    try {
                        binder.writeBean(targetEntity);
                        DAO.update(targetEntity);
                        rootNeedsToUpdate = true;
                        close();
                    } catch (ValidationException e) {
                        //очень долго парился, уверен, что это костыл, но у меня без этого не получилось заставить
                        //его выводить сообщения об ошибке в каждом поле, если пользователь открывает форму с пустыми полями и сразу же
                        //нажимает Confirm
                        for (BindingValidationStatus result : e.getFieldValidationErrors()){
                            ((AbstractComponent)result.getField()).setComponentError(new UserError((String)result.getMessage().get()));
                        }
                        Notification.show("Can't save this,\n"
                                + "please check error messages for each field.");
                    } catch (SQLException | IOException e) {
                        Notification.show("Oops! Looks like we have some problems with database\n"
                                + "please, contact your administrator and try again later");
                    } catch (UniqueConstraintException e) {
                        Notification.show(e.getMessage());
                    } catch (RuntimeException e) {
                        Notification.show("Unknown error\n"
                                + "please, contact your administrator and try again later");
                    }
                }
            });
        } else {
            confirmButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    try {
                        targetEntity = createEntity();
                        binder.writeBean(targetEntity);
                        DAO.save(targetEntity);
                        rootNeedsToUpdate = true;
                        close();
                    } catch (ValidationException e) {
                        //очень долго парился, уверен, что это костыл, но у меня без этого не получилось заставить
                        //его выводить сообщения об ошибке в каждом поле, если пользователь открывает форму с пустыми полями и сразу же
                        //нажимает Confirm
                        for (BindingValidationStatus result : e.getFieldValidationErrors()){
                            ((AbstractComponent)result.getField()).setComponentError(new UserError((String)result.getMessage().get()));
                        }
                        Notification.show("Can't save this,\n"
                                + "please check error messages for each field.");
                    } catch (SQLException | IOException e) {
                        Notification.show("Oops! Looks like we have some problems with database\n"
                                + "please, contact your administrator and try again later");
                    } catch (UniqueConstraintException e) {
                        Notification.show(e.getMessage());
                    } catch (RuntimeException e) {
                        Notification.show("Unknown error\n"
                                + "please, contact your administrator and try again later");
                    }
                }
            });
        }
        cancelButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                close();
            }
        });
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addComponents(confirmButton, cancelButton);
        buttons.setSpacing(true);
        buttons.setSizeUndefined();
        layout.addComponent(buttons);
    }

    abstract void initContent();

    abstract void initBinder();

    abstract T createEntity();
}
