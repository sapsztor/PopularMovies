package com.dwbi.android.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

//import com.dwbi.android.popularmovies.BuildConfig;

import java.net.URL;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.Scanner;


/**
 * Created by PSX on 9/29/2017.
 */

@SuppressWarnings("DefaultFileTemplate")
public class NetworkUtils {
    private static final String TMDB_POSTER_URL = "http://image.tmdb.org/t/p/";
      private static final String MOVIE_THUMB_SIZE_500 = "w500";



    private static final String API_KEY = com.dwbi.android.popularmovies.BuildConfig.API_KEY;
    //private static final String API_KEY = BuildConfig.API_KEY;



    @SuppressWarnings("unused")
    public static URL buildQueryUrl(String sortBy , String pageNum) {
        Log.d("PSX", "NetworkUtils.buildQueryUrl.sortBy-> " + sortBy);

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(sortBy)
                .appendQueryParameter("page", pageNum)
                .appendQueryParameter("api_key", API_KEY);
        URL url = null;

        try {
            url = new URL(builder.build().toString()) ;
            String myUrl = builder.build().toString();
            Log.d("PSX", "NetworkUtils.buildQueryUrl.url-> " + myUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }



    public static String buildQueryPoster(String posterPath) {
        return TMDB_POSTER_URL + MOVIE_THUMB_SIZE_500 + posterPath;
    }
    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    @SuppressWarnings("unused")
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
    
}
