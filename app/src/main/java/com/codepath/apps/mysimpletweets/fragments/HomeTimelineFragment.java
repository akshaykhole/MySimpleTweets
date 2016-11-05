package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.util.Log;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by akshay on 11/5/16.
 */

public class HomeTimelineFragment extends TweetsListFragment {
    private TwitterClient client;
    private boolean fetchNewAfterInitialLoad = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
        populateTimeline();
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
                            // Add to fragment here
                            addAll(tweetArrayFromJson);
                        } else {
                            Log.d("DEBUG", "INSERTING NEW STUFF TO TIMELINE" + tweetArrayFromJson.size());

//                            for(int x = tweetArrayFromJson.size() - 1; x >= 0; --x) {
//                                Log.d("DEBUG", "PREPENDING" + tweetArrayFromJson.get(x).getUser().getScreenName() + tweetArrayFromJson.get(x).getBody());
//                                tweets.add(0, tweetArrayFromJson.get(x));
//                                tweetsArrayAdapter.notifyItemInserted(0);
//                                rvTimeline.scrollToPosition(0);
//                            }

                            fetchNewAfterInitialLoad = false;
                            // swipeContainer.setRefreshing(false);
                        }
                        // Log.d("DEBUG", tweets.size() + "size");
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
                        // showToast("Oops! Something went wrong..Please try again after some time");

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
}
