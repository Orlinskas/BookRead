package com.orlinskas.bookread.helpers;

import android.content.Context;

import com.orlinskas.bookread.Book;
import com.orlinskas.bookread.Library;
import com.orlinskas.bookread.data.LibraryData;
import com.orlinskas.bookread.data.LicenceData;
import com.orlinskas.bookread.parsers.ParserFb2;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class BookHelper {
    private ArrayList<Book> books = new ArrayList<>();
    private Context context;

    public BookHelper(Context context){
        this.context = context;
    }

    public Book createBook (XmlPullParser xml){
        ParserFb2 parserFb2 = new ParserFb2();
        return  parserFb2.parse(xml);
    }

    public boolean addBookInLibrary (Book book){
        LibraryData libraryData = new LibraryData(context);
        Library library = libraryData.loadLibrary();
        books = library.getBooks();
        books.add(book); //нужно добавить проверку на существование книги в библиотеке
        library.setBooks(books);
        libraryData.saveLibrary(library);

        LicenceData.newBookCreate();

        return true;
    }

    public Book getBook (int id){
        LibraryData libraryData = new LibraryData(context);
        Library library = libraryData.loadLibrary();
        books = library.getBooks();
        return books.get(id);
    }

    public int booksSize(){
        LibraryData libraryData = new LibraryData(context);
        Library library = libraryData.loadLibrary();
        return library.getBooks().size();
    }
}
