package com.codepath.apps.mysimpletweets;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by akshay on 10/29/16.
 */

public class ComposeTweetDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {
    public ComposeTweetDialogFragment() {}

    private EditText etComposeTweet;
    private TextView tvTweetCharCount;
    private EditText etTweetBody;
    private Button sendTweet;

    private final TextWatcher tweetCharCountWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tvTweetCharCount.setText(String.valueOf(140 - s.length()));

            if(s.toString().length() > 140) {
                tvTweetCharCount.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public interface ComposeTweetDialogListener {
        void onFinishComposeTweet(String composedTweet);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.compose_new_tweet, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTweetCharCount = (TextView) view.findViewById(R.id.tvNumOfCharsRemaining);
        etComposeTweet = (EditText) view.findViewById(R.id.etTweetBody);
        etComposeTweet.setOnEditorActionListener(this);
        etComposeTweet.addTextChangedListener(tweetCharCountWatcher);

        etTweetBody = (EditText) view.findViewById(R.id.etTweetBody);

        // Get stuff from externally shared data
        String tweetBodyFromShared = "";

        if(getArguments() != null && getArguments().getString("url") != null) {
            tweetBodyFromShared += getArguments().getString("url");
        }

        if(getArguments() != null && getArguments().getString("title") != null) {
            tweetBodyFromShared += getArguments().getString("title");
        }

        if(!tweetBodyFromShared.isEmpty()) {
            Log.d("DEBUG", "body1 " + tweetBodyFromShared);
            etTweetBody.setText(tweetBodyFromShared);
        }

        Log.d("DEBUG", "body2 " + tweetBodyFromShared);

        etComposeTweet.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        sendTweet = (Button) view.findViewById(R.id.btnSendTweet);

        sendTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComposeTweetDialogListener listener = (ComposeTweetDialogListener) getActivity();
                listener.onFinishComposeTweet(etComposeTweet.getText().toString());
                dismiss();
            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        Log.d("DEBUG", etComposeTweet.getText().toString());

        if(EditorInfo.IME_ACTION_DONE == actionId) {
            ComposeTweetDialogListener listener = (ComposeTweetDialogListener) getActivity();
            listener.onFinishComposeTweet(etComposeTweet.getText().toString());
            dismiss();
            return true;
        }
        return false;
    }


}
