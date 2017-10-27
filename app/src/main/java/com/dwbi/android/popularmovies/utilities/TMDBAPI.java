package com.dwbi.android.popularmovies.utilities;

/**
 * Created by PSX on 10/27/2017.
 */

import com.dwbi.android.popularmovies.model.Movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDBAPI {
    @GET("3/movie/{sortby}")
    Call<Movies> getMovies(@Path("sortby") String sortby, @Query("page") String pagenum, @Query("api_key") String api_key);
}
