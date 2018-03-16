package com.dwbi.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by PSX on 3/15/2018.
 */

public class Review implements Parcelable {
    
    private final String id;
    private final String author;
    private final String content;
    
    public Review(String id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }
    
    public String getId() {return id;}
    public String getAuthor() {return author;}
    public String getContent() {return content;}
    
    private Review (Parcel in) {
        this.id = in.readString();
        this.author = in.readString();
        this.content = in.readString();
    }
    
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }
    
        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
    
    @Override
    public String toString() {
        return id + ", " + author + ", " + content;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
}
