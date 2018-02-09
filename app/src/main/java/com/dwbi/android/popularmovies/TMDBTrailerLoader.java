package com.dwbi.android.popularmovies;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;


import com.dwbi.android.popularmovies.model.Trailer;
import com.dwbi.android.popularmovies.model.Trailers;
import com.dwbi.android.popularmovies.utilities.TMDBAPI;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by PSX on 2/9/2018.
 */

public class TMDBTrailerLoader extends AsyncTaskLoader<ArrayList<Trailer>> {
    private static final String API_KEY = com.dwbi.android.popularmovies.BuildConfig.API_KEY;
    
    private static String BASE_URL = "http://api.themoviedb.org/";
    
    String id;
    
    ArrayList<Trailer> result = null;
    
    
    public TMDBTrailerLoader(Context context, String id) {
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
    public void deliverResult(ArrayList<Trailer> data) {
        if (isReset()) {
            return;
        }
        result = data;
        if(isStarted()){
            super.deliverResult(data);
        }
        
    }
    
    
    @Override
    public ArrayList<Trailer> loadInBackground() {
        //----------------------- RETROFIT ---------------------------------------------------------
        
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        
        TMDBAPI tmdbapi = retrofit.create(TMDBAPI.class);
        
        Call<Trailers> call = tmdbapi.getTrailers(id, API_KEY);
        
        try {
            Response<Trailers> response = call.execute();
            Trailers trailers = response.body();
            return (ArrayList<Trailer>) trailers.getTrailers();
        } catch (IOException e) {
            Log.d("PSX", "e-> " + e);
        }
        //----------------------- RETROFIT ---------------------------------------------------------
        
        return null;
        
    }
}
