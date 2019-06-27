package com.orlinskas.bookread.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class BookBodyFileReader {

    public ArrayList<String> read (File file) throws Exception {
        BufferedReader bufferedReader;
        bufferedReader = new BufferedReader(new FileReader(file));

        ArrayList<String> words = new ArrayList<>();
        StringBuilder word = new StringBuilder();

        int c;
        while ((c = bufferedReader.read()) != -1) {
            if ((char) c == ' '){
                if(word.length() > 0) {
                    words.add(word.toString());
                    words.add(" ");
                }
                else {
                    words.add(" ");
                }
                word = new StringBuilder();
            }
            else if (c == 10){
                if(word.length() > 0) {
                    words.add(word.toString());
                    words.add("\n");
                }
                else {
                    words.add("\n");
                }
                word = new StringBuilder();
            }
            else if ((char) c == ','){
                if(word.length() > 0) {
                    words.add(word.toString());
                    words.add(",");
                }
                else {
                    words.add(",");
                }
                word = new StringBuilder();
            }
            else if ((char) c == '.'){
                if(word.length() > 0) {
                    words.add(word.toString());
                    words.add(".");
                }
                else {
                    words.add(".");
                }
                word = new StringBuilder();
            }
            else if ((char) c == '!'){
                if(word.length() > 0) {
                    words.add(word.toString());
                    words.add("!");
                }
                else {
                    words.add("!");
                }
                word = new StringBuilder();
            }
            else {
                word.append((char) c);
            }
        }
        bufferedReader.close();

        return words;
    }

}
