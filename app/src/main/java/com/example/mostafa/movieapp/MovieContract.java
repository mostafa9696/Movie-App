package com.example.mostafa.movieapp;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Mostafa on 3/5/2017.
 */


class MovieContract {
    public static final String Authority="com.example.mostafa.movieapp";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+Authority);
    public static final String PATH_MOVIES="favorite";
    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI=
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_NAME = "Title";
    }
}
