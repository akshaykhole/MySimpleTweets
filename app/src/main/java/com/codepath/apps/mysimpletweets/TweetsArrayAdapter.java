package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.codepath.apps.mysimpletweets.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 10/28/16.
 */

public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {
    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }
}
