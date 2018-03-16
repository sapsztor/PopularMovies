package com.dwbi.android.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dwbi.android.popularmovies.model.Review;

import java.util.ArrayList;

/**
 * Created by PSX on 3/16/2018.
 */

public class ReviewsAdapter extends BaseAdapter {
    private static final String TAG = "PSX";
    
    private Context mContext;
    private ArrayList<Review> reviewData = new ArrayList<>();
    
    public ReviewsAdapter(Context context) {
        //
        this.mContext = context;
    }
    public ReviewsAdapter(Context context, ArrayList data) {
        //
        this.mContext = context;
        this.reviewData = data;
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
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridCell;
        
        if(convertView == null){
            
            gridCell = inflater.inflate(R.layout.review_item_layout, null);
            TextView reviewContent = (TextView) gridCell.findViewById(R.id.tv_review_content);
            reviewContent.setText(reviewData.get(position).getAuthor() + "\n\n" + reviewData.get(position).getContent() + "\n\n");
    
//            reviewContent.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onClickCallback(reviewData.get(position).getContent());
//                }
//            });
        

        
        } else {
            gridCell = (View) convertView;
        }
        return gridCell;
        
    }

}