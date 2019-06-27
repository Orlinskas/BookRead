package com.orlinskas.bookread.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WordsDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private String tableName;
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_RUSSIAN = "original";
    public static final String COLUMN_ENGLISH = "translate";
    public static final String COLUMN_COUNT = "count";

    public WordsDatabase(Context context, String tableName) {
        super(context, tableName + ".db", null, DATABASE_VERSION);
        this.tableName = tableName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE_WORDS = "CREATE TABLE " + tableName + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_RUSSIAN + " TEXT NOT NULL, "
                + COLUMN_ENGLISH + " TEXT , "
                + COLUMN_COUNT + " INTEGER NOT NULL DEFAULT 1 );";

        db.execSQL(SQL_CREATE_TABLE_WORDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        onCreate(db);
    }
}

