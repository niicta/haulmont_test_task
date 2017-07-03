package com.niicta.model;

import java.time.LocalDate;

public interface Book{
    //surrogate key
    void setId(Long id);
    Long getId();
    //natural key
    void setName(String name);
    String getName();
    void setAuthor(Author author);
    Author getAuthor();
    void setGenre(Genre genre);
    Genre getGenre();
    void setYear(LocalDate year);
    LocalDate getYear();
    void setCity(String city);
    String getCity();
    //Хотел хранить число экземпляров книги но база этого не хочет - всегда после вставки число оказывалось равным нулю, пробовал
    //с разными целочисленными типами данных
    void setCount(long count);
    long getCount();
    void setPublisher(Publisher publisher);
    Publisher getPublisher();
    enum Publisher {
        MOSKOW("Moskow"), S_PETERSBURG("Saint Petersburg"), O_RELLY("O\'Relly");
        private String value;
        Publisher(String value){
            this.value = value;
        }
        public String getValue(){
            return value;
        }

        @Override
        public String toString(){
            return this.getValue();
        }
    }
}
