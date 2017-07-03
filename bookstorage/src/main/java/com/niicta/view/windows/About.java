package com.niicta.view.windows;

import com.vaadin.ui.*;

public class About extends Window {
    public About(){
        super("About");
        init();
        center();
    }

    private void init() {
        VerticalLayout verticalLayout = new VerticalLayout();
        Label description = new Label("This is just a simple electronic logbook application");
        Label description2 = new Label("which allows You to create, update and delete authors, books and genres.");
        Label description3 = new Label("To start working, click to \"Navigation\" at the top of the window and select the necessary category");
        Label empty = new Label("");
        Label description4 = new Label("Author: Nikita Krasnopolsky");
        Component[] c = new Component[]{description, description2, description3, empty, description4};
        for (Component component : c){
            component.setSizeUndefined();
            verticalLayout.addComponent(component);
        }
        verticalLayout.setComponentAlignment(description4, Alignment.BOTTOM_CENTER);
        Button ok = new Button("OK", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                close();
            }
        });
        verticalLayout.addComponent(ok);
        verticalLayout.setComponentAlignment(ok, Alignment.BOTTOM_CENTER);
        setContent(verticalLayout);
        setModal(true);
        setClosable(false);
        setResizable(false);
    }
}
