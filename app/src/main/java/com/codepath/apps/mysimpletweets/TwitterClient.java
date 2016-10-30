package com.codepath.apps.mysimpletweets;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;
import android.content.Context;
import android.util.Log;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;


//Access level	Read and write (modify app permissions)
//		Consumer Key (API Key)	CqRB8kgsuUntsdnGR57oSRGzV (manage keys and access tokens)
//		Callback URL	http://akshaykhole.com
//		Callback URL Locked	No
//		Sign in with Twitter	Yes
//		App-only authentication	https://api.twitter.com/oauth2/token
//		Request token URL	https://api.twitter.com/oauth/request_token
//		Authorize URL	https://api.twitter.com/oauth/authorize
//		Access token URL	https://api.twitter.com/oauth/access_token

//Consumer Key (API Key)	CqRB8kgsuUntsdnGR57oSRGzV
//        Consumer Secret (API Secret)	9m0F3KhOA1XzhFnVAQzoQIyYomm8SWc8HOyi13Q6FbpL5aLQqx
//        Access Level	Read and write (modify app permissions)
//        Owner	_akshaykhole
//        Owner ID	2432446208

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */

public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "CqRB8kgsuUntsdnGR57oSRGzV";       // Change this
	public static final String REST_CONSUMER_SECRET = "9m0F3KhOA1XzhFnVAQzoQIyYomm8SWc8HOyi13Q6FbpL5aLQqx"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)
    private static final Integer numOfTweetsToFetchOnEveryReqeust = 50;

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

    public void getHomeTimeline(boolean fetchNewAfterInitialLoad, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", numOfTweetsToFetchOnEveryReqeust);
        Log.d("DEBUG", fetchNewAfterInitialLoad + "");

        if (fetchNewAfterInitialLoad) {

            params.put("since_id", Tweet.maxTweetId);

        } else {
            params.put("since_id", "1");

            // Sending Long.MAX_VALUE crashes the twitter API
            String maxTweetId;

            if (Tweet.minTweetId == Long.MAX_VALUE) {
                maxTweetId = "9223372036854775000";
            } else {
                maxTweetId = Tweet.minTweetId + "";
            }
            params.put("max_id", maxTweetId);
        }

        getClient().get(apiUrl, params, handler);
    }

    public void postTweet(String tweetBody, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", tweetBody);
        getClient().post(apiUrl, params, handler);
    }

    public void getSelfProfile(AsyncHttpResponseHandler handler) {

    }
}
