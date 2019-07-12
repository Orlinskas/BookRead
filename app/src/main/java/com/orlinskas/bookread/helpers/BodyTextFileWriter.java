package com.orlinskas.bookread.helpers;

import android.content.Context;

import com.orlinskas.bookread.Book;

import java.io.File;
import java.io.FileWriter;
/**
 * @author Orlinskas
 * @version 2
 */
public class BodyTextFileWriter {

    public File write (Context context, Book book, String bodyText) {
        String cleanTitleName = book.getTitle();
        cleanTitleName = cleanTitleName.replaceAll("\\s","");

        String fileName = cleanTitleName + book.getCreateDate() + ".txt";
        String filePath = context.getFilesDir().getPath() + "/" + fileName;
        File file = new File(filePath);
        try (FileWriter writer = new FileWriter(file, true)) {
            // запись всей строки
            writer.write(bodyText);
            writer.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return file;
    }
}
