package com.orlinskas.bookread.helpers;

import com.orlinskas.bookread.Book;
import com.orlinskas.bookread.data.BookFilesData;
import com.orlinskas.bookread.data.LicenceData;
import com.orlinskas.bookread.parsers.ParserFb2;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class BookHelper {
    private ArrayList<Book> books = new ArrayList<>();

    public void createBook (XmlPullParser xml){
        Book book = new Book();
        ParserFb2 parserFb2 = new ParserFb2();

        book.setAuthorName(parserFb2.findAuthorName(xml));
        book.setBookTitle(parserFb2.findBookTitle(xml));
        book.setAnnotation(parserFb2.findAnnotation(xml));
        book.setDate();
        book.setBookBody(parserFb2.findBody(xml));

        books = BookFilesData.findBooks();
        books.add(book);
        BookFilesData.saveBooks(books);

        LicenceData.newBookCreate();
    }

    public Book getBook (int id){
        return books.get(id);
    }

    public int booksCount(){
        return books.size();
    }
}
