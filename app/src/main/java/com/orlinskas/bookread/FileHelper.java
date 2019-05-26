package com.orlinskas.bookread;

import com.orlinskas.bookread.data.LicenceData;

import java.io.File;
import java.io.FileWriter;

public class FileHelper {

    public File createFile () {
        String fileName = LicenceData.getCountOfBookCreate() + ".txt";

        File bodyFile = new File(fileName);

        try(FileWriter writer = new FileWriter(bodyFile, true))
        {
            // запись всей строки
            String text = "Hello Gold!";
            writer.write(text);

            // запись по символам
            writer.append('\n');
            writer.append('E');

            writer.flush();
        }
        catch(Exception ex){

            System.out.println(ex.getMessage());
        }

        return bodyFile;
    }
}
