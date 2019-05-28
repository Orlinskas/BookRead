package com.orlinskas.bookread.helpers;

import android.content.Context;

import com.orlinskas.bookread.Book;
import com.orlinskas.bookread.parsers.ParserFb2;

import org.xmlpull.v1.XmlPullParser;

public class BookCreator {

    public Book create(Context context, XmlPullParser xml){
        ParserFb2 parserFb2 = new ParserFb2();
        return writeBookBodyToFile(context, parserFb2.parse(xml));
    }

    private Book writeBookBodyToFile (Context context, Book book) {
        BookBodyFileWriter bookBodyFileWriter = new BookBodyFileWriter();
        book.setBookBodyFile(bookBodyFileWriter.write(context, book));
        return book;
    }
}
