package com.dwbi.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by PSX on 10/1/2017.
 */
// //TODO: implementing Parcellable interface
@SuppressWarnings("DefaultFileTemplate")
public class Movie implements Parcelable {

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

    private Movie(Parcel in) {
        this.title = in.readString();
        this.posterpath = in.readString();
        this.overview = in.readString();
        this.vote_average = in.readString();
        this.release_date = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public Movie createFromParcel(Parcel in){
            return new Movie(in);
        }
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public String toString(){
        return title + ", " + posterpath + ", " + overview + ", " + vote_average + ", " + release_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.posterpath);
        dest.writeString(this.overview);
        dest.writeString(this.vote_average);
        dest.writeString(this.release_date);
    }
}
