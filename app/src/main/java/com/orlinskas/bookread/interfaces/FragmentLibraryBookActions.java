package com.orlinskas.bookread.interfaces;

import com.orlinskas.bookread.Book;

public interface FragmentLibraryBookActions {
    boolean deleteBook(Book book);
    void openBook(Book book);
    boolean showBookAnnotation(Book book);
    boolean setTrainingMode(Book book, boolean mode);
}
