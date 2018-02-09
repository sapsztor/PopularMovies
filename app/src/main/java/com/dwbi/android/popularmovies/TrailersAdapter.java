package com.dwbi.android.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.dwbi.android.popularmovies.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by PSX on 2/7/2018.
 */

public class TrailersAdapter extends BaseAdapter {
    
    private static final String TAG = "PSX";
    
    private Context mContext;
    private ArrayList<Trailer> trailerData = new ArrayList<>();
    
    public TrailersAdapter(Context context) {
        //
        this.mContext = context;
    }
    public TrailersAdapter(Context context, ArrayList data) {
        //
        this.mContext = context;
        this.trailerData = data;
        Log.d(TAG, "TrailersAdapter data.size-> " + data.size());
        
    }
    @Override
    public int getCount() {
        return trailerData.size();
    }
    
    @Override
    public Object getItem(int position) {
        //
        return null;
    }
    
    @Override
    public long getItemId(int position) {
        //
        return 0;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        
        if(convertView == null){
            imageView = new ImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8,8,8,8);
        } else {
            imageView = (ImageView) convertView;
        }
        Picasso.with(mContext)
            .load("https://img.youtube.com/vi/" + trailerData.get(position).getKey() + "/0.jpg")
            //.centerCrop()
            .centerInside()
            .resize(150,150)
            .into(imageView);
        return imageView;
        
    }
    
    
}
