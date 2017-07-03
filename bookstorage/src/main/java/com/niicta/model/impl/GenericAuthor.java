package com.niicta.model.impl;

import com.niicta.model.Author;

public class GenericAuthor implements Author {

    private Long id;
    private String authorCode;
    private String name;
    private String middleName;
    private String lastName;

    public GenericAuthor(String authorCode, String name, String middleName, String lastName) {
        this.authorCode = authorCode;
        this.name = name;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getAuthorCode() {
        return authorCode;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getMiddleName() {
        return middleName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setAuthorCode(String registrationCode) {
        this.authorCode = registrationCode;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString(){
        return "Author:"+
                " \nid: " + id +
                " \nregCode: " + authorCode +
                " \nname: " + name +
                " \nmiddleName: " + middleName +
                " \nlastName: " + lastName;
    }
}
