package com.dwbi.android.popularmovies;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.compat.*;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.dwbi.android.popularmovies.model.Movie;

import java.util.ArrayList;

@SuppressWarnings("UnnecessaryReturnStatement")
public class MainActivity extends AppCompatActivity implements TMDBQueryTask.AsyncResponse , MoviesAdapter.AdapterStatusListener {

    private static final int DETAIL_ACTIVITY_RESPONSE = 12345;



    private RecyclerView rv_video_thumb;
    private ProgressBar pb_loading_indicator;

    @SuppressWarnings("CanBeFinal")
    private ArrayList<Movie> movieData = new ArrayList<>();
    private MoviesAdapter adapter;


    private int pageNum = 1;

    // save recyclerview state  https://stackoverflow.com/questions/28236390/recyclerview-store-restore-state-between-activities
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle recyclerViewState;

    //--------------------------------------------------------------------------
    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1 :
                    queryMore();
                    break;
                default :
            }
        }
    };
    //----------------------------------------------------------------------------------------------
    private void initRecyclerView(){
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rv_video_thumb.setLayoutManager(layoutManager);
        rv_video_thumb.setHasFixedSize(true);
        adapter = new MoviesAdapter(this, handler);
    }
    //----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_scroll);

        rv_video_thumb =  findViewById(R.id.rv_video_thumb);
        pb_loading_indicator =  findViewById(R.id.pb_loading_indicator);

        if(getQueryPreference() == null){
            setQueryPreference(getString(R.string.query_param_value_sortby_popular));
        }


        initRecyclerView();

        pageNum = 1;
        startQuery(getQueryPreference(), Integer.toString(pageNum));
    }

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onResume() {
        super.onResume();
        if (recyclerViewState != null) {
            Parcelable listState = recyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            rv_video_thumb.getLayoutManager().onRestoreInstanceState(listState);
        }


    }
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onPause() {
        super.onPause();
        recyclerViewState = new Bundle();
        if (rv_video_thumb != null) {
            Parcelable listState = rv_video_thumb.getLayoutManager().onSaveInstanceState();
            recyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
        }
    }
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        GridLayoutManager layoutManager = (GridLayoutManager) rv_video_thumb.getLayoutManager();
        layoutManager.onSaveInstanceState();

    }

    //----------------------------------------------------------------------------------------------
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void onListItemClick(Movie movie) {
        // save position until we get back from detailActivity

        Context context = this;
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("title", movie.getTitle());
        intent.putExtra("posterpath", movie.getPosterPath());
        intent.putExtra("overview", movie.getOverview());
        intent.putExtra("vote_average", movie.getVote_Average());
        intent.putExtra("release_date", movie.getRelease_Date());

        startActivityForResult(intent, DETAIL_ACTIVITY_RESPONSE);
    }
    //----------------------------------------------------------------------------------------------


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    //----------------------------------------------------------------------------------------------
    //TODO: starting network setting implicit intent
    private void checkInternetConnection (){
        if (isOnline()) {
            return;
        } else {
            try {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

                alertDialog.setTitle("Info");
                alertDialog.setMessage("Internet not available, Check your internet connectivity and try again");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                finish();
                                //dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            catch(Exception e)
            {
                Log.d("PSX", "Show Dialog: "+e.getMessage());
            }
        }
    }
    //----------------------------------------------------------------------------------------------
    // after the TMDBQueryTask finish then it calls the processResponse method
    private void startQuery(String sortBy, String pageNum){
        pb_loading_indicator =  findViewById(R.id.pb_loading_indicator);
        pb_loading_indicator.setVisibility(View.VISIBLE);

        checkInternetConnection();
        TMDBQueryTask task = new TMDBQueryTask(this);
        task.execute(sortBy, pageNum);

    }
    //----------------------------------------------------------------------------------------------
    // to query more from web only has to do is increment the pageNum variable and call the startQuery
    private void queryMore(){
        if (adapter == null) {
            return;
        }

        pageNum++;
        startQuery(getQueryPreference(), Integer.toString(pageNum));
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void processResponse(ArrayList<Movie> response) {
        ArrayList<Movie> tmpData;
        if (response == null) {
            tmpData = new ArrayList<>();
        } else {
             tmpData = response;
        }

        if (pageNum == 1){
            movieData.clear();
            movieData.addAll(tmpData);

            adapter.setData(movieData);

        } else {
            int curSize = adapter.getItemCount();
            //noinspection ConstantConditions
            movieData.addAll(response);
            adapter.setData(movieData);
            adapter.notifyItemRangeInserted(curSize, response.size());
        }

        rv_video_thumb.setAdapter(adapter);

        pb_loading_indicator =  findViewById(R.id.pb_loading_indicator);
        pb_loading_indicator.setVisibility(View.INVISIBLE);

        if (pageNum > 1) {
            int scrollPosition = ((this.pageNum - 1) * 20) - 6;
            rv_video_thumb.scrollToPosition(scrollPosition);
        }

    }
    //----------------------------------------------------------------------------------------------
    private void setQueryPreference(String sortOrder) {
        SharedPreferences prefs = this.getPreferences(MODE_PRIVATE);
        String text = prefs.getString(getString(R.string.preferred_sort_order), null);
        //noinspection StringEquality
        if(text == null || text != sortOrder) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(getString(R.string.preferred_sort_order), sortOrder);
            //editor.commit();
            editor.apply();
        }
    }
    //----------------------------------------------------------------------------------------------
    private String getQueryPreference(){
        SharedPreferences prefs = this.getPreferences(MODE_PRIVATE);
        return prefs.getString(getString(R.string.preferred_sort_order), null);
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.action_popular:
                //noinspection StringEquality
                if(getString(R.string.query_param_value_sortby_popular) != getQueryPreference()){
                    setQueryPreference(getString(R.string.query_param_value_sortby_popular));
                    pageNum = 1;
                    adapter.clearData();
                    startQuery(getQueryPreference(), Integer.toString(pageNum));
                }
                return true;

            case R.id.action_top_rated:
                //noinspection StringEquality
                if(getString(R.string.query_param_value_sortby_top_rated) != getQueryPreference()){
                    setQueryPreference(getString(R.string.query_param_value_sortby_top_rated));
                    pageNum = 1;
                    adapter.clearData();
                    startQuery(getQueryPreference(), Integer.toString(pageNum));
                }
                return true;

            default:
                return true;
        }
    }

}
