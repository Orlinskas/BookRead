package com.orlinskas.bookread.helpers;

import com.orlinskas.bookread.Book;
import com.orlinskas.bookread.parsers.ParserFb2;

import org.xmlpull.v1.XmlPullParser;

public class BookCreater {

    public Book create(XmlPullParser xml){
        ParserFb2 parserFb2 = new ParserFb2();
        return  parserFb2.parse(xml);
    }
}
