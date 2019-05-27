package com.orlinskas.bookread.data;

import android.content.Context;

import com.orlinskas.bookread.Book;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class BooksData implements java.io.Serializable {
    private static final String FILE_BOOKS_DATA_NAME  = "serializedBooks" + ".txt";
    private Context context;

    public BooksData(Context context){
        this.context = context;
    }
    public  ArrayList<Book> findBooks() {
        String filePath = context.getFilesDir().getPath() + "/" + FILE_BOOKS_DATA_NAME;
        File file = new File(filePath);

        try{
            ObjectInputStream ois =
                    new ObjectInputStream(new FileInputStream(file));

           return (ArrayList<Book>) ois.readObject();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return books;
    }

    public void saveBooks(ArrayList<Book> books) {
        String filePath = context.getFilesDir().getPath() + "/" + FILE_BOOKS_DATA_NAME;
        File file = new File(filePath);

        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(books);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
