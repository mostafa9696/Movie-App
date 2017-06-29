package com.example.mostafa.movieapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mostafa on 3/5/2017.
 */

public class FavoriteDataBase extends SQLiteOpenHelper {
    private static final String DataBaseName="Favorite.db";
    private static final int version=1;
    FavoriteDataBase(Context context)
    {
        super(context, DataBaseName, null, version);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table favorite ( id INTEGER PRIMARY KEY AUTOINCREMENT , Title Text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS favorite");
        onCreate(sqLiteDatabase);
    }
}
