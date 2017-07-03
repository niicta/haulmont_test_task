package com.niicta.model.impl;

import com.niicta.model.Genre;

public class GenericGenre implements Genre {

    private Long id;
    private String name;

    public GenericGenre(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
