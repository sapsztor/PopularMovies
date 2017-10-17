package com.dwbi.android.popularmovies.model;

/**
 * Created by PSX on 10/1/2017.
 */
// //TODO: implementing Parcellable interface
@SuppressWarnings("DefaultFileTemplate")
public class Movie {
    private final String title;
    private final String posterpath;
    private final String overview;
    private final String vote_average;
    private final String release_date;

    public  Movie(String title,
                      String posterpath,
                      String overview,
                      String vote_average,
                      String release_date) {
        this.title = title;
        this.posterpath = posterpath;
        this.overview = overview;
        this.vote_average = vote_average;
        this.release_date = release_date;
    }

    public String getTitle() {return title;}
    public String getPosterPath() {return posterpath;}
    public String getOverview() {return overview;}
    public String getVote_Average() {return vote_average;}
    public String getRelease_Date() {return release_date;}


    public String toString(){
        return title + ", " + posterpath + ", " + overview + ", " + vote_average + ", " + release_date;
    }

}
