package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static com.codepath.apps.mysimpletweets.ParseRelativeDate.getRelativeTimeAgo;

/**
 * Created by akshay on 10/28/16.
 */

public class TweetsArrayAdapter extends RecyclerView.Adapter<TweetsArrayAdapter.ViewHolder> {

    private List<Tweet> timelineTweets;
    private Context context;

    public TweetsArrayAdapter(Context iContext, ArrayList<Tweet> tweetArrayList) {
        context = iContext;
        timelineTweets = tweetArrayList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvCreatedAt;

        public ViewHolder(View itemView) {
            super(itemView);

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvCreatedAt = (TextView) itemView.findViewById(R.id.tvCreatedAt);
        }
    }

    // Inflate and create the Holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    // Populates data into view via ViewHolder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tweet tweet = timelineTweets.get(position);

        // Populate username
        TextView tvUsername = holder.tvUsername;
        tvUsername.setText("@" + tweet.getUser().getScreenName());

        // Populate Created At
        TextView tvCreatedAt = holder.tvCreatedAt;
        tvCreatedAt.setText(getRelativeTimeAgo(tweet.getCreatedAt()));

        // Populate body
        TextView tvBody = holder.tvBody;
        tvBody.setText(tweet.getBody());

        // Populate Profile Image
        ImageView ivProfileImage = holder.ivProfileImage;
        ivProfileImage.setImageResource(android.R.color.transparent);
        Glide.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return timelineTweets.size();
    }

    private Context getContext() {
        return context;
    }
}