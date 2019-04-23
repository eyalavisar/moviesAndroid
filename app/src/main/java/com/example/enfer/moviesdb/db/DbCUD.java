package com.example.enfer.moviesdb.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbCUD extends SQLiteOpenHelper {
    public DbCUD(Context context) {
        super(context, DbConstants.DATABASE_NAME, null, DbConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + DbConstants.TABLE_MOVIES + "("
                + DbConstants._ID + " INTEGER PRIMARY KEY," + DbConstants.TITLE + " TEXT,"
                + DbConstants.OVERVIEW + " TEXT," + DbConstants.URLIMAGE + " TEXT,"
                + DbConstants.RATING + " INTEGER," + DbConstants.WATCHED + " INTEGER"
                + ")"; //+ DbConstants.XINDEX + "INTEGER"
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + DbConstants.TABLE_MOVIES);

        // Create tables again
        onCreate(db);
    }

}
