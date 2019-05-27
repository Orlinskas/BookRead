package com.orlinskas.bookread.helpers;

import android.content.Context;

import com.orlinskas.bookread.Book;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BookBodyFileWriter {

    public File write (Context context, Book book) {
        String fileName = book.getBookTitle() + ".txt";
        String filePath = context.getFilesDir().getPath() + "/" + fileName;
        File file = new File(filePath);
        try (FileWriter writer = new FileWriter(file, true)) {
            // запись всей строки
            writer.write(book.getBookBody());
            writer.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return file;
    }


}
