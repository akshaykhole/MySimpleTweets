package com.codepath.apps.mysimpletweets;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.fragments.UserTimelineFragment;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {
    TwitterClient client;
    TextView tvName;
    TextView tvTagline;
    TextView tvFollowerCount;
    TextView tvFollowingCount;
    ImageView ivProfileImage;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        client = TwitterApplication.getRestClient();
        // Get the screen name
        String screenName = getIntent().getStringExtra("screen_name");

        // Call end-point. Assume null means self profile
        if(screenName != null) {
            // Get User account info
            client.getOtherUserInfo(screenName, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    onSuccessFetchUserInfo(response);
                }
            });
        } else {
            // Get User account info
            client.getUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    onSuccessFetchUserInfo(response);
                }
            });
        }

        if(savedInstanceState == null) {
            // Create the user timeline fragment
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);
            // Display user fragment within this activity dynamically
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit();
        }
    }

    public void onSuccessFetchUserInfo(JSONObject userJsonObject) {
        user = User.fromJson(userJsonObject);
        // My current user account's info
        getSupportActionBar().setTitle("@" + user.getScreenName());
        populateProfileHeader(user);
    }

    public void populateProfileHeader(User user) {
        tvName = (TextView) findViewById(R.id.tvUserName);
        tvTagline = (TextView) findViewById(R.id.tvUserTagline);
        tvFollowerCount = (TextView) findViewById(R.id.tvFollowerCount);
        tvFollowingCount = (TextView) findViewById(R.id.tvFollowingCount);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);

        tvName.setText(user.getName());
        tvTagline.setText(user.getTagline());
        tvFollowerCount.setText(user.getFollowersCount() + " Followers");
        tvFollowingCount.setText(user.getFollowingsCount() + " Following");
    }
}
