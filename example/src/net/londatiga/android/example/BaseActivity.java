package net.londatiga.android.example;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class BaseActivity extends Activity {
	private SharedPreferences mSharedPref;
	
	private static final String SHARED_PREF = "android_twitter";
	private static final String USER_SCREEN_NAME = "tw_user_id";
	private static final String USER_NAME = "tw_user_name";
	private static final String USER_PIC = "tw_user_pic";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mSharedPref	= getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
	}
	
	public void saveCredential(String screenName, String name, String profilePic) {
		Editor editor = mSharedPref.edit();
		
		editor.putString(USER_SCREEN_NAME, screenName);
		editor.putString(USER_NAME, name);
		editor.putString(USER_PIC, profilePic);
		
		editor.commit();
	}
	
	public void clearCredential() {
		Editor editor = mSharedPref.edit();
		
		editor.putString(USER_SCREEN_NAME, "");
		editor.putString(USER_NAME, "");
		editor.putString(USER_PIC, "");
		
		editor.commit();
	}
	
	public String getScreenName() {
		return mSharedPref.getString(USER_SCREEN_NAME, "");
	}
	
	public String getUserName() {
		return mSharedPref.getString(USER_NAME, "");
	}
	
	public String getProfilePicture() {
		return mSharedPref.getString(USER_PIC, "");
	}
	
	public void showToast(String text) {
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
	}
	
	public Activity getActivity() {
		return this;
	}
}