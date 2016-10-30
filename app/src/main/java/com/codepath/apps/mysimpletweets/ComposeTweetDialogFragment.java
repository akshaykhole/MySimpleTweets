package com.codepath.apps.mysimpletweets;

import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by akshay on 10/29/16.
 */

public class ComposeTweetDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {
    public ComposeTweetDialogFragment() {}

    private EditText etComposeTweet;

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
        etComposeTweet = (EditText) view.findViewById(R.id.etTweetBody);
        etComposeTweet.setOnEditorActionListener(this);

        getDialog().setTitle("Compose New Tweet");

        etComposeTweet.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
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
