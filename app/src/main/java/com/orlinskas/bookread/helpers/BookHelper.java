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
        ParserFb2 parserFb2 = new ParserFb2();
        Book book = parserFb2.parse(xml);
        book.setDate();

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
