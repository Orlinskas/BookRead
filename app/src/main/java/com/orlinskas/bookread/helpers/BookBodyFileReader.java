package com.orlinskas.bookread.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class BookBodyFileReader {
    private Character[] characters = new Character[]{
            ' ', '\n', '.', ',', '!', '&', '?', ':', ';',
            '@', '#', '$', '%', '*', '(', ')', '-'/*минус*/, '‐'/*дефиз*/,
            '–'/*короткое тире*/, '—'/*длинное тире*/, '+', '«', '»'
    };

    private ArrayList<String> words = new ArrayList<>();
    private StringBuilder word = new StringBuilder();

    public ArrayList<String> read(File file) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        int c;

        while ((c = bufferedReader.read()) != -1) {
            char charValue = (char) c;
            String stringValue = String.valueOf(charValue);

            if (Arrays.asList(characters).contains(charValue)) {
                separateWord(stringValue);
            } else {
                word.append(charValue);
            }
        }

        bufferedReader.close();

        return words;
    }

    private void separateWord(String character) {
        if (word.length() > 0) {
            words.add(word.toString());
        }

        words.add(character);
        this.word = new StringBuilder();
    }
}
