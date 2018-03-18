package com.dwbi.android.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PSX on 3/16/2018.
 */

public class Reviews {
    @SerializedName("results")
    private final List<Review> reviews = new ArrayList<>();
    
    public List<Review> getReviews(){
        return reviews;
    }
}
