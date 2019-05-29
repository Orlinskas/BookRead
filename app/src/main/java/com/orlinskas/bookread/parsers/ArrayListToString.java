package com.orlinskas.bookread.parsers;

import java.util.ArrayList;

public class ArrayListToString {

    public static String parse (ArrayList<String> words){
        StringBuilder text = new StringBuilder();

        for (String word : words){
            text.append(word);
        }

        return text.toString();
    }
}
