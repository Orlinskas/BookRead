package com.orlinskas.bookread;

import com.orlinskas.bookread.constants.BookConstant;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Book implements Serializable {
    private SimpleDateFormat commonFormat = new SimpleDateFormat(BookConstant.YYYY_MM_DD, Locale.ENGLISH);

    private String authorName = BookConstant.UNKNOWN_AUTHOR;
    private String bookTitle = BookConstant.NA_BOOK_TITLE;
    private String annotation = BookConstant.DEFAULT_ANNOTATION;
    private String date = BookConstant.NOT_AVAILABILITY;
    private String bookBody = BookConstant.NOT_AVAILABILITY;
    private int coverImagePath;
    private File bookBodyFile;
    private File coverImage;
    private int bookPercentProgress = 0;
    private String dataTableName;

    public Book(String authorName, String bookTitle, String annotation){
        this.authorName = authorName;
        this.bookTitle = bookTitle;
        this.annotation = annotation;
        this.bookBody = bookBody;
        SimpleDateFormat commonFormat = new SimpleDateFormat(BookConstant.YYYY_MM_DD, Locale.ENGLISH);
        try {
            this.date = commonFormat.format(new Date());
        } catch (Exception e) {
            this.date = "n/a";
        }
    }

    public Book(){
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(authorName, book.authorName) &&
                Objects.equals(bookTitle, book.bookTitle) &&
                Objects.equals(date, book.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorName, bookTitle, date);
    }

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

    public File getBookBodyFile() {
        return bookBodyFile;
    }

    public void setBookBodyFile(File bookBodyFile) {
        this.bookBodyFile = bookBodyFile;
    }

    public File getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(File coverImage) {
        this.coverImage = coverImage;
    }

    public int getCoverImagePath() {
        return coverImagePath;
    }

    public void setCoverImagePath(int coverImagePath) {
        this.coverImagePath = coverImagePath;
    }

    public int getBookPercentProgress() {
        return bookPercentProgress;
    }

    public void setBookPercentProgress(int bookPercentProgress) {
        this.bookPercentProgress = bookPercentProgress;
    }

    public String getDataTableName() {
        return dataTableName;
    }

    public void setDataTableName(String dataTableName) {
        this.dataTableName = dataTableName;
    }
}
