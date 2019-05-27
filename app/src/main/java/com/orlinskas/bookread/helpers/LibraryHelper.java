package com.orlinskas.bookread.helpers;

import android.content.Context;

import com.orlinskas.bookread.Book;
import com.orlinskas.bookread.Library;
import com.orlinskas.bookread.data.LibraryData;

import java.util.ArrayList;

public class LibraryHelper {
    private ArrayList<Book> books = new ArrayList<>();
    private Context context;

    public LibraryHelper(Context context){
        this.context = context;
    }

    public boolean add(Book book){
        try {
            LibraryData libraryData = new LibraryData(context);
            Library library = libraryData.loadLibrary();
            books = library.getBooks();
            books.add(book); //нужно добавить проверку на существование книги в библиотеке
            library.setBooks(books);
            libraryData.saveLibrary(library);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Book getBook(int id){ //после переопределения equals изменить на гет (Бук)
        try {
            LibraryData libraryData = new LibraryData(context);
            Library library = libraryData.loadLibrary();
            books = library.getBooks();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books.get(id);
    }
}
