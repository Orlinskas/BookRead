package com.orlinskas.bookread.helpers;

import android.content.Context;

import com.orlinskas.bookread.Book;

import java.io.File;
import java.io.FileWriter;

public class BookBodyFileWriter {

    public File write (Context context, Book book, String body) {
        String clearAuthorName = book.getBookTitle();
        clearAuthorName = clearAuthorName.replaceAll("\\s","");

        String fileName = clearAuthorName + book.getDate() + ".txt";
        String filePath = context.getFilesDir().getPath() + "/" + fileName;
        File file = new File(filePath);
        try (FileWriter writer = new FileWriter(file, true)) {
            // запись всей строки
            writer.write(body);
            writer.flush();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return file;
    }
}
