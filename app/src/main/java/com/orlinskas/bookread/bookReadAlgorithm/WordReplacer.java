package com.orlinskas.bookread.bookReadAlgorithm;


import com.orlinskas.bookread.Word;

import java.util.ArrayList;

public class WordReplacer {
    public String replace(Word needWord, ArrayList<Word> words) {
        for(Word word : words) {
            if(word.equals(needWord)) {
                return word.getTranslate();
            }
        }
        return needWord.getOriginal();
    }

    public String firstReplace(Word needWord, ArrayList<Word> words) {
        for(Word word : words) {
            if(word.equals(needWord)) {
                return word.getOriginal() + " (" + word.getTranslate() + ")";
            }
        }
        return needWord.getOriginal();
    }
}
