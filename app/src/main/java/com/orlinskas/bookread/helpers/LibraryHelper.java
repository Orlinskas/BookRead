package com.orlinskas.bookread.helpers;

import android.content.Context;

import com.orlinskas.bookread.Book;
import com.orlinskas.bookread.Library;
import com.orlinskas.bookread.data.LibraryData;

import java.util.ArrayList;

public class LibraryHelper {
    private ArrayList<Book> libraryBooks = new ArrayList<>();
    private Context context;

    public LibraryHelper(Context context){
        this.context = context;
    }

    public boolean addBook(Book book) throws Exception{
        LibraryData libraryData = new LibraryData(context);
        Library library = libraryData.loadLibrary();
        libraryBooks = library.getBooks();
        if (containBookInLibrary(libraryBooks, book)){
            return false;
        }
        else {
            libraryBooks.add(book);
            library.setBooks(libraryBooks);
            libraryData.saveLibrary(library);
            return true;
        }
    }

    public Book getBook(int id) throws Exception {
        LibraryData libraryData = new LibraryData(context);
        Library library = libraryData.loadLibrary();
        libraryBooks = library.getBooks();

        return libraryBooks.get(id);
    }

    public Book getBook(Book book) throws Exception {
        LibraryData libraryData = new LibraryData(context);
        Library library = libraryData.loadLibrary();
        libraryBooks = library.getBooks();
        for (Book libraryBook: libraryBooks) {
            if (libraryBook.equals(book)){
                return libraryBook;
            }
        }
        return null;
    }

    public ArrayList<Book> getBooks() throws Exception {
        LibraryData libraryData = new LibraryData(context);
        Library library = libraryData.loadLibrary();
        return libraryBooks = library.getBooks();
    }

    private boolean containBookInLibrary(ArrayList<Book> libraryBooks, Book book) {
        for (Book libraryBook: libraryBooks) {
            if (libraryBook.equals(book)){
             return true;
            }
        }
        return false;
    }
}
