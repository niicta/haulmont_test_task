package com.niicta.model;

public interface Genre {
    //surrogate key
    void setId(Long id);
    Long getId();
    //natural key
    void setName(String name);
    String getName();

}
