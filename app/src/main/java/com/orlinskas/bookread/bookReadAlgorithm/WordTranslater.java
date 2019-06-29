package com.orlinskas.bookread.bookReadAlgorithm;

import android.content.Context;

import com.orlinskas.bookread.Word;
import com.orlinskas.bookread.data.DatabaseAdapter;

import java.util.ArrayList;

public class WordTranslater {
    private Context context;
    private String tableName;

    public WordTranslater(Context context, String tableName) {
        this.context = context;
        this.tableName = tableName;
    }

    public boolean checkNeedTranslate() {
        ArrayList<Word> words = getWords(tableName);
        if(words.get(1).getEnglish() == null){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean go() {
        ArrayList<Word> words = getWords(tableName);
        if(translate(words)){
            return transactionToData(words);
        }
        return false;
    }

    private ArrayList<Word> getWords(String tableName) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(context, tableName);
        databaseAdapter.openWithTransaction();
        ArrayList<Word> words = databaseAdapter.getWords(tableName);
        databaseAdapter.closeWithTransaction();
        return words;
    }

    private boolean translate(ArrayList<Word> words) {
        //перевод тут апи
        for(Word word : words) {
            word.setEnglish("CookHero");
        }
        return true;
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
