package com.dwbi.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.dwbi.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;


public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_detail);

        TextView tv_title;
        ImageView iv_poster;
        TextView tv_overview;
        TextView tv_vote_average;
        TextView tv_release_date;


        //noinspection RedundantCast
        tv_title = (TextView) findViewById(R.id.tv_title);
        //noinspection RedundantCast
        iv_poster = (ImageView) findViewById(R.id.iv_poster);
        //noinspection RedundantCast
        tv_overview = (TextView) findViewById(R.id.tv_overview);
        //noinspection RedundantCast
        tv_vote_average = (TextView) findViewById(R.id.tv_vote_average);
        //noinspection RedundantCast
        tv_release_date = (TextView) findViewById(R.id.tv_release_date);

        Intent intent = getIntent();
        String posterpath = intent.getStringExtra("posterpath");
        String title = intent.getStringExtra("title");
        String overview = intent.getStringExtra("overview");
        String vote_average = intent.getStringExtra("vote_average");
        String release_date = intent.getStringExtra("release_date");

        tv_title.setText(title);
        tv_overview.setText(overview);
        tv_vote_average.setText(vote_average);
        tv_release_date.setText(release_date);


        Picasso.with(this)
                .load(NetworkUtils.buildQueryPoster(posterpath))
                .placeholder(R.drawable.poster_placeholder)
                .into(iv_poster);


    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d("PSX", "DetailActivity.onOptionsItemSelected");
        }

        return super.onOptionsItemSelected(item);
    }
}
