package net.londatiga.android.example;

import net.londatiga.android.twitter.*;
import net.londatiga.android.twitter.oauth.OauthAccessToken;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends BaseActivity {
	private Twitter mTwitter;
	
	public static final String CONSUMER_KEY = "your consumer key";
	public static final String CONSUMER_SECRET = "your consumer secret";
	public static final String CALLBACK_URL = "your callback here";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		mTwitter = new Twitter(this, CONSUMER_KEY, CONSUMER_SECRET, CALLBACK_URL);
		
		if (mTwitter.sessionActive()) {
			startActivity(new Intent(this, UserActivity.class));
			
			finish();
		} else {
			((Button) findViewById(R.id.btn_connect)).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					signinTwitter();
				}
			});
		}		
	}

	private void signinTwitter() {
		mTwitter.signin(new Twitter.SigninListener() {				
			@Override
			public void onSuccess(OauthAccessToken accessToken, String userId, String screenName) {
				getCredentials();
			}
			
			@Override
			public void onError(String error) {
				showToast(error);
			}
		});
	}
	
	private void getCredentials() {
		final ProgressDialog progressDlg = new ProgressDialog(this);
		
		progressDlg.setMessage("Getting credentials...");
		progressDlg.setCancelable(false);
		
		progressDlg.show();
		
		TwitterRequest request = new TwitterRequest(mTwitter.getConsumer(), mTwitter.getAccessToken());
		
		request.verifyCredentials(new TwitterRequest.VerifyCredentialListener() {
			
			@Override
			public void onSuccess(TwitterUser user) {
				progressDlg.dismiss();
				
				showToast("Hello " + user.name);
				
				saveCredential(user.screenName, user.name, user.profileImageUrl);
				
				startActivity(new Intent(getActivity(), UserActivity.class));
				
				finish();
			}
			
			@Override
			public void onError(String error) {
				progressDlg.dismiss();
				
				showToast(error);
			}
		});
	}
}