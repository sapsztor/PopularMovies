package com.dwbi.android.popularmovies.adapters;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.dwbi.android.popularmovies.R;
import com.dwbi.android.popularmovies.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by PSX on 2/7/2018.
 */

public class TrailersAdapter extends BaseAdapter {
    
    private static final String TAG = "PSX";
    
    private final Context mContext;
    private ArrayList<Trailer> trailerData = new ArrayList<>();
    
    @SuppressWarnings("unused")
    public TrailersAdapter(Context context) {
        //
        this.mContext = context;
    }
    public TrailersAdapter(Context context, ArrayList<Trailer> data) {
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
    
    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
    
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridCell;
        //int targetWidth = (int) parent.getResources().getDimension(R.dimen.trailer_thumb_width);
        //int  targetHeigth = (int) parent.getResources().getDimension(R.dimen.trailer_thumb_width);
        
        if(convertView == null){
    
            //noinspection ConstantConditions
            gridCell = inflater.inflate(R.layout.trailer_item_layout, null);
            ImageView trailerThumb = gridCell.findViewById(R.id.ivTrailerThumb);
    
            trailerThumb.setScaleType(ImageView.ScaleType.CENTER_CROP);
            trailerThumb.setPadding(8,8,8,8);
            trailerThumb.setContentDescription(trailerData.get(position).getType());
            
            trailerThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickCallback(trailerData.get(position).getKey());
                }
            });
            
            
            Picasso.with(mContext)
                .load("https://img.youtube.com/vi/" + trailerData.get(position).getKey() + "/0.jpg")
                //.centerCrop()
                //.centerInside()
                //.resize(150,150)
                .into(trailerThumb);
            
        } else {
            gridCell = convertView;
        }
        return gridCell;

    }
    


    
    private void onClickCallback(String videoId) {
        if (checkYoutubePlayer()) {
            
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
                mContext.startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + videoId));
                mContext.startActivity(intent);
            }
        } else {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + videoId));
                mContext.startActivity(intent);
                
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(mContext, "No videoplayer app", Toast.LENGTH_SHORT).show();
            }
            
        }
    }
    

    private boolean checkYoutubePlayer() {
        PackageManager pm = mContext.getPackageManager();
        boolean installed;
        try {
            pm.getPackageInfo("com.google.android.youtube", PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }
}
