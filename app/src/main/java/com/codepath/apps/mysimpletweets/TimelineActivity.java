package com.codepath.apps.mysimpletweets;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements ComposeTweetDialogFragment.ComposeTweetDialogListener {
    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter tweetsArrayAdapter;
    private RecyclerView rvTimeline;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    private static final int gridNumOfColumns = 2;
    private boolean fetchNewAfterInitialLoad = false;
    private ComposeTweetDialogFragment composeTweetDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        initialize();
        // populateTimeline();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline_menu, menu);
        return true;
    }

    private void populateTimeline() {
        client.getHomeTimeline(fetchNewAfterInitialLoad,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode,
                                          Header[] headers,
                                          JSONArray jsonArray) {

                        // Log.d("RESPONSE", jsonArray.toString());
                        tweets.addAll(Tweet.fromJSONArray(jsonArray));
                        Log.d("DEBUG", tweets.size() + "size");
                        tweetsArrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(int statusCode,
                                          Header[] headers,
                                          String responseString,
                                          Throwable throwable) {

                        Log.d("DEBUG", responseString.toString());
                    }

                    @Override
                    public void onFailure(int statusCode,
                                          Header[] headers,
                                          Throwable throwable,
                                          JSONObject errorResponse) {

                        Log.d("DEBUG", errorResponse.toString());
                        showToast("Oops! Something went wrong..Please try again after some time");

                        try {
                            TimeUnit.SECONDS.sleep(10);
                            showToast("Please wait while we attempt to load more tweets");
                            populateTimeline();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initialize() {
        client = TwitterApplication.getRestClient();
        tweets = new ArrayList<>();
        tweetsArrayAdapter = new TweetsArrayAdapter(this, tweets);
        rvTimeline = (RecyclerView) findViewById(R.id.rvTimeline);
        rvTimeline.setAdapter(tweetsArrayAdapter);

        staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(gridNumOfColumns,
                        StaggeredGridLayoutManager.VERTICAL);

        rvTimeline.setLayoutManager(staggeredGridLayoutManager);
        setRvScrollListener();

    }

    public void setRvScrollListener() {
        rvTimeline.addOnScrollListener(new EndlessRecyclerViewScrollListener(
                staggeredGridLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d("DEBUG", "LOADING MORE TWEETS!");
                // populateTimeline();
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(TimelineActivity.this,
                message,
                Toast.LENGTH_SHORT).show();
    }

    public void composeNewTweet(MenuItem menuItem) {
        FragmentManager fm = getSupportFragmentManager();
        composeTweetDialogFragment = new ComposeTweetDialogFragment();
        composeTweetDialogFragment.show(fm, "NEW_TWEET_FRAGMENT");
    }

    @Override
    public void onFinishComposeTweet(String composedTweet) {
        client.postTweet(composedTweet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode,
                                  Header[] headers,
                                  JSONObject response) {
                Log.d("DEBUG", response.toString());
            }

            @Override
            public void onFailure(int statusCode,
                                  Header[] headers,
                                  Throwable throwable,
                                  JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    public void sendTweet(View v) {
        composeTweetDialogFragment.dismiss();
    }

    public void closeComposeTweetFragment(View v) {
        composeTweetDialogFragment.dismiss();
    }
}
