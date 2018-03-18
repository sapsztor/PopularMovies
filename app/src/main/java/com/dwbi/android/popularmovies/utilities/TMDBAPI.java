package com.dwbi.android.popularmovies.utilities;

/*
 * Created by PSX on 10/27/2017.
 */

import com.dwbi.android.popularmovies.model.Movies;
import com.dwbi.android.popularmovies.model.Reviews;
import com.dwbi.android.popularmovies.model.Trailers;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

@SuppressWarnings("SameParameterValue")
public interface TMDBAPI {

    @GET("3/movie/{sortby}")
    Call<Movies> getMovies(
        @Path("sortby") String sortby,
        @Query("page") String pagenum,
        @Query("api_key") String api_key);
    
    
    @GET("3/movie/{id}/videos")
    Call<Trailers> getTrailers(
        @Path("id") String id,
        @Query("api_key") String api_key);
    
    
    @GET("3/movie/{id}/reviews")
    Call<Reviews> getReviews(
        @Path("id") String id,
        @Query("api_key") String api_key);
}
