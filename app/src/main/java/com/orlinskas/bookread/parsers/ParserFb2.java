package com.orlinskas.bookread.parsers;


import com.orlinskas.bookread.constants.BookConstant;
import com.orlinskas.bookread.constants.XML_TAG;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class ParserFb2 {

    public String findAuthorName (XmlPullParser parser){
        //нужно исключить неправильные имена авторов!
        StringBuilder authorName = new StringBuilder();

        int eventType;
        String tag;
        boolean stop = false;

        try {
            do {
                eventType = parser.next();
                tag = parser.getName();

                if (tag != null && tag.equals(XML_TAG.AUTHOR) && !stop) {
                    try {
                        authorName.append(parser.nextText()).append(" ");
                    } catch (Exception ignored) {}
                    while (checkEndTag(XML_TAG.AUTHOR, parser.getName(), parser.getEventType()) && !stop) {
                        try {
                            authorName.append(parser.nextText()).append(" ");
                        } catch (Exception ignored) {}
                        if (!checkEndTag(XML_TAG.LAST_NAME, parser.getName(), parser.getEventType())){
                            stop = true;
                        }
                        parser.next();
                    }
                }

            } while (eventType != parser.END_DOCUMENT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.valueOf(authorName);
    }

    public String findBookTitle (XmlPullParser parser){
        //не останавливается в конце тега, доходит до конца документа
        String bookTitle = BookConstant.NA_BOOK_TITLE;
        String tag;
        int eventType;
        boolean stop = false;

        try {
            do {
                eventType = parser.next();
                tag = parser.getName();

                if (tag != null && tag.equals(XML_TAG.BOOK_TITLE) && !stop) {
                    try {
                        bookTitle = parser.nextText();
                    } catch (Exception ignored) {}
                    while (checkEndTag(XML_TAG.BOOK_TITLE, parser.getName(), parser.getEventType()) && !stop) {
                        try {
                            bookTitle = parser.nextText();
                        } catch (Exception ignored) {}
                        if (!checkEndTag(XML_TAG.BOOK_TITLE, parser.getName(), parser.getEventType())){
                            stop = true;
                        }
                        parser.next();
                    }
                }

            } while (eventType != parser.END_DOCUMENT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookTitle;
    }

    public String findAnnotation(XmlPullParser parser){
        //не останавливается в конце тега, доходит до конца документа
        StringBuilder annotation = new StringBuilder();

        int eventType;
        String tag;
        boolean stop = false;

        try {
            do {
                eventType = parser.next();
                tag = parser.getName();

                if (tag != null && tag.equals(XML_TAG.ANNOTATION) && !stop) {
                    try {
                        annotation.append(parser.nextText()).append(" ");
                    } catch (Exception ignored) {}
                    while (checkEndTag(XML_TAG.ANNOTATION, parser.getName(), parser.getEventType()) && !stop) {
                        try {
                            annotation.append(parser.nextText()).append(" ");
                        } catch (Exception ignored) {}
                        if (!checkEndTag(XML_TAG.ANNOTATION, parser.getName(), parser.getEventType())){
                            stop = true;
                        }
                        parser.next();
                    }
                }

            } while (eventType != parser.END_DOCUMENT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.valueOf(annotation);
    }

    public String findBody(XmlPullParser parser){
        //не останавливается в конце тега, доходит до конца документа
        StringBuilder body = new StringBuilder();

        int eventType;
        String tag;
        boolean stop = false;

        try {
            do {
                eventType = parser.next();
                tag = parser.getName();

                if (tag != null && tag.equals(XML_TAG.BODY) && !stop) {
                    while (checkEndTag(XML_TAG.BODY, parser.getName(), parser.getEventType()) && !stop) {
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
                        if (!checkEndTag(XML_TAG.BODY, parser.getName(), parser.getEventType())){
                            stop = true;
                        }
                        parser.next();
                    }
                }

            } while (eventType != parser.END_DOCUMENT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return body.toString();
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
