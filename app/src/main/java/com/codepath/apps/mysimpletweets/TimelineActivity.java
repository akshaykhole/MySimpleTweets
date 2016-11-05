package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionsTimelineFragment;

public class TimelineActivity extends AppCompatActivity implements ComposeTweetDialogFragment.ComposeTweetDialogListener {
    private ComposeTweetDialogFragment composeTweetDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        setupImplicitIntentReceiver();

        // Get the viewpager
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        // Set viewpager adapter for the pager
        viewPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        // Find the pager sliding tab strip
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the pager tab strip to viewpager
        tabStrip.setViewPager(viewPager);
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

    public void onProfileView(MenuItem item) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    public void onProfileView(View v) {
        String screenName = (String) v.getTag();
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("screen_name", screenName);
        startActivity(i);
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

    // Adapter for Fragment Pager
    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = { "Home", "Mentions" };

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if(position == 0) {
                return new HomeTimelineFragment();
            } else if (position == 1) {
                return new MentionsTimelineFragment();
            } else {
                return null;
            }
        }

        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
}