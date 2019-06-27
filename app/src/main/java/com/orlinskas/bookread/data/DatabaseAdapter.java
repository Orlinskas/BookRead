package com.orlinskas.bookread.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.provider.UserDictionary;

import com.orlinskas.bookread.Word;

public class DatabaseAdapter {
    private WordsDatabase dbHelper;
    private SQLiteDatabase database;

    public DatabaseAdapter(Context context, String tableName) {
        dbHelper = new WordsDatabase(context, tableName);
    }

    public DatabaseAdapter open(){
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public long insert(Word word, String tableName) {
        ContentValues cv = new ContentValues();
        cv.put(WordsDatabase.COLUMN_RUSSIAN, word.getRussian());
        cv.put(WordsDatabase.COLUMN_ENGLISH, word.getEnglish());
        return database.insert(tableName, null, cv);
    }

    public Word getWord(Word word, String tableName) {
        Cursor cursor = getAllEntries(tableName);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(WordsDatabase.COLUMN_ID));
                String russian = cursor.getString(cursor.getColumnIndex(WordsDatabase.COLUMN_RUSSIAN));
                String english = cursor.getString(cursor.getColumnIndex(WordsDatabase.COLUMN_ENGLISH));
                int count = cursor.getInt(cursor.getColumnIndex(WordsDatabase.COLUMN_COUNT));

                if (russian.equals(word.getRussian())) {
                    if (english == null) {
                        cursor.close();
                        return new Word(id, russian, count);
                    }
                    else {
                        cursor.close();
                        return new Word(id, russian, english, count);
                    }
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return new Word();
    }

    public boolean isContain(Word word, String tableName) {
        try {
            Cursor cursor = getAllEntries(tableName);

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(WordsDatabase.COLUMN_ID));
                    String russian = cursor.getString(cursor.getColumnIndex(WordsDatabase.COLUMN_RUSSIAN));
                    String english = cursor.getString(cursor.getColumnIndex(WordsDatabase.COLUMN_ENGLISH));
                    int count = cursor.getInt(cursor.getColumnIndex(WordsDatabase.COLUMN_COUNT));

                    if (russian.equals(word.getRussian())) {
                        cursor.close();
                        return true;
                    }
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public long update(Word word, String tableName){
        String whereClause = null;
        ContentValues cv = null;
        try {
            whereClause = WordsDatabase.COLUMN_ID + "=" + word.getId();
            cv = new ContentValues();
            cv.put(WordsDatabase.COLUMN_ID, word.getId());
            cv.put(WordsDatabase.COLUMN_RUSSIAN, word.getRussian());
            cv.put(WordsDatabase.COLUMN_ENGLISH, word.getEnglish());
            cv.put(WordsDatabase.COLUMN_COUNT, word.getCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return database.update(tableName, cv, whereClause, null);
    }

    private Cursor getAllEntries(String tableName){
        String[] columns = new String[] {WordsDatabase.COLUMN_ID, WordsDatabase.COLUMN_RUSSIAN, WordsDatabase.COLUMN_ENGLISH, WordsDatabase.COLUMN_COUNT};
        return  database.query(tableName, columns, null, null, null, null, null);
    }

    public long getCount(String tableName){
        return DatabaseUtils.queryNumEntries(database, tableName);
    }

}