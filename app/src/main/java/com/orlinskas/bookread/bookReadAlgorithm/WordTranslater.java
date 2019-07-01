package com.orlinskas.bookread.bookReadAlgorithm;

import android.content.Context;

import com.orlinskas.bookread.TranslaterRequestSender;
import com.orlinskas.bookread.Word;
import com.orlinskas.bookread.data.DatabaseAdapter;
import com.orlinskas.bookread.parsers.ParserJson;

import java.util.ArrayList;

public class WordTranslater {
    private Context context;
    private String tableName;
    private ArrayList<Word> words = new ArrayList<>();

    private static final int MAX_COUNT_SYMBOL = 1400;

    private StringBuilder alpha = new StringBuilder();
    private StringBuilder beta  = new StringBuilder();
    private StringBuilder gamma = new StringBuilder();
    private StringBuilder quatro = new StringBuilder();
    private StringBuilder close = new StringBuilder();

    public WordTranslater(Context context, String tableName) {
        this.context = context;
        this.tableName = tableName;
    }

    public boolean checkNeedTranslate() {
        ArrayList<Word> words = getWords(tableName);
        if(words.get(1).getEnglish() == null | words.get(1).getEnglish().equals("") | words.get(1).getEnglish().equals(" ")){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean go() {
        words = getWords(tableName);
        saveTranslate(respliter(sendToTranslate(spliter(words))));
        return transactionToData(words);
    }

    private ArrayList<Word> getWords(String tableName) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(context, tableName);
        databaseAdapter.openWithTransaction();
        ArrayList<Word> words = databaseAdapter.getWords(tableName);
        databaseAdapter.closeWithTransaction();
        return words;
    }

    private int spliter(ArrayList<Word> words) {
        String translationWord;
        int countOfSplit = 1;
        for (Word word : words) {
            switch (countOfSplit) {
                case 1:
                    translationWord = word.getRussian() + ". ";
                    alpha.append(translationWord);
                    if(alpha.toString().length() > MAX_COUNT_SYMBOL){
                        countOfSplit = 2;
                    }
                    break;
                case 2:
                    translationWord = word.getRussian() + ". ";
                    beta.append(translationWord);
                    if(beta.toString().length() > MAX_COUNT_SYMBOL){
                        countOfSplit = 3;
                    }
                    break;
                case 3:
                    translationWord = word.getRussian() + ". ";
                    gamma.append(translationWord);
                    if(gamma.toString().length() > MAX_COUNT_SYMBOL){
                        countOfSplit = 4;
                    }
                    break;
                case 4:
                    translationWord = word.getRussian() + ". ";
                    quatro.append(translationWord);
                    if(quatro.toString().length() > MAX_COUNT_SYMBOL){
                        countOfSplit = 5;
                    }
                    break;
                case 5:
                    translationWord = word.getRussian() + ". ";
                    close.append(translationWord);
                    break;
            }
        }
        return countOfSplit;
    }

    private String sendToTranslate(int countOfSplit) {
        StringBuilder translateWords = new StringBuilder();
        TranslaterRequestSender translaterRequestSender = new TranslaterRequestSender();
        ParserJson parserJson = new ParserJson();
        try {
            switch (countOfSplit) {
                case 1:
                    translateWords.append(parserJson.parse(translaterRequestSender.request(alpha.toString())));
                    break;
                case 2:
                    translateWords.append(parserJson.parse(translaterRequestSender.request(alpha.toString())));
                    translateWords.append(parserJson.parse(translaterRequestSender.request(beta.toString())));
                    break;
                case 3:
                    translateWords.append(parserJson.parse(translaterRequestSender.request(alpha.toString())));
                    translateWords.append(parserJson.parse(translaterRequestSender.request(beta.toString())));
                    translateWords.append(parserJson.parse(translaterRequestSender.request(gamma.toString())));
                    break;
                case 4:
                    translateWords.append(parserJson.parse(translaterRequestSender.request(alpha.toString())));
                    translateWords.append(parserJson.parse(translaterRequestSender.request(beta.toString())));
                    translateWords.append(parserJson.parse(translaterRequestSender.request(gamma.toString())));
                    translateWords.append(parserJson.parse(translaterRequestSender.request(quatro.toString())));
                    break;
                case 5:
                    translateWords.append(parserJson.parse(translaterRequestSender.request(alpha.toString())));
                    translateWords.append(parserJson.parse(translaterRequestSender.request(beta.toString())));
                    translateWords.append(parserJson.parse(translaterRequestSender.request(gamma.toString())));
                    translateWords.append(parserJson.parse(translaterRequestSender.request(quatro.toString())));
                    translateWords.append(parserJson.parse(translaterRequestSender.request(close.toString())));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return translateWords.toString();
    }

    private ArrayList<String> respliter (String words) {
        ArrayList<String> englishWords = new ArrayList<>();
        try {
            char wordsChar[] = words.toCharArray();
            StringBuilder translateWord = new StringBuilder();

            for (int i = 0; i < wordsChar.length; i++) {
                if(wordsChar[i] != '.' & wordsChar[i] != ' ' & wordsChar[i] != '?') {
                    translateWord.append(wordsChar[i]);
                }
                if(wordsChar[i] == '.') {
                    englishWords.add(translateWord.toString());
                    translateWord = new StringBuilder();
                }
                if(wordsChar[i] == ' ' & translateWord.length() > 0 & wordsChar[i - 1] != '.') {
                    englishWords.add(translateWord.toString());
                    translateWord.append(wordsChar[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return englishWords;
    }

    private void saveTranslate (ArrayList<String> translateWords) {
        for (int i = 0; i < words.size(); i++) {
            words.get(i).setEnglish(translateWords.get(i));
        }
    }

    private boolean transactionToData(ArrayList<Word> words) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(context, tableName);
        try {
            databaseAdapter.open();
            databaseAdapter.removeAll(tableName);
            databaseAdapter.close();
        } catch (Exception e) {
            e.printStackTrace();
            databaseAdapter.close();
        }

        try {
            databaseAdapter.openWithTransaction();
            for (Word word : words) {
                databaseAdapter.insert(word, tableName);
            }
        }
        catch (Exception e){
            return false;
        }
        finally {
            databaseAdapter.closeWithTransaction();
        }
        return true;
    }

}
