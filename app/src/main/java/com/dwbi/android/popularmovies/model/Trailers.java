package com.dwbi.android.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by PSX on 2/8/2018.
 */

public class Trailers {
    @SuppressWarnings("CanBeFinal")
    @SerializedName("results")
    private List<Trailer> trailers = new ArrayList<>();
    
    public List<Trailer> getTrailers() {
        return trailers;
    }
}


