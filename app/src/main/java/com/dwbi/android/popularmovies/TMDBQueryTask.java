package com.dwbi.android.popularmovies;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import com.dwbi.android.popularmovies.model.Movie;
import com.dwbi.android.popularmovies.utilities.MovieJSONParser;
import com.dwbi.android.popularmovies.utilities.NetworkUtils;


//https://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a
class TMDBQueryTask extends AsyncTask<String, Void, ArrayList<Movie>>{

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
        String web_result = null;
        URL query_url = NetworkUtils.buildQueryUrl(sortBy, pageNum);

        //TODO: using Volley or Retrofit
        try {
            web_result = NetworkUtils.getResponseFromHttpUrl(query_url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO: checking empty result!
        return MovieJSONParser.parseMovies(web_result);
    }


    @Override
    protected void onPostExecute(ArrayList<Movie> result) {
        super.onPostExecute(result);
        callback.processResponse(result);

    }
}
