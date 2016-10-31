package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        initialize();
        populateTimeline();
        setupImplicitIntentReceiver();
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

                        ArrayList<Tweet> tweetArrayFromJson = Tweet.fromJSONArray(jsonArray);

                        if(!fetchNewAfterInitialLoad) {
                            tweets.addAll(tweetArrayFromJson);
                            tweetsArrayAdapter.notifyDataSetChanged();
                        } else {

                            Log.d("DEBUG", "INSERTING NEW STUFF TO TIMELINE" + tweetArrayFromJson.size());

                            for(int x = tweetArrayFromJson.size() - 1; x >= 0; --x) {
                                Log.d("DEBUG", "PREPENDING" + tweetArrayFromJson.get(x).getUser().getScreenName() + tweetArrayFromJson.get(x).getBody());
                                tweets.add(0, tweetArrayFromJson.get(x));
                                tweetsArrayAdapter.notifyItemInserted(0);
                                rvTimeline.scrollToPosition(0);
                            }

                            fetchNewAfterInitialLoad = false;
                            swipeContainer.setRefreshing(false);
                        }
                        Log.d("DEBUG", tweets.size() + "size");
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

                        // TODO: Do this in a background thread
//                        try {
//                            TimeUnit.SECONDS.sleep(10);
//                            showToast("Please wait while we attempt to load more tweets");
//                            // populateTimeline();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
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


        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchNewAfterInitialLoad = true;
                populateTimeline();
            }
        });
    }

    public void setRvScrollListener() {
        rvTimeline.addOnScrollListener(new EndlessRecyclerViewScrollListener(
                staggeredGridLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                populateTimeline();
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
                Tweet t = Tweet.fromJSON(response);;
                tweets.add(0, t);
                tweetsArrayAdapter.notifyItemInserted(0);
                rvTimeline.scrollToPosition(0);
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
    }

    public void closeComposeTweetFragment(View v) {
        composeTweetDialogFragment.dismiss();
    }

    public void setupImplicitIntentReceiver() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (null != type && Intent.ACTION_SEND.equals(action)) {
            if("text/plain".equals(type)) {
                String title = intent.getStringExtra(Intent.EXTRA_SUBJECT);
                String url = intent.getStringExtra(Intent.EXTRA_TEXT);

                FragmentManager fm = getSupportFragmentManager();
                composeTweetDialogFragment = new ComposeTweetDialogFragment();


                Bundle dataForTweet = new Bundle();
                dataForTweet.putString("title", title);
                dataForTweet.putString("url", url);

                composeTweetDialogFragment.setArguments(dataForTweet);
                composeTweetDialogFragment.show(fm, "NEW_TWEET_FRAGMENT");

                Log.d("DEBUG", title);
                Log.d("DEBUG", url);
            }
        }
    }
}