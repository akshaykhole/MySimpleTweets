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

import com.codepath.apps.mysimpletweets.fragments.TweetsListFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements ComposeTweetDialogFragment.ComposeTweetDialogListener {
    private ComposeTweetDialogFragment composeTweetDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        setupImplicitIntentReceiver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline_menu, menu);
        return true;
    }

    public void composeNewTweet(MenuItem menuItem) {
        FragmentManager fm = getSupportFragmentManager();
        composeTweetDialogFragment = new ComposeTweetDialogFragment();
        composeTweetDialogFragment.show(fm, "NEW_TWEET_FRAGMENT");
    }

    @Override
    public void onFinishComposeTweet(String composedTweet) {
//        client.postTweet(composedTweet, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode,
//                                  Header[] headers,
//                                  JSONObject response) {
////                Tweet t = Tweet.fromJSON(response);
////                tweets.add(0, t);
////                tweetsArrayAdapter.notifyItemInserted(0);
////                rvTimeline.scrollToPosition(0);
//            }
//
//            @Override
//            public void onFailure(int statusCode,
//                                  Header[] headers,
//                                  Throwable throwable,
//                                  JSONObject errorResponse) {
//                Log.d("DEBUG", errorResponse.toString());
//            }
//        });
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