package com.dwbi.android.popularmovies.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dwbi.android.popularmovies.R;
import com.dwbi.android.popularmovies.model.Review;

import java.util.ArrayList;

/**
 * Created by PSX on 3/16/2018.
 */

public class ReviewsAdapter extends BaseAdapter {
    private static final String TAG = "PSX";
    
    private final Context mContext;
    private ArrayList<Review> reviewData = new ArrayList<>();
    
    @SuppressWarnings("unused")
    public ReviewsAdapter(Context context) {
        this.mContext = context;
    }
    public ReviewsAdapter(Context context, ArrayList<Review> data) {
        //
        this.mContext = context;
        this.reviewData =  data;
        Log.d(TAG, "ReviewsAdapter data.size-> " + data.size());
        
    }
    @Override
    public int getCount() {
        return reviewData.size();
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
    
    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridCell;
        
        if(convertView == null){
    
            //noinspection ConstantConditions
            gridCell = inflater.inflate(R.layout.review_item_layout, null) ;
            TextView reviewContent = gridCell.findViewById(R.id.tv_review_content);
            TextView reviewAuthor = gridCell.findViewById(R.id.tv_review_author);
            
            reviewContent.setText(reviewData.get(position).getContent() + "\n\n");
            reviewAuthor.setText(reviewData.get(position).getAuthor());
            if(position % 2 == 0){
                reviewContent.setBackgroundColor(mContext.getResources().getColor(R.color.reviewBackgroudColor1));
                reviewAuthor.setBackgroundColor(mContext.getResources().getColor(R.color.reviewBackgroudColor1));
            } else {
                reviewContent.setBackgroundColor(mContext.getResources().getColor(R.color.reviewBackgroudColor2));
                reviewAuthor.setBackgroundColor(mContext.getResources().getColor(R.color.reviewBackgroudColor2));
            }
        
        } else {
            gridCell = convertView;
        }
        return gridCell;
        
    }

}
