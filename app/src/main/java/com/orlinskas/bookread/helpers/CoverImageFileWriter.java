package com.orlinskas.bookread.helpers;

import android.content.Context;

import java.io.File;
import java.io.FileWriter;
/**
 * @author Orlinskas
 * @version 2
 */
public class CoverImageFileWriter {

    public File write (Context context, String bookTitle, String bookDate, String base64) {
        String cleanTitleName = bookTitle;
        cleanTitleName = cleanTitleName.replaceAll("\\s","");

        String fileName = "im" + cleanTitleName + bookDate + ".jpg";
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
