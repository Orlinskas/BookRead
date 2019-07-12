package com.orlinskas.bookread;

import com.orlinskas.bookread.constants.BookConstant;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * @author Orlinskas
 * @version 2
 */
public class Book implements Serializable {
    private SimpleDateFormat commonFormat = new SimpleDateFormat(BookConstant.YYYY_MM_DD, Locale.ENGLISH);

    private String authorFullName = BookConstant.UNKNOWN_AUTHOR;
    private String title = BookConstant.NA_BOOK_TITLE;
    private String annotation = BookConstant.DEFAULT_ANNOTATION;
    private String createDate = BookConstant.NOT_AVAILABILITY;
    private String bodyText = BookConstant.NOT_AVAILABILITY;
    private int coverImagePath;
    private File bodyTextFile;
    private File coverImageFile;
    private int readingProgressPercent = 0;
    private String databaseTableName;
    private boolean trainingMode = true;
    private boolean isTranslateWords;

    public Book(){
    }

    public Book(String authorFullName, String title, String annotation){
        this.authorFullName = authorFullName;
        this.title = title;
        this.annotation = annotation;
        this.createDate = commonFormat.format(new Date());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(authorFullName, book.authorFullName) &&
                Objects.equals(title, book.title) &&
                Objects.equals(createDate, book.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorFullName, title, createDate);
    }

    public String getAuthorFullName() {
        return authorFullName;
    }

    public void setAuthorFullName(String authorFullName) {
        this.authorFullName = authorFullName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setDate() {
        this.createDate = commonFormat.format(new Date());
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public File getBodyTextFile() {
        return bodyTextFile;
    }

    public void setBodyTextFile(File bodyTextFile) {
        this.bodyTextFile = bodyTextFile;
    }

    public File getCoverImageFile() {
        return coverImageFile;
    }

    public void setCoverImageFile(File coverImageFile) {
        this.coverImageFile = coverImageFile;
    }

    public int getCoverImagePath() {
        return coverImagePath;
    }

    public void setCoverImagePath(int coverImagePath) {
        this.coverImagePath = coverImagePath;
    }

    public int getReadingProgressPercent() {
        return readingProgressPercent;
    }

    public void setReadingProgressPercent(int readingProgressPercent) {
        this.readingProgressPercent = readingProgressPercent;
    }

    public String getDatabaseTableName() {
        return databaseTableName;
    }

    public void setDatabaseTableName(String databaseTableName) {
        this.databaseTableName = databaseTableName;
    }

    public boolean isTrainingMode() {
        return trainingMode;
    }

    public void setMode(boolean trainingMode) {
        this.trainingMode = trainingMode;
    }

    public boolean isTranslateWords() {
        return isTranslateWords;
    }

    public void setTranslateWords(boolean translateWords) {
        isTranslateWords = translateWords;
    }
}
