package com.orlinskas.bookread.bookReadAlgorithm;

import com.orlinskas.bookread.Word;

public class WordHandler {

    private static final int MIN_NEED_LENGTH = 3;
    private static final int MIN_NEED_COUNT = 4;

    public boolean processRussian(Word word) {
        return isLength(word.getRussian()) & !isUpperCase(word.getRussian()) & isCountRepeat(word.getCount());
    }

    private boolean isLength(String word) {
        return word.length() > MIN_NEED_LENGTH;
    }

    private boolean isUpperCase(String word) {
        char[] wordLetters = word.toCharArray();
        char[] upperLetters = "ЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮЁ".toCharArray();

        for(Character c : upperLetters){
            if(wordLetters[0] == c){
                return true;
            }
        }
        return false;
    }

    private boolean isCountRepeat(int count) {
        return count > MIN_NEED_COUNT;
    }
}
