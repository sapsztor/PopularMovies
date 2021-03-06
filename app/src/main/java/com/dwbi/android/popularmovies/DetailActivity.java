package com.dwbi.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;


import com.dwbi.android.popularmovies.adapters.ReviewsAdapter;
import com.dwbi.android.popularmovies.adapters.TrailersAdapter;
import com.dwbi.android.popularmovies.loaders.TMDBReviewLoader;
import com.dwbi.android.popularmovies.loaders.TMDBTrailerLoader;
import com.dwbi.android.popularmovies.model.Movie;
import com.dwbi.android.popularmovies.model.MovieContract;
import com.dwbi.android.popularmovies.model.Review;
import com.dwbi.android.popularmovies.model.Trailer;
import com.dwbi.android.popularmovies.utilities.ExpandableGridView;
import com.dwbi.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "PSX";
    
    private static final int TRAILER_LOADER_ID = 3;
    private static final int REVIEW_LOADER_ID = 4;
    
    private final LoaderManager.LoaderCallbacks<ArrayList<Trailer>> trailersLoaderCallback = new TrailersLoaderCallback();
    private final LoaderManager.LoaderCallbacks<ArrayList<Review>> reviewsLoaderCallbacks = new ReviewsLoaderCallback();
    
    //GridView gvTrailer;
    private ExpandableGridView gvTrailer;
    private ExpandableGridView gvReview;
    
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_detail);

        TextView tvTitle;
        ImageView ivPoster;
        TextView tvOverview;
        TextView tvVoteAverage;
        TextView tvReleaseDate;
        
        final ToggleButton btnFavoriteToggle;
        
        


        //noinspection RedundantCast
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        //noinspection RedundantCast
        ivPoster = (ImageView) findViewById(R.id.ivPoster);
        //noinspection RedundantCast
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        //noinspection RedundantCast
        tvVoteAverage = (TextView) findViewById(R.id.tvVoteAverage);
        //noinspection RedundantCast
        tvReleaseDate = (TextView) findViewById(R.id.tvReleaseDate);
        
        btnFavoriteToggle = findViewById(R.id.btnFavoriteToggle);
        
        //gvTrailer = (GridView) findViewById(R.id.gv_videos);
        gvTrailer = findViewById(R.id.gvTrailers);
        gvReview = findViewById(R.id.gvReviews);
        
        
        
        

        Intent intent = getIntent();
        final Movie movie ;
        if(intent.hasExtra(getString(R.string.extra_selectedmovie))){
            //noinspection ConstantConditions
            movie = intent.getExtras().getParcelable(getString(R.string.extra_selectedmovie));
        } else {
            return;
        }
        

        @SuppressWarnings("ConstantConditions")
        String tmdbID = movie.getId();
        String posterpath = movie.getPosterPath();
        String title = movie.getTitle();
        String overview = movie.getOverview();
        String vote_average = movie.getVote_Average();
        String release_date = movie.getRelease_Date().substring(0,4);

        tvTitle.setText(title);
        tvOverview.setText(overview);
        tvVoteAverage.setText(vote_average);
        tvReleaseDate.setText(release_date);
    
        if (checkFavorite(movie)) {
            btnFavoriteToggle.setChecked(true);
        } else {
            btnFavoriteToggle.setChecked(false);
        }
        
       
        Log.d(TAG, "DetailActivity.onCreate tmdbID-> " + tmdbID);
        
        
        startQuery(tmdbID);
        
        

        Picasso.with(this)
                .load(NetworkUtils.buildQueryPoster(posterpath))
                .placeholder(R.drawable.poster_placeholder)
                .into(ivPoster);
    
        
        
        btnFavoriteToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkFavorite(movie)) {
                    saveFavorite(movie);
                    btnFavoriteToggle.setChecked(true);
                } else {
                    deleteFavorite(movie);
                    btnFavoriteToggle.setChecked(false);
                }
        
                //dumpDB();
            }
        });
    }

    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    //--- DB functions
    //----------------------------------------------------------------------------------------------
    private void saveFavorite(Movie movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTERPATH, movie.getPosterPath());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, movie.getVote_Average());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getRelease_Date());
        getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
    }
    //----------------------------------------------------------------------------------------------
    private void deleteFavorite(Movie movie) {
        String selection = MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
        String[] selectionArgs = {String.valueOf(movie.getId())};
        getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, selection, selectionArgs);
    }
    //----------------------------------------------------------------------------------------------
    private boolean checkFavorite(Movie movie) {
        String tmdbID = movie.getId();
        boolean movie_is_favorite;
        
    
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(tmdbID).build();
    
        try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
            //noinspection ConstantConditions
            if (cursor.moveToFirst()) {
                cursor.close();
                movie_is_favorite = true;
            } else {
                movie_is_favorite = false;
            }
        }
        
        return movie_is_favorite;
    }
    //----------------------------------------------------------------------------------------------
    @SuppressWarnings("unused")
    private void dumpDB() {
        Log.d("PSX", "dumpDB");
        String[] projection = {MovieContract.MovieEntry.COLUMN_MOVIE_ID, MovieContract.MovieEntry.COLUMN_MOVIE_TITLE};
        Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, projection, null, null, null);
        if(cursor != null){
            Log.d("PSX", "dumpDB->cursor is not null");
            while (cursor.moveToNext()) {
                String tmdbMovieID = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
                String title = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE));
                Log.d("PSX", "dumpDB.COLUMN_MOVIE_ID-> " + tmdbMovieID + " COLUMN_MOVIE_TITLE-> " + title);
            }
        } else {
            Log.d("PSX", "dumpDB-> cursor is null");
        }
        if (cursor != null) {
            cursor.close();
        }
    }
    //----------------------------------------------------------------------------------------------
    
    private class TrailersLoaderCallback implements LoaderManager.LoaderCallbacks<ArrayList<Trailer>> {
        //------------------------------------------------------------------------------------------
        @Override
        public Loader<ArrayList<Trailer>> onCreateLoader(int id, final Bundle args) {
            if(args == null) {
                return null;
            }
            String movieId = args.getString("movieId");
    
    
            return new TMDBTrailerLoader(DetailActivity.this, movieId);
        }
        //------------------------------------------------------------------------------------------
        @Override
        public void onLoadFinished(Loader<ArrayList<Trailer>> loader, ArrayList<Trailer> data) {
            if (data != null) {
                trailerLoaderResponse(data);
            }
        }
        //------------------------------------------------------------------------------------------
        @Override
        public void onLoaderReset(Loader<ArrayList<Trailer>> loader) {
        
        }
        //------------------------------------------------------------------------------------------
        //------------------------------------------------------------------------------------------
    }
    
    private class ReviewsLoaderCallback implements LoaderManager.LoaderCallbacks<ArrayList<Review>> {
        //------------------------------------------------------------------------------------------
        @Override
        public Loader<ArrayList<Review>> onCreateLoader(int id, final Bundle args) {
            if(args == null) {
                return null;
            }
            String movieId = args.getString("movieId");
    
    
            return new TMDBReviewLoader(DetailActivity.this, movieId);
        }
        //------------------------------------------------------------------------------------------
        @Override
        public void onLoadFinished(Loader<ArrayList<Review>> loader, ArrayList<Review> data) {
            if (data != null) {
                reviewLoaderResponse(data);
            }
        }
        //------------------------------------------------------------------------------------------
        @Override
        public void onLoaderReset(Loader<ArrayList<Review>> loader) {
        
        }

    }

    
    //----------------------------------------------------------------------------------------------
    private void startQuery(String movieId){
        
        Log.d("PSX", "DetailActivity startQuery movieId-> " + movieId);
        
            
        Bundle loadBundle = new Bundle();
        loadBundle.putString("movieId", movieId);
    
        LoaderManager loaderManager = getSupportLoaderManager();
        //Loader<ArrayList<Trailer>> movieLoader = loaderManager.getLoader(TRAILER_LOADER_ID);
        loaderManager.restartLoader(TRAILER_LOADER_ID, loadBundle, trailersLoaderCallback);

        //Loader<ArrayList<Review>> reviewLoader = loaderManager.getLoader(REVIEW_LOADER_ID);
        loaderManager.restartLoader(REVIEW_LOADER_ID, loadBundle, reviewsLoaderCallbacks);

    }
    //----------------------------------------------------------------------------------------------
    private void trailerLoaderResponse(ArrayList<Trailer> response) {
        gvTrailer = findViewById(R.id.gvTrailers);
        if(response != null) {
            gvTrailer.setAdapter(new TrailersAdapter(this, response));
            gvTrailer.setExpanded(true);
        }
    }
    //----------------------------------------------------------------------------------------------
    private void reviewLoaderResponse(ArrayList<Review> response) {
        gvReview = findViewById(R.id.gvReviews);
        if(response != null) {
            gvReview.setAdapter(new ReviewsAdapter(this, response));
            gvReview.setExpanded(true);
            
        }
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d("PSX", "DetailActivity.onOptionsItemSelected");
        }

        return super.onOptionsItemSelected(item);
    }
}
