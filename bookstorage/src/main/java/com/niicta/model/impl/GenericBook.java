package com.niicta.model.impl;

import com.niicta.model.Author;
import com.niicta.model.Book;
import com.niicta.model.Genre;

import java.time.LocalDate;
import java.util.Date;


public class GenericBook implements Book {
    private Long id;
    private String name;
    private Genre genre;
    private Author author;
    private String city;
    private LocalDate year;
    private long count;
    private Publisher Publisher;

    public GenericBook(Genre genreName, String name, Author author, Publisher publisher, LocalDate year, String city, int count) {
        this.name = name;
        this.genre = genreName;
        this.author = author;
        this.year = year;
        this.city = city;
        this.count = 0;
        Publisher = publisher;
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

    @Override
    public Genre getGenre() {
        return genre;
    }

    @Override
    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public Author getAuthor() {
        return author;
    }

    @Override
    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public LocalDate getYear() {
        return year;
    }

    @Override
    public void setYear(LocalDate year) {
        this.year = year;
    }

    @Override
    public long getCount() {
        return count;
    }

    @Override
    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public Book.Publisher getPublisher() {
        return Publisher;
    }

    @Override
    public void setPublisher(Book.Publisher publisher) {
        Publisher = publisher;
    }

    @Override
    public String toString(){
        return "Book" +
                "\nName: " +getName()+
                "\nAuthorID: " + getAuthor().getId()+
                "\nGenreID: " +getGenre().getId()+
                "\nCity: " + getCity()+
                "\nYear: " +getYear().getYear()+
                "\nCount: " +getCount()+
                "\npublisher: " + getPublisher();
    }
}
