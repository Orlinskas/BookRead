package com.orlinskas.bookread.bookReadAlgorithm;

import android.content.Context;

import com.orlinskas.bookread.Word;
import com.orlinskas.bookread.data.DatabaseAdapter;

public class WordDataWriter {
    private String tableName;
    private Context context;

    public WordDataWriter(String tableName, Context context) {
        this.tableName = tableName;
        this.context = context;
    }


    public void containWordsInDatabase(String word) {
        try {
            Word wordData = new Word();
            wordData.setOriginal(word);

            if (wordData.getOriginal().length() > 3) {//можно проверить ворд хэндлером WordHandler
                DatabaseAdapter databaseAdapter = new DatabaseAdapter(context, tableName);
                databaseAdapter.open();

                if (databaseAdapter.isContain(wordData, tableName)) {
                    wordData = databaseAdapter.getWord(wordData, tableName);
                    wordData.setCount(wordData.getCount() + 1);
                    databaseAdapter.update(wordData, tableName);
                } else {
                    databaseAdapter.insert(wordData, tableName);
                }
                databaseAdapter.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
