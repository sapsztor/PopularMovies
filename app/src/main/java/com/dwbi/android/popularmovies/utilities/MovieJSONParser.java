package com.dwbi.android.popularmovies.utilities;

import android.util.Log;

import com.dwbi.android.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by PSX on 10/1/2017.
 */

@SuppressWarnings("DefaultFileTemplate")
public class MovieJSONParser {

    public static ArrayList<Movie> parseMovies(String jsonstring) {

        if (jsonstring == null || jsonstring.length() == 0){
            return null;
        }

        JSONObject tmpJSON;
        JSONArray moviesJSONArray;
        ArrayList<Movie> movieList = new ArrayList<>();

        try {
            tmpJSON = new JSONObject(jsonstring);
            // TODO: ellenorizni hogy nem hibaval jott-e a json
            moviesJSONArray = tmpJSON.getJSONArray("results");
            for(int i = 0; i < moviesJSONArray.length(); i++){
                JSONObject row = moviesJSONArray.getJSONObject(i);

                Movie tmpMovie = new Movie(
                        row.getString("id"),
                        row.getString("title"),
                        row.getString("poster_path"),
                        row.getString("overview"),
                        row.getString("vote_average"),
                        row.getString("release_date"));
                movieList.add(tmpMovie);
            }

        }  catch (JSONException e) {
            e.printStackTrace();
            Log.d("PSX", "MovieJSONParser -> hiba");
        }
        return movieList;
    }

}
