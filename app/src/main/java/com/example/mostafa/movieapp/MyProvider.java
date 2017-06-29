package com.example.mostafa.movieapp;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Mostafa on 3/5/2017.
 */

public class MyProvider extends ContentProvider {
    public static final int AllMovies=100;
    public static final int OneMovies=100;
    private static final UriMatcher sUriMatcher=BuildMactcher();
    private FavoriteDataBase favoriteDataBase;
    public static UriMatcher BuildMactcher()
    {
        UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.Authority,MovieContract.PATH_MOVIES,AllMovies);
        //uriMatcher.addURI(MovieContract.Authority,MovieContract.PATH_MOVIES+"/#",OneMovies);// for table (all movies)
        return uriMatcher;
    }
    @Override
    public boolean onCreate() {
        Context context=getContext();
        favoriteDataBase=new FavoriteDataBase(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        final SQLiteDatabase db=favoriteDataBase.getReadableDatabase();
        Cursor cursor=null;
        cursor=db.query("favorite",strings,s,strings1,null,null,null);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }
    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db=favoriteDataBase.getWritableDatabase();
        Uri returnURI = null;
        long id=db.insert("favorite",null,contentValues);
        if(id>0) // success
        {
            String s=contentValues.getAsString("Title");
            returnURI= ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI,id);
        }
        else
        {
            Toast.makeText(getContext(),"Failed to inset",Toast.LENGTH_SHORT).show();
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnURI;
    }
    public int Is_favorite(String Title,Uri uri)
    {
        SQLiteDatabase db=favoriteDataBase.getReadableDatabase();
        String title=uri.getPathSegments().get(1);
        String select="Title=?";
        String []selectArgs=new String[]{title};
        Cursor cursor=db.query("favorite",null,select,selectArgs,null,null,null);
        return cursor.getCount();
    }
    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase db=favoriteDataBase.getWritableDatabase();
        int MovieDel=0;
        String title=uri.getPathSegments().get(1);
        MovieDel=db.delete("favorite","Title = ?",new String[]{title});

        if (MovieDel != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return MovieDel;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
