package com.example.rifi;

import android.content.Context;
import android.database.sqlite.*;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "census_2026.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE beneficiaries (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "program TEXT, name TEXT, address TEXT," +
                "elec INTEGER, gas INTEGER, water INTEGER, sanit INTEGER," +
                "status TEXT, completed INTEGER DEFAULT 0, image TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
