package com.dwbi.android.popularmovies.model;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by PSX on 11/17/2017.
 */

public class MovieContract  {
    public static final String CONTENT_AUTHORITY = "com.dwbi.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movie";
        // column names
        public static final String COLUMN_MOVIE_ID = "tmdb_movie_id";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_POSTERPATH = "posterpath";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";

    }
}
