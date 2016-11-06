package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.codepath.apps.mysimpletweets.ComposeTweetDialogFragment;
import com.codepath.apps.mysimpletweets.EndlessRecyclerViewScrollListener;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TweetsListFragment extends Fragment  {
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter tweetsArrayAdapter;
    private RecyclerView rvTimeline;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    private static final int gridNumOfColumns = 1;
    private boolean fetchNewAfterInitialLoad = false;
    private ComposeTweetDialogFragment composeTweetDialogFragment;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup parent,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        initialize(v);
        return v;
    }

    // creation lifecycle event
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void initialize(View v) {
        tweets = new ArrayList<>();
        tweetsArrayAdapter = new TweetsArrayAdapter(getActivity(), tweets);

        // Initialize Recycler view container Tweets
        rvTimeline = (RecyclerView) v.findViewById(R.id.rvTimeline);
        rvTimeline.setAdapter(tweetsArrayAdapter);

        staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(gridNumOfColumns,
                        StaggeredGridLayoutManager.VERTICAL);

        rvTimeline.setLayoutManager(staggeredGridLayoutManager);
        setRvScrollListener();
        // Done init-ing Recycler view
    }

    public void setRvScrollListener() {
        rvTimeline.addOnScrollListener(new EndlessRecyclerViewScrollListener(
                staggeredGridLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // populateTimeline();
            }
        });
    }

    // API METHODS BELOW
    public void addAll(ArrayList<Tweet> tweetArrayList) {
        tweets.addAll(tweetArrayList);
        tweetsArrayAdapter.notifyDataSetChanged();
    }

    public void addToTop(Tweet t) {
        tweets.add(0, t);
        tweetsArrayAdapter.notifyItemInserted(0);
        rvTimeline.scrollToPosition(0);
    }
    // API METHODS END

}
