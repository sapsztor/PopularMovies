package com.dwbi.android.popularmovies;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.dwbi.android.popularmovies.model.Movie;
import com.dwbi.android.popularmovies.model.Movies;
import com.dwbi.android.popularmovies.utilities.MovieJSONParser;
import com.dwbi.android.popularmovies.utilities.NetworkUtils;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.dwbi.android.popularmovies.utilities.TMDBAPI;

//https://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a
class TMDBQueryTask extends AsyncTask<String, Void, ArrayList<Movie>>{

    private static final String API_KEY = com.dwbi.android.popularmovies.BuildConfig.API_KEY;

    public interface AsyncResponse {
        void processResponse(ArrayList<Movie> response);
    }
    private AsyncResponse callback = null;

    public TMDBQueryTask(AsyncResponse callback) {
        this.callback = callback;
    }



    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }
        String sortBy = params[0];
        String pageNum = params[1];

        //TODO: using Volley or Retrofit
//        try {
//            web_result = NetworkUtils.getResponseFromHttpUrl(query_url);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //----------------------- RETROFIT ---------------------------------------------------------

        Log.d("PSX", "---- R E T R O F I T ------------");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TMDBAPI tmdbapi = retrofit.create(TMDBAPI.class);

        Call<Movies> call = tmdbapi.getMovies(sortBy, pageNum, API_KEY);

        try {
            Response<Movies> response = call.execute();
            Movies movies = response.body();
            Log.d("PSX", "movies.getMovies().size()-> " + movies.getMovies().size());
            Log.d("PSX", "reponse-> " + response);

            return (ArrayList<Movie>) movies.getMovies();
        } catch (IOException e) {
            Log.d("PSX", "e-> " + e);
        }
        //----------------------- RETROFIT ---------------------------------------------------------

        // TODO: checking empty result!
        return null;
    }


    @Override
    protected void onPostExecute(ArrayList<Movie> result) {
        super.onPostExecute(result);

        callback.processResponse(result);

    }
}
