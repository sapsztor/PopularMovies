package com.dwbi.android.popularmovies;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
    private  Handler handler_to_main ;

    @SuppressWarnings("CanBeFinal")
    private AdapterStatusListener callback;
    //--------------------------------------------------------------------------

    public interface AdapterStatusListener {
        void onListItemClick(Movie movie);
    }

    //--------------------------------------------------------------------------

    public MoviesAdapter(AdapterStatusListener parent, Handler handler) {
        this.callback = parent;
        this.handler_to_main = handler;

    }

    //--------------------------------------------------------------------------
    public void setData(ArrayList<Movie> data) {
        this.movieData = data;
    }

    //--------------------------------------------------------------------------
    @Override
    public int getItemCount() {
        return movieData.size();
        //
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

    //--------------------------------------------------------------------------

    @Override
    public void onViewAttachedToWindow(MovieViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getAdapterPosition();


        int Gap = 2;
        if (position == (getItemCount() - Gap)) {
            Message msg = handler_to_main.obtainMessage(1);
            handler_to_main.sendMessage(msg);
        }

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


        public MovieViewHolder(View itemView) {
            super(itemView);
            listItemPosterView =  itemView.findViewById(R.id.iv_video_poster);
            itemView.setOnClickListener(this);
        }
    }
}


























