package com.codepath.apps.mysimpletweets;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

/**
 * Created by akshay on 10/29/16.
 */

public class ComposeTweetDialogFragment extends DialogFragment {
    public ComposeTweetDialogFragment() {}

    private EditText etComposeTweet;

//    public static ComposeTweetDialogFragment newInstance(String title) {
//        ComposeTweetDialogFragment frag = new ComposeTweetDialogFragment();
//        Bundle args = new Bundle();
//        args.putString("title", title);
//        frag.setArguments(args);
//        return frag;
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.compose_new_tweet, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etComposeTweet = (EditText) view.findViewById(R.id.etTweetBody);
        getDialog().setTitle("Compose New Tweet");

        etComposeTweet.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}
