package com.codepath.apps.mysimpletweets;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {
    private TwitterClient client;
    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
    private GridView gvTimeline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = TwitterApplication.getRestClient(); // Gives us a singleton client
        populateTimeline();

        gvTimeline = (GridView) findViewById(R.id.gvTimeline);
        client = TwitterApplication.getRestClient();
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(this, tweets);
        gvTimeline.setAdapter(aTweets);
        populateTimeline();
    }

    // Send API request
    // Fill Listview
    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode,
                                  Header[] headers,
                                  JSONArray jsonArray) {

                Log.d("DEBUG", jsonArray.toString());

                aTweets.addAll(Tweet.fromJSONArray(jsonArray));
                aTweets.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode,
                                  Header[] headers,
                                  String responseString,
                                  Throwable throwable) {
                Log.d("DEBUG", responseString.toString());
            }
        });
    }
}
