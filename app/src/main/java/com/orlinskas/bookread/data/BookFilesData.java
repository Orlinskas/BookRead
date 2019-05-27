package com.orlinskas.bookread.data;

import com.orlinskas.bookread.Book;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class BookFilesData implements java.io.Serializable {
    private static ArrayList<Book> books = new ArrayList<>();

    public static ArrayList<Book> getBooks() {
        return books;
    }

    public static void setBooks(ArrayList<Book> books) {
        BookFilesData.books = books;
    }

    public static ArrayList<Book> findBooks() {
        try{
            ObjectInputStream ois =
                    new ObjectInputStream(new FileInputStream("serializedBooks.txt"));

           return (ArrayList<Book>) ois.readObject();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return books;
    }

    public static void saveBooks(ArrayList<Book> books) {
        try (FileOutputStream fos = new FileOutputStream("serializedBooks.txt");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(books);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
