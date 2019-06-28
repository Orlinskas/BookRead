package com.orlinskas.bookread.parsers;


import com.orlinskas.bookread.Book;
import com.orlinskas.bookread.R;
import com.orlinskas.bookread.Word;
import com.orlinskas.bookread.bookReadAlgorithm.WordDataWriter;
import com.orlinskas.bookread.constants.BookConstant;
import com.orlinskas.bookread.constants.XML_TAG;
import com.orlinskas.bookread.data.DatabaseAdapter;
import com.orlinskas.bookread.data.WordsDatabase;
import com.orlinskas.bookread.helpers.BookBodyFileReader;
import com.orlinskas.bookread.helpers.BookBodyFileWriter;
import com.orlinskas.bookread.helpers.BookImageFileWriter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.util.Base64;

public class ParserXmlToBook {
    private Context context;
    private String imageName = null;
    private int[] defaultCoverImages = new int[] {R.drawable.ic_book_blue, R.drawable.ic_book_fiolet,
            R.drawable.ic_book_green, R.drawable.ic_book_orange, R.drawable.ic_book_red};
    private boolean almostFinishParseBook = false;
    private StringBuilder authorName = new StringBuilder();
    private String bookTitle = BookConstant.NA_BOOK_TITLE;
    private StringBuilder annotation = new StringBuilder();
    private StringBuilder body = new StringBuilder();
    private String imageBase64 = null;

    public ParserXmlToBook(Context context){
        this.context = context;
    }

    public Book parse (XmlPullParser parser, String bookPath) {

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
                if (tag != null && tag.equals(XML_TAG.COVER_PAGE)) {
                    while (checkEndTag(XML_TAG.COVER_PAGE, parser.getName(), parser.getEventType())) {
                        parser.next();
                        if (parser.getName() != null &&
                                parser.getName().equals(XML_TAG.IMAGE) &&
                                parser.getEventType() == XmlPullParser.START_TAG){
                            try {
                                imageName = parser.getAttributeValue(0);
                                imageName = imageName.replaceAll("#", "");
                                break;
                            } catch (Exception e) {
                                break;
                            }
                        }
                    }
                }
                if (tag != null && tag.equals(XML_TAG.BODY)) {//очень долго!

                    while (checkEndTag(XML_TAG.BODY, parser.getName(), parser.getEventType())) {
                        try {
                            String text = parser.getText();
                            if(text != null) {
                                body.append(text);
                            }
                        } catch (Exception ignored) {}
                        if (parser.getName() != null && parser.getName().equals(XML_TAG.P) && parser.getEventType() == XmlPullParser.START_TAG){
                            body.append("    ");
                        }
                        if (parser.getName() != null && parser.getName().equals(XML_TAG.TITLE)){
                            body.append("\n");
                        }
                        if (parser.getName() != null && parser.getName().equals(XML_TAG.EMPTY_LINE)){
                            body.append("\n");
                        }
                        parser.next();
                    }
                }
                if (tag != null && tag.equals(XML_TAG.IMAGE_BINARY) && eventType == XmlPullParser.START_TAG) {
                     if (containCoverImage(parser.getAttributeValue(0)) |
                             containCoverImage(parser.getAttributeValue(1))) {
                         try {
                             imageBase64 = parser.nextText();
                         } catch (Exception ignored) {
                         }
                         parser.next();
                     }
                }
                if (eventType == parser.END_DOCUMENT){
                    almostFinishParseBook = true;
                    //break;
                }

            } while (/*eventType != parser.END_DOCUMENT*/ !almostFinishParseBook);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(bookTitle.equals("Unknown") | bookTitle.equals(BookConstant.NA_BOOK_TITLE)){
            bookTitle = bookPath;
        }

        if(almostFinishParseBook){
            return createBook();
        }else {
            return null;
        }
    }

    private Book createBook(){

        Book book = new Book(authorName.toString(), bookTitle, annotation.toString());
        book.setBookBodyFile(writeBookBodyToFile(book, body.toString()));
        if(imageBase64 != null){
            book.setCoverImage(decodeImage(book, imageBase64));
        }
        else {
            book.setCoverImage(null);
            book.setCoverImagePath(defaultCoverImages[randomNumber(defaultCoverImages.length)]);
        }

        BookBodyFileReader bookBodyFileReader = new BookBodyFileReader();
        try {
            addWordsToDataTemp(bookBodyFileReader.read(book.getBookBodyFile()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //addWordsInDatabase(book);

        return book;
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

    private File decodeImage(Book book, String imageBase64) {
        BookImageFileWriter bookImageFileWriter = new BookImageFileWriter();
        File imageFile = bookImageFileWriter.write(context, book.getBookTitle(), book.getDate(), imageBase64);
        try {
            byte data[] = Base64.decode(imageBase64, Base64.DEFAULT);
            FileOutputStream fos = new FileOutputStream(imageFile);
            fos.write(data);
            fos.close();
            return imageFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private File writeBookBodyToFile(Book book, String body) {
        BookBodyFileWriter bookBodyFileWriter = new BookBodyFileWriter();
        return bookBodyFileWriter.write(context, book, body);
    }

    private boolean containCoverImage(String searchText) {
        if (searchText != null & imageName != null){
            return searchText.contains(imageName);
        }
        return false;
    }

    private static int randomNumber(int max)
    {
        return (int) (Math.random() * ++max);
    }

    private void addWordsInDatabase(Book book) {
        String tableName = "asgagag";
        BookBodyFileReader bookBodyFileReader = new BookBodyFileReader();
        WordDataWriter wordDataWriter = new WordDataWriter(tableName, context);
        try {
            ArrayList<String> words = bookBodyFileReader.read(book.getBookBodyFile());
            for (String word : words){
                wordDataWriter.containWordsInDatabase(word);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addWordsToDataTemp(ArrayList<String> words) {
        String tableTemp = WordsDatabase.TABLE_TEMP;
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(context, tableTemp);
        try {
            databaseAdapter.open();
            databaseAdapter.removeAll(tableTemp);
            databaseAdapter.close();
        } catch (Exception e) {
            e.printStackTrace();
            databaseAdapter.close();
        }
        databaseAdapter.openWithTransaction();

        try {
            for (String word : words) {
                if (word.length() > 3) {
                    Word wordData = new Word();
                    wordData.setRussian(word);
                    databaseAdapter.insert(wordData, tableTemp);
                }
            }
        } finally {
            databaseAdapter.closeWithTransaction();
        }
    }
}
