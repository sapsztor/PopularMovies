package com.dwbi.android.popularmovies.model;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by PSX on 11/20/2017.
 */

public class MovieProvider extends ContentProvider {

    private static final int FAVORITES = 100;
    private static final int FAVORITE_WITH_ID = 101;

    private MovieDbHelper dbHelper;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIES, FAVORITES);
        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/#", FAVORITE_WITH_ID);
        return matcher;
    }

    //----------------------------------------------------------------------------------------------
    @Override
    public boolean onCreate() {
        dbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        switch (uriMatcher.match(uri)) {
            case FAVORITES: {
                cursor = dbHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case FAVORITE_WITH_ID: {
                String paramMovieId = uri.getLastPathSegment();
                try {
                    @SuppressWarnings("unused") int checkMovieId = Integer.parseInt(paramMovieId);
                } catch (NumberFormatException e) {
                    System.out.println("Not valid TMDB movie ID: " + e);
                }
                String[] myArgs = new String[] {paramMovieId};
                cursor = dbHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        myArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        //noinspection ConstantConditions
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("getType is not implemented.");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case FAVORITES:
                long id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                } else {
                    db.close();
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                db.close();
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        //noinspection ConstantConditions
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        @SuppressWarnings("UnusedAssignment")
        int rowsDeleted = 0;
        switch (uriMatcher.match(uri) ) {
            case FAVORITES:
                if(selection == null) {
                    selection = "1";
                }
                rowsDeleted = dbHelper.getWritableDatabase().delete(MovieContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case FAVORITE_WITH_ID:
                String paramMovieId = uri.getLastPathSegment();
                try {
                    @SuppressWarnings("unused") int checkMovieId = Integer.parseInt(paramMovieId);
                } catch (NumberFormatException e) {
                    System.out.println("Not valid TMDB movie ID: " + e);
                }
                String[] myArgs = new String[] {paramMovieId};
                rowsDeleted = dbHelper.getWritableDatabase().delete(MovieContract.MovieEntry.TABLE_NAME,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        myArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            //noinspection ConstantConditions
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new RuntimeException("Update not implemented.");
    }
}
