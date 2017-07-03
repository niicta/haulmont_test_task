package com.niicta.view.windows.statistic;

import com.niicta.data.DAO;
import com.vaadin.ui.*;

import java.util.List;
import java.util.Set;

public abstract class AbstractStaticticWindow<SOURCE, TARGET> extends Window {
    DAO<TARGET> dao;
    private Button okButton;
    VerticalLayout layout;
    Set<SOURCE> items;
    List<Grid<TARGET>> grids;

    public AbstractStaticticWindow(Set<SOURCE> items, DAO<TARGET> dao, String caption){
        super(caption);
        this.dao = dao;
        this.items = items;
        init();
    }

    private void init(){
        layout = new VerticalLayout();
        okButton = new Button("OK", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                close();
            }
        });
        initGrids();
        layout.addComponent(okButton);
        layout.setComponentAlignment(okButton, Alignment.BOTTOM_CENTER);
        setContent(layout);
        setModal(true);
        setClosable(false);
        setResizable(false);
    }

    protected abstract void initGrids();
}
