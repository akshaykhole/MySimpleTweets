package com.codepath.apps.mysimpletweets;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = TwitterApplication.getRestClient(); // Gives us a singleton client
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
