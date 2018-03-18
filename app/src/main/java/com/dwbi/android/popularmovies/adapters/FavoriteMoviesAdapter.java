package com.dwbi.android.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dwbi.android.popularmovies.DetailActivity;
import com.dwbi.android.popularmovies.model.Movie;
import com.dwbi.android.popularmovies.model.MovieContract;
import com.dwbi.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;
import com.dwbi.android.popularmovies.R;


/**
 * Created by PSX on 1/2/2018.
 */

public class FavoriteMoviesAdapter extends RecyclerView.Adapter<FavoriteMoviesAdapter.MovieViewHolder>  {
    private Cursor cursor;
    private final Context context;

    
    
    public FavoriteMoviesAdapter(Context context){
        this.context = context;
        Log.d("PSX", "FavoriteMoviesAdapter");
    }
    
    public void swapCursor(Cursor newCursor) {
        Log.d("PSX", "FavoriteMoviesAdapter.swapCursor");
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
    }
    
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_poster_item, parent, false);
        return new MovieViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(FavoriteMoviesAdapter.MovieViewHolder holder, int position) {
        cursor.moveToPosition(position);
        String posterpath = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTERPATH));
    
        //Log.d("PSX", "FavoriteMoviesAdapter.onBindViewHolder posterpath-> " + posterpath);
    
        Picasso.with(holder.listItemPosterView.getContext())
            .load(NetworkUtils.buildQueryPoster(posterpath))
            .placeholder(R.drawable.poster_placeholder)
            .into(holder.listItemPosterView);
    
    }
    
    @Override
    public int getItemCount() {
        if (cursor == null) {
            return 0;
        }
        return cursor.getCount();
    }
    
    
    //--------------------------------------------------------------------------
    class MovieViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        @SuppressWarnings("CanBeFinal")
        ImageView listItemPosterView;
        
        private Movie getCurrentMovie(){
            return new Movie(
                cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)),
                cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE)),
                cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTERPATH)),
                cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW)),
                cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE)),
                cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE))
            );
        }
        
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), DetailActivity.class);
            cursor.moveToPosition(getAdapterPosition());
            //Log.d("PSX", "FavoriteMoviesAdapter.onClick getAdapterPosition()-> " + getAdapterPosition());
            //Log.d("PSX", "FavoriteMoviesAdapter.onClick getAdapterPosition()-> " + cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)) + cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));
            Movie tmpMovie = getCurrentMovie();
            
            //Log.d("PSX", "FavoriteMoviesAdapter.onClick tmpMovie.toString()-> " + tmpMovie.toString()) ;
            intent.putExtra(context.getString(R.string.extra_selectedmovie), tmpMovie);
            v.getContext().startActivity(intent);
            
            
        }
        
        
        public MovieViewHolder(View itemView) {
            super(itemView);
            listItemPosterView =  itemView.findViewById(R.id.iv_video_poster);
            itemView.setOnClickListener(this);
        }
    }
}
