package com.orlinskas.bookread.bookReadAlgorithm;

import com.orlinskas.bookread.Word;

import java.util.ArrayList;

public class WordsHandler {
    public ArrayList<Word> process(ArrayList<String> words) {
        int maxCountWords = words.size() / 2;
        int countWords = 0;

        ArrayList<Word> wordsExclusive = new ArrayList<>();
        WordHandler wordHandler = new WordHandler();

        for(String w : words) {
            if (w.length() > 4) {
                Word word = new Word();
                word.setRussian(w);
                countWords++;

                if(wordsExclusive.contains(word)){
                    for (Word wExclusive : wordsExclusive) {
                        if(wExclusive.equals(word)){
                            wExclusive.setCount(wExclusive.getCount() + 1);
                            break;
                        }
                    }
                }
                else {
                     if(countWords < maxCountWords) {
                         if (wordHandler.processRussian(word)) {
                             wordsExclusive.add(word);
                         }
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
