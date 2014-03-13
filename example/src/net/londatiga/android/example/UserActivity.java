package net.londatiga.android.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import net.londatiga.android.twitter.Twitter;
import net.londatiga.android.twitter.TwitterRequest;
import net.londatiga.android.twitter.util.Debug;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class UserActivity extends BaseActivity {
	private Twitter mTwitter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_user);
		
		mTwitter = new Twitter(this, MainActivity.CONSUMER_KEY, MainActivity.CONSUMER_SECRET, MainActivity.CALLBACK_URL);
		
		((Button) findViewById(R.id.btn_logout)).setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				mTwitter.clearSession();
				
				clearCredential();
				
				startActivity(new Intent(getActivity(), MainActivity.class));
				
				finish();
			}
		});
		
		((TextView) findViewById(R.id.tv_name)).setText(getUserName());
		((TextView) findViewById(R.id.tv_username)).setText(getScreenName());
		
		final EditText tweetEt = (EditText) findViewById(R.id.et_message);
		
		((Button) findViewById(R.id.btn_post)).setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				String status = tweetEt.getText().toString();
				
				if (status.equals("")) {
					showToast("Please write your status");
					return;
				}
				
				updateStatus(status);
			}
		});
		
		ImageView userIv = (ImageView) findViewById(R.id.iv_user);
		
		DisplayImageOptions displayOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_user)
				.showImageForEmptyUri(R.drawable.ic_user)
				.showImageOnFail(R.drawable.ic_user)
				.cacheInMemory(true)
				.cacheOnDisc(false)
				.considerExifParams(true)
				.build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)                		                       
		        .writeDebugLogs()
		        .defaultDisplayImageOptions(displayOptions)		        
		        .build();
	
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);
		
		AnimateFirstDisplayListener animate  = new AnimateFirstDisplayListener();
		
		imageLoader.displayImage(getProfilePicture(), userIv, animate);
	}
	
	private void updateStatus(String status) {
		final ProgressDialog progressDlg = new ProgressDialog(this);
		
		progressDlg.setMessage("Sending...");
		progressDlg.setCancelable(false);
		
		progressDlg.show();
		
		TwitterRequest request 		= new TwitterRequest(mTwitter.getConsumer(), mTwitter.getAccessToken());
		
		String updateStatusUrl		= "https://api.twitter.com/1.1/statuses/update.json";
		
		List<NameValuePair> params 	= new ArrayList<NameValuePair>(1);
		
		params.add(new BasicNameValuePair("status", status));
		
		request.createRequest("POST", updateStatusUrl, params, new TwitterRequest.RequestListener() {
			
			@Override
			public void onSuccess(String response) {
				progressDlg.dismiss();
				
				showToast(response);
				
				Debug.i(response);
			}
			
			@Override
			public void onError(String error) {
				showToast(error);
				
				progressDlg.dismiss();
			}
		});
	}
	
	public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}