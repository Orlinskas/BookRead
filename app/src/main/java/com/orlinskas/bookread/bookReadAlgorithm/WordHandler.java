package com.orlinskas.bookread.bookReadAlgorithm;

public class WordHandler {

    public String process(String word) {
        if(processLength(word)) {
            return word;
        }
        else {
            return null;
        }
    }

    private boolean processLength(String word) {
        return word.length() > 3;
    }
}
