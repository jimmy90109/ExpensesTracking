package com.example.expensestracking;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StdDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ExpensesData";
    private static final int DATABASE_VERSION = 1;
    public StdDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE expenses (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "yyyymm text not null, " +
                "date text not null, " +
                "detail text not null, " +
                "money real not null)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS expenses");
        onCreate(db);
    }
}