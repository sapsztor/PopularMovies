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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;


import com.dwbi.android.popularmovies.model.Movie;
import com.dwbi.android.popularmovies.model.MovieContract;
import com.dwbi.android.popularmovies.model.Trailer;
import com.dwbi.android.popularmovies.utilities.FavoriteQueryLoader;
import com.dwbi.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "PSX";
    
    public static final int TRAILER_LOADER_ID = 3;
    
    private LoaderManager.LoaderCallbacks<ArrayList<Trailer>> loaderCallbacks = new LoaderCallback();
    
    GridView gv_trailer;
    
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        //setContentView(R.layout.activity_detail);
        setContentView(R.layout.activity_detail3);

        TextView tv_title;
        ImageView iv_poster;
        TextView tv_overview;
        TextView tv_vote_average;
        TextView tv_release_date;
        
        final ToggleButton btn_favorite_toggle;
        
        


        //noinspection RedundantCast
        tv_title = (TextView) findViewById(R.id.tv_title);
        //noinspection RedundantCast
        iv_poster = (ImageView) findViewById(R.id.iv_poster);
        //noinspection RedundantCast
        tv_overview = (TextView) findViewById(R.id.tv_overview);
        //noinspection RedundantCast
        tv_vote_average = (TextView) findViewById(R.id.tv_vote_average);
        //noinspection RedundantCast
        tv_release_date = (TextView) findViewById(R.id.tv_release_date);
        
        btn_favorite_toggle = (ToggleButton) findViewById(R.id.btn_favorite_toggle);
        
        gv_trailer = (GridView) findViewById(R.id.gv_videos);
        
        
        

        Intent intent = getIntent();
        final Movie movie = intent.getExtras().getParcelable(getString(R.string.extra_selectedmovie));

        String tmdbID = movie.getId();
        String posterpath = movie.getPosterPath();
        String title = movie.getTitle();
        String overview = movie.getOverview();
        String vote_average = movie.getVote_Average();
        String release_date = movie.getRelease_Date().substring(0,4);

        tv_title.setText(title);
        tv_overview.setText(overview);
        tv_vote_average.setText(vote_average);
        tv_release_date.setText(release_date);
    
        if (checkFavorite(movie)) {
            btn_favorite_toggle.setChecked(true);
        } else {
            btn_favorite_toggle.setChecked(false);
        }
        
        // TODO: delete debug
        //dumpDB();
        
        Log.d(TAG, "DetailActivity.onCreate tmdbID-> " + tmdbID);
        startQuery(tmdbID);
        
        

        Picasso.with(this)
                .load(NetworkUtils.buildQueryPoster(posterpath))
                .placeholder(R.drawable.poster_placeholder)
                .into(iv_poster);
    
        
        
        btn_favorite_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkFavorite(movie)) {
                    saveFavorite(movie);
                    btn_favorite_toggle.setChecked(true);
                } else {
                    deleteFavorite(movie);
                    btn_favorite_toggle.setChecked(false);
                }
        
                //dumpDB();
            }
        });
        
        //gv_trailer.setAdapter(new TrailersAdapter(this, ));
        
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
        boolean movie_is_favorite = false;
        
    
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(tmdbID).build();
    
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                cursor.close();
                movie_is_favorite = true;
            } else {
                movie_is_favorite = false;
            }
        } finally {
            cursor.close();
        }
        
        return movie_is_favorite;
    }
    //----------------------------------------------------------------------------------------------
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
    
    private class LoaderCallback implements LoaderManager.LoaderCallbacks<ArrayList<Trailer>> {
        //------------------------------------------------------------------------------------------
        @Override
        public Loader<ArrayList<Trailer>> onCreateLoader(int id, final Bundle args) {
            if(args == null) {
                return null;
            }
            String movieId = args.getString("movieId");
            
            
            TMDBTrailerLoader loader = new TMDBTrailerLoader(DetailActivity.this, movieId);
            
            return loader;
        }
        //------------------------------------------------------------------------------------------
        @Override
        public void onLoadFinished(Loader<ArrayList<Trailer>> loader, ArrayList<Trailer> data) {
            if (data == null) {
            } else {
//                for(Trailer t: data){
//                    Log.d("PSX", "DetailActivity TrailerLoader onLoadFinished data.name, data.key-> "+ t.getType() + ", " + t.getName() + ", " + t.getName() + ", " + t.getKey());
//                }
                processResponse(data);
            }
        }
        //------------------------------------------------------------------------------------------
        @Override
        public void onLoaderReset(Loader<ArrayList<Trailer>> loader) {
        
        }
        //------------------------------------------------------------------------------------------
        //------------------------------------------------------------------------------------------
    }
    
    //----------------------------------------------------------------------------------------------
    private void startQuery(String movieId){
        
        Log.d("PSX", "DetailActivity startQuery movieId-> " + movieId);
        
            
            //setAdapter();
//            pb_loading_indicator =  findViewById(R.id.pb_loading_indicator);
//            pb_loading_indicator.setVisibility(View.VISIBLE);
        // TODO:  put checkInternetConnection to NetworkUtils class
        //checkInternetConnection();
        Bundle loadBundle = new Bundle();
        loadBundle.putString("movieId", movieId);
        
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<ArrayList<Trailer>> movieLoader = loaderManager.getLoader(TRAILER_LOADER_ID);
        loaderManager.restartLoader(TRAILER_LOADER_ID, loadBundle, loaderCallbacks);

    }
    //----------------------------------------------------------------------------------------------
    public void processResponse(ArrayList<Trailer> response) {
        //gv_trailer.setAdapter(new TrailersAdapter(this, response));
        gv_trailer = (GridView) findViewById(R.id.gv_videos);
        gv_trailer.setAdapter(new TrailersAdapter(this, response));
        //TrailersAdapter ta = new TrailersAdapter(this, response);
        
        for(Trailer t: response){
            Log.d("PSX", "DetailActivity processResponse response.name, response.key-> "+ t.getType() + ", " + t.getName() + ", " + t.getName() + ", " + t.getKey());
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
