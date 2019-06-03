package com.orlinskas.bookread.helpers;

import android.content.Context;

import com.orlinskas.bookread.Book;
import com.orlinskas.bookread.parsers.ParserFb2ToXML;
import com.orlinskas.bookread.parsers.ParserXmlToBook;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;

public class BookCreator {

    public Book create(Context context, File book){
        ParserFb2ToXML parserFb2ToXML = new ParserFb2ToXML();
        XmlPullParser xml = parserFb2ToXML.parse(book);
        ParserXmlToBook parserXmlToBook = new ParserXmlToBook(context);
        return parserXmlToBook.parse(xml, book.getName());
    }

}
