package com.dwbi.android.popularmovies.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by PSX on 11/17/2017.
 */

/*
    private final String id;
    private final String title;
    private final String posterpath;
    private final String overview;
    private final String vote_average;
    private final String release_date;
 */
public class MovieDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE =
                "CREATE table " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                        MovieContract.MovieEntry._ID                        + " integer primary key autoincrement, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID            + " integer not null, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_TITLE         + " text not null, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_POSTERPATH    + " text, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW      + " text, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE  + " text, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE  + " text, " +
                        " unique (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ") on conflict replace);";


        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
