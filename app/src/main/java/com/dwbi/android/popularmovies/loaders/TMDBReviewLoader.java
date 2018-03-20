package com.dwbi.android.popularmovies.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;


import com.dwbi.android.popularmovies.model.Review;
import com.dwbi.android.popularmovies.model.Reviews;
import com.dwbi.android.popularmovies.utilities.TMDBAPI;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by PSX on 3/16/2018.
 */

public class TMDBReviewLoader extends AsyncTaskLoader<ArrayList<Review>>{
    private static final String API_KEY = com.dwbi.android.popularmovies.BuildConfig.API_KEY;
    
    @SuppressWarnings("FieldCanBeLocal")
    private static final String BASE_URL = "http://api.themoviedb.org/";
    
    @SuppressWarnings("CanBeFinal")
    private String id;
    
    private ArrayList<Review> result = null;
    
    
    public TMDBReviewLoader(Context context, String id) {
        super(context);
        this.id = id;
    }
    
    @Override
    protected void onStartLoading() {
        if (result != null) {
            deliverResult(result);
        } else {
            forceLoad();
        }
    }
    
    @Override
    public void deliverResult(ArrayList<Review> data) {
        if (isReset()) {
            return;
        }
        result = data;
        if(isStarted()){
            super.deliverResult(data);
        }
        
    }
    
    
    @Override
    public ArrayList<Review> loadInBackground() {
        //----------------------- RETROFIT ---------------------------------------------------------
        
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        
        TMDBAPI tmdbapi = retrofit.create(TMDBAPI.class);
        
        Call<Reviews> call = tmdbapi.getReviews(id, API_KEY);
        
        try {
            Response<Reviews> response = call.execute();
            Reviews reviews = response.body();
            //noinspection ConstantConditions
            return (ArrayList<Review>) reviews.getReviews();
        } catch (IOException e) {
            Log.d("PSX", "TMDBReviewLoader e-> " + e);
        }
        //----------------------- RETROFIT ---------------------------------------------------------
        
        return null;
        
    }
}
