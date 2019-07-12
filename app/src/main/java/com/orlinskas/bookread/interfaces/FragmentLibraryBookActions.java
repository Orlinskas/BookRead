package com.orlinskas.bookread.interfaces;

import com.orlinskas.bookread.Book;
/**
 * @author Orlinskas
 * @version 2
 */
public interface FragmentLibraryBookActions {
    void deleteBook(Book book);
    void openBook(Book book);
    void showBookAnnotation(Book book);
    boolean setTrainingMode(Book book, boolean mode);
}
