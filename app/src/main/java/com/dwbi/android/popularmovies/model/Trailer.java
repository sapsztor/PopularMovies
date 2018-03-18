package com.dwbi.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by PSX on 2/8/2018.
 */

@SuppressWarnings("unused")
public class Trailer implements Parcelable {

    private final String id;
    private final String name;
    private final String key;
    private final String site;
    private final String type;
    private final String size;
    
    public Trailer(String id,
                   String name,
                   String key,
                   String site,
                   String type,
                   String size){
        this.id = id;
        this.name = name;
        this.key = key;
        this.site = site;
        this.type = type;
        this.size = size;
    }
    
    public String getId () {return id;}
    public String getName () {return name;}
    public String getKey () {return key;}
    public String getSite () {return site;}
    public String getType () {return type;}
    public String getSize () {return size;}
    
    private Trailer(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.key = in.readString();
        this.site = in.readString();
        this.type = in.readString();
        this.size = in.readString();
    }
    //----------------------------------------------------------------------------------------------
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }
    
        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
    //----------------------------------------------------------------------------------------------
    
    
    @Override
    public String toString() {
        return id + ", " + name + ", " + key + ", " + site + ", " + type + ", " + size;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.key);
        dest.writeString(this.site);
        dest.writeString(this.type);
        dest.writeString(this.size);
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
}
