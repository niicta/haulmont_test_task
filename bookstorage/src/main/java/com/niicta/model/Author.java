package com.niicta.model;

public interface Author {
    //surrogate key
    Long getId();
    void setId(Long id);
    //natural key
    String getAuthorCode();
    void setAuthorCode(String code);
    String getName();
    void setName(String name);
    String getLastName();
    void setLastName(String lastName);
    String getMiddleName();
    void setMiddleName(String middleName);
}
