package com.orlinskas.bookread.bookReadAlgorithm;

import com.orlinskas.bookread.Word;

import java.util.ArrayList;

public class WordsHandler {
    public ArrayList<Word> process(ArrayList<String> words) {
        int countWords = (int) (words.size() * 0.03);

        ArrayList<Word> wordsExclusive = new ArrayList<>();
        for(String w : words) {
            if (w.length() > 3) {
                Word word = new Word();
                word.setRussian(w);

                if(wordsExclusive.contains(word)){
                    for (Word wExclusive : wordsExclusive) {
                        if(wExclusive.equals(word)){
                            wExclusive.setCount(wExclusive.getCount() + 1);
                            break;
                        }
                    }
                }
                else {
                    if(wordsExclusive.size() < countWords) {
                        wordsExclusive.add(word);
                    }
                    else {
                        return wordsExclusive;
                    }
                }
            }
        }
        return wordsExclusive;
    }
}
