package com.orlinskas.bookread;

import com.orlinskas.bookread.constants.BookConstant;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Book implements Serializable {
    private String authorName = BookConstant.UNKNOWN_AUTHOR;
    private String bookTitle = BookConstant.NA_BOOK_TITLE;
    private String annotation = BookConstant.DEFAULT_ANNOTATION;
    private String date = BookConstant.NOT_AVAILABILITY;
    private String bookBody = BookConstant.NOT_AVAILABILITY;
    //private File bookText;

    public Book(String authorName, String bookTitle, String annotation, String bookBody){
        this.authorName = authorName;
        this.bookTitle = bookTitle;
        this.annotation = annotation;
        this.bookBody = bookBody;
    }

    public Book(){
    }

    private SimpleDateFormat commonFormat = new SimpleDateFormat(BookConstant.YYYY_MM_DD, Locale.ENGLISH);

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

   /* public File getBookText() {
        return bookText;
    }

    public void setBookText(File bookText) {
        this.bookText = bookText;
    } */

    public String getDate() {
        return date;
    }

    public void setDate() {
        try {
            this.date = commonFormat.format(new Date());
        } catch (Exception e) {
            this.date = "n/a";
        }
    }

    public String getBookBody() {
        return bookBody;
    }

    public void setBookBody(String bookBody) {
        this.bookBody = bookBody;
    }
}
