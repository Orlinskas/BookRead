package com.orlinskas.bookread.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class BookBodyFileReader {

    public String read (File file) {
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

        StringBuilder endText = new StringBuilder();
        for (String word : findWordsIn(bodyChar)){
            endText.append(word);
        }

        return endText.toString();
    }

    private ArrayList<String> findWordsIn (ArrayList<Character> bodyChar) {
        ArrayList<String> words = new ArrayList<>();
        StringBuilder word = new StringBuilder();

        for (Character ch : bodyChar){
            if (ch == ' '){
                word.append(ch);
                words.add(word.toString());
                word = new StringBuilder();
            }
            else {
                word.append(ch);
            }
        }

        return words;
    }
}
