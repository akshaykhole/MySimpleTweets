package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.mysimpletweets.EndlessRecyclerViewScrollListener;
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

public class MentionsTimelineFragment extends TweetsListFragment {
    private TwitterClient client;
    private boolean fetchNewAfterInitialLoad = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
        populateTimeline();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup parent,
                             @Nullable Bundle savedInstanceState) {

        View v = super.onCreateView(inflater, parent, savedInstanceState);

        getRvTimeline().addOnScrollListener(
                new EndlessRecyclerViewScrollListener(getStaggeredGridLayoutManager()) {

                    @Override
                    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                        populateTimeline();
                    }
                });
        return v;
    }

    public void initialize() {
        client = TwitterApplication.getRestClient();
    }

    private void populateTimeline() {
        client.getMentionsTimeline(fetchNewAfterInitialLoad,
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
}
