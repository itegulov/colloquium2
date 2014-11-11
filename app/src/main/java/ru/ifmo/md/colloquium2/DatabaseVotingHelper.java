package ru.ifmo.md.colloquium2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DatabaseVotingHelper extends SQLiteOpenHelper implements BaseColumns {
    public static final String DATABASE_NAME = "voting_data";
    public static final int DATABASE_VERSION = 1;

    //Candidates table
    public static final String CANDIDATES_TABLE_NAME = "candidates";
    public static final String CANDIDATES_ID = _ID;
    public static final String CANDIDATES_NAME = "name";
    public static final String CANDIDATES_VOTES = "votes";

    public static final String CANDIDATES_TABLE_CREATE_REQUEST = "CREATE TABLE " + CANDIDATES_TABLE_NAME + " (" +
            CANDIDATES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CANDIDATES_NAME + " TEXT, " +
            CANDIDATES_VOTES + " INTEGER)";


    public DatabaseVotingHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CANDIDATES_TABLE_CREATE_REQUEST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CANDIDATES_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onOpen(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("PRAGMA foreign_keys=ON");
    }
}
