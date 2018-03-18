package com.dwbi.android.popularmovies.loaders;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.dwbi.android.popularmovies.adapters.FavoriteMoviesAdapter;
import com.dwbi.android.popularmovies.model.MovieContract;

import static com.dwbi.android.popularmovies.MainActivity.FAVORITE_LOADER_ID;

/**
 * Created by PSX on 1/2/2018.
 */

public class FavoriteQueryLoader implements LoaderManager.LoaderCallbacks<Cursor> {
    
    private final Context context;
    private final FavoriteMoviesAdapter adapter;
    
    public FavoriteQueryLoader(Context context, FavoriteMoviesAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case FAVORITE_LOADER_ID:
                String[] projection = {
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                    MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
                    MovieContract.MovieEntry.COLUMN_MOVIE_POSTERPATH,
                    MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
                    MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE,
                    MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE
                };
                return new CursorLoader(context,
                    MovieContract.MovieEntry.CONTENT_URI,
                    projection,
                    null,
                    null,
                    null);
            default:
                throw new RuntimeException("Favorite loader failed: " + loaderId);
        }
    }
    
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }
    
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}