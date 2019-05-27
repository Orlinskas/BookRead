package com.orlinskas.bookread.parsers;


import com.orlinskas.bookread.Book;
import com.orlinskas.bookread.constants.BookConstant;
import com.orlinskas.bookread.constants.XML_TAG;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class ParserFb2 {

    public Book parse (XmlPullParser parser){
        //нужно исключить неправильные имена авторов!
        StringBuilder authorName = new StringBuilder();
        String bookTitle = BookConstant.NA_BOOK_TITLE;
        StringBuilder annotation = new StringBuilder();
        StringBuilder body = new StringBuilder();

        int eventType;
        String tag;

        boolean stopSearchAuthorName = false;

        try {
            do {
                eventType = parser.next();
                tag = parser.getName();

                if (tag != null && tag.equals(XML_TAG.AUTHOR) && !stopSearchAuthorName) {
                    try {
                        authorName.append(parser.nextText()).append(" ");
                    } catch (Exception ignored) {}
                    while (checkEndTag(XML_TAG.AUTHOR, parser.getName(), parser.getEventType())) {
                        try {
                            authorName.append(parser.nextText()).append(" ");
                        } catch (Exception ignored) {}
                        if (!checkEndTag(XML_TAG.LAST_NAME, parser.getName(), parser.getEventType())){
                            stopSearchAuthorName = true;
                            break;
                        }
                        parser.next();
                    }
                }
                if (tag != null && tag.equals(XML_TAG.BOOK_TITLE)) {
                    try {
                        bookTitle = parser.nextText();
                    } catch (Exception ignored) {}
                    while (checkEndTag(XML_TAG.BOOK_TITLE, parser.getName(), parser.getEventType())) {
                        try {
                            bookTitle = parser.nextText();
                        } catch (Exception ignored) {}
                        parser.next();
                    }
                }
                if (tag != null && tag.equals(XML_TAG.ANNOTATION)) {
                    try {
                        annotation.append(parser.nextText()).append(" ");
                    } catch (Exception ignored) {}
                    while (checkEndTag(XML_TAG.ANNOTATION, parser.getName(), parser.getEventType())) {
                        try {
                            annotation.append(parser.nextText()).append(" ");
                        } catch (Exception ignored) {}
                        parser.next();
                    }
                }
                if (tag != null && tag.equals(XML_TAG.BODY)) {
                    while (checkEndTag(XML_TAG.BODY, parser.getName(), parser.getEventType())) {
                        try {
                            body.append("   ").append(parser.nextText());
                        } catch (Exception ignored) {}
                        if (parser.getName() != null && parser.getName().equals(XML_TAG.P)){
                            body.append("\n");
                        }
                        if (parser.getName() != null && parser.getName().equals(XML_TAG.TITLE)){
                            body.append("\n" + "\n");
                        }
                        if (parser.getName() != null && parser.getName().equals(XML_TAG.EMPTY_LINE)){
                            body.append("\n" + "\n");
                        }
                        parser.next();
                    }
                }

            } while (eventType != parser.END_DOCUMENT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Book(authorName.toString(), bookTitle, annotation.toString(), body.toString());
    }

    private boolean checkEndTag (String needTag, String nowTag, int eventType){
        if (nowTag == null){
            return true;
        }
        if (nowTag.equals(needTag)){
            return eventType != XmlPullParser.END_TAG;
        }
        return true;
    }

}
