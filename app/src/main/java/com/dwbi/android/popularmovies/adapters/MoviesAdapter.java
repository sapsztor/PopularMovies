package com.dwbi.android.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dwbi.android.popularmovies.R;
import com.dwbi.android.popularmovies.model.Movie;

import java.util.ArrayList;

import com.dwbi.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;



/**
 * Created by PSX on 10/2/2017.
 */

 @SuppressWarnings("DefaultFileTemplate")
 public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private ArrayList<Movie> movieData = new ArrayList<>();

    @SuppressWarnings("CanBeFinal")
    private AdapterStatusListener callback;
    //--------------------------------------------------------------------------

    public interface AdapterStatusListener {
        void onListItemClick(Movie movie);
    }

    //--------------------------------------------------------------------------

    public MoviesAdapter(AdapterStatusListener parent) {
        this.callback = parent;
    }

    //--------------------------------------------------------------------------
    public void setData(ArrayList<Movie> data) {
        this.movieData = data;
    }

    //--------------------------------------------------------------------------
    public void appendData(ArrayList<Movie> data) {
        //notifyItemRangeChanged(int positionStart, int itemCount)
        int positionStart = movieData.size() - 1;
        int itemCount = data.size();
        this.movieData.addAll(data);

        //notifyItemRangeChanged(positionStart, itemCount);
        notifyDataSetChanged();
    }
    //--------------------------------------------------------------------------
    @Override
    public int getItemCount() {
        return movieData.size();
    }
    //--------------------------------------------------------------------------

    public void clearData() {
        movieData.clear();
    }
    //--------------------------------------------------------------------------

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_poster_item, parent, false);
        return new MovieViewHolder(view);
    }
    //--------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Picasso.with(holder.listItemPosterView.getContext())
                .load(NetworkUtils.buildQueryPoster(movieData.get(position).getPosterPath()))
                .placeholder(R.drawable.poster_placeholder)
                .into(holder.listItemPosterView);


    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void onViewAttachedToWindow(MovieViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getAdapterPosition();
    }
    //--------------------------------------------------------------------------
    class MovieViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        @SuppressWarnings("CanBeFinal")
        ImageView listItemPosterView;

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Movie currMovie = movieData.get(position);

            callback.onListItemClick(currMovie);
        }
    //--------------------------------------------------------------------------
        public MovieViewHolder(View itemView) {
            super(itemView);
            listItemPosterView =  itemView.findViewById(R.id.iv_video_poster);
            itemView.setOnClickListener(this);
        }
    }
}


























