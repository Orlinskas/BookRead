package com.orlinskas.bookread.helpers;

import android.content.Context;

import com.orlinskas.bookread.data.BookFilesData;
import com.orlinskas.bookread.data.LicenceData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class FileHelper {

    public File createFile (Context context, String body) {
        String fileName = LicenceData.getCountOfBookCreate() + ".txt";
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

    public ArrayList<Character> readFile (File file) {
        ArrayList<Character> bodyChar = new ArrayList<>();
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            int c;
            while ((c = bufferedReader.read()) != -1) {
                bodyChar.add((char) c);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return bodyChar;
    }
}
