package com.orlinskas.bookread.bookReadAlgorithm;

import android.widget.ArrayAdapter;

import com.orlinskas.bookread.Word;

import java.util.ArrayList;

public class WordReplacer {
    public String replace(Word needWord, ArrayList<Word> words) {
        for(Word word : words) {
            if(word.equals(needWord)) {
                return word.getEnglish();
            }
        }
        return needWord.getRussian();
    }
}
