package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.codepath.apps.mysimpletweets.ComposeTweetDialogFragment;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class HomeTimelineFragment extends TweetsListFragment
        implements ComposeTweetDialogFragment.ComposeTweetDialogListener {

    private TwitterClient client;
    private boolean fetchNewAfterInitialLoad = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
        populateTimeline();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_timeline_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.menu_compose_tweet:
                composeNewTweet();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initialize() {
        client = TwitterApplication.getRestClient();
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
                            addAll(tweetArrayFromJson);
                        } else {
                            Log.d("DEBUG", "INSERTING NEW STUFF TO TIMELINE" + tweetArrayFromJson.size());
                            fetchNewAfterInitialLoad = false;
                        }
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
                    }
                });
    }

    private void composeNewTweet() {
        Log.d("DEBUG", "SHOWING COMPOSER");
        FragmentManager fm = getFragmentManager();
        ComposeTweetDialogFragment composeTweetDialogFragment = new ComposeTweetDialogFragment();
        composeTweetDialogFragment.setTargetFragment(HomeTimelineFragment.this, 300);
        composeTweetDialogFragment.show(fm, "NEW_TWEET_FRAGMENT");
    }

    @Override
    public void onFinishComposeTweet(String composedTweet) {
        client.postTweet(composedTweet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                Tweet t = Tweet.fromJSON(response);
                addToTop(t);
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
}
