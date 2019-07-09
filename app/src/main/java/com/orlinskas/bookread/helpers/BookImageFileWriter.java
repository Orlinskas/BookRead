package com.orlinskas.bookread.helpers;

import android.content.Context;

import java.io.File;
import java.io.FileWriter;

public class BookImageFileWriter {

    public File write (Context context, String bookTitle, String bookDate, String base64) {
        String clearAuthorName = bookTitle;
        clearAuthorName = clearAuthorName.replaceAll("\\s","");

        String fileName = "im" + clearAuthorName + bookDate + ".jpg";
        String filePath = context.getFilesDir().getPath() + "/" + fileName;
        File file = new File(filePath);
        try (FileWriter writer = new FileWriter(file, true)) {
            // запись всей строки
            writer.write(base64);
            writer.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return file;
    }
}
