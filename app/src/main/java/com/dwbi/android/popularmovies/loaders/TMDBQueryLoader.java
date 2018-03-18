package com.dwbi.android.popularmovies.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.dwbi.android.popularmovies.model.Movie;
import com.dwbi.android.popularmovies.model.Movies;
import com.dwbi.android.popularmovies.utilities.TMDBAPI;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by PSX on 10/27/2017.
 */
// http://www.androiddesignpatterns.com/2012/08/implementing-loaders.html

public class TMDBQueryLoader extends AsyncTaskLoader<ArrayList<Movie>> {
    private static final String API_KEY = com.dwbi.android.popularmovies.BuildConfig.API_KEY;
    
    private static String BASE_URL = "http://api.themoviedb.org/";

   
    String sortBy;
    String pageNum;

    ArrayList<Movie> result = null;

    public TMDBQueryLoader(Context context, String sortBy, String pageNum) {
        super(context);
        this.sortBy = sortBy;
        this.pageNum = pageNum;
    }




    @Override
    protected void onStartLoading() {
        //super.onStartLoading();

        if (result != null) {
            deliverResult(result);
        } else {
            forceLoad();
        }
    }


    @Override
    public void deliverResult(ArrayList<Movie> data) {
        if (isReset()) {
            return;
        }
        result = data;
        if(isStarted()){
            super.deliverResult(data);
        }

    }

    @Override
    public ArrayList<Movie> loadInBackground() {
        //----------------------- RETROFIT ---------------------------------------------------------

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TMDBAPI tmdbapi = retrofit.create(TMDBAPI.class);

        Call<Movies> call = tmdbapi.getMovies(sortBy, pageNum, API_KEY);

        try {
            Response<Movies> response = call.execute();
            Movies movies = response.body();
            return (ArrayList<Movie>) movies.getMovies();
        } catch (IOException e) {
            Log.d("PSX", "e-> " + e);
        }
        //----------------------- RETROFIT ---------------------------------------------------------

        return null;

    }

}
