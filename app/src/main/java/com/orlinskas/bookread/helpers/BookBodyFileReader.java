package com.orlinskas.bookread.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class BookBodyFileReader {

    public ArrayList<String> read (File file) {
        BufferedReader bufferedReader = null;
        ArrayList<String> words = new ArrayList<>();
        StringBuilder word = new StringBuilder();

        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            int c;
            while ((c = bufferedReader.read()) != -1) {

                if ((char) c == ' '){
                    word.append((char) c);
                    words.add(word.toString());
                    word = new StringBuilder();
                }
                else {
                    word.append((char) c);
                }
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

        return words;
    }

}
