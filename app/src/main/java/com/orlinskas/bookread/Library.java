package com.orlinskas.bookread;

import java.io.Serializable;
import java.util.ArrayList;

public class Library implements Serializable {
    private ArrayList<Book> books = new ArrayList<>();


    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

}
