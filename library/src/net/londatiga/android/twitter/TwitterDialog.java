package net.londatiga.android.twitter;

import java.net.URL;
import java.net.URLDecoder;

import net.londatiga.android.twitter.oauth.OauthUtil;
import net.londatiga.android.twitter.util.Debug;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import android.os.Build;
import android.os.Bundle;

import android.content.Context;

import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

/**
 * Twitter authorization dialog.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 *
 */
@SuppressLint("NewApi")
public class TwitterDialog extends Dialog {
    static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                         						ViewGroup.LayoutParams.MATCH_PARENT);
    static final int MARGIN = 4;
    static final int PADDING = 2;

    private String mUrl;
    private String mCallbackUrl;
    
    private TwDialogListener mListener;
    private ProgressDialog mSpinner;
    private WebView mWebView;
    private LinearLayout mContent;
    private TextView mTitle;

    public TwitterDialog(Context context, String callback, String url, TwDialogListener listener) {
        super(context);
        
        mCallbackUrl= callback;
        mUrl 		= url;
        mListener 	= listener;
    }

	@SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSpinner = new ProgressDialog(getContext());
        
        mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpinner.setMessage("Loading...");

        mContent = new LinearLayout(getContext());
        
        mContent.setOrientation(LinearLayout.VERTICAL);
        
        setUpTitle();
        setUpWebView();
        
        Display display 	= getWindow().getWindowManager().getDefaultDisplay();
		Point outSize		= new Point();
		
		int width			= 0;
		int height			= 0;
		
		double[] dimensions = new double[2];
		        
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			display.getSize(outSize);
			
			width	= outSize.x;
			height	= outSize.y;
		} else {
			width	= display.getWidth();
			height	= display.getHeight();
		}
		
		if (width < height) {
			dimensions[0]	= 0.87 * width;
	        dimensions[1]	= 0.82 * height;
		} else {
			dimensions[0]	= 0.75 * width;
			dimensions[1]	= 0.75 * height;	        
		}
        
        addContentView(mContent, new FrameLayout.LayoutParams((int) dimensions[0], (int) dimensions[1]));
    }

    private void setUpTitle() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        Drawable icon = getContext().getResources().getDrawable(R.drawable.twitter_icon);
        
        mTitle = new TextView(getContext());
        
        mTitle.setText("Twitter");
        mTitle.setTextColor(Color.WHITE);
        mTitle.setTypeface(Typeface.DEFAULT_BOLD);
        mTitle.setBackgroundColor(0xFFbbd7e9);
        mTitle.setPadding(MARGIN + PADDING, MARGIN, MARGIN, MARGIN);
        mTitle.setCompoundDrawablePadding(MARGIN + PADDING);
        mTitle.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
        
        mContent.addView(mTitle);
    }

    @SuppressLint("SetJavaScriptEnabled")
	private void setUpWebView() {
    	CookieSyncManager.createInstance(getContext()); 
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        
        mWebView = new WebView(getContext());
        
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebViewClient(new TwitterWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mUrl);
        mWebView.setLayoutParams(FILL);
        
        mContent.addView(mWebView);
    }

    private void processVerifier(String callbackUrl) {
		String verifier	 = "";
		
		try {
			URL url 		= new URL(callbackUrl);
			String query 	= url.getQuery();
		
			String array[]	= query.split("&");

			for (String parameter : array) {
	             String v[] = parameter.split("=");
	             
	             if (URLDecoder.decode(v[0], "UTF-8").equals(OauthUtil.OAUTH_VERIFIER)) {
	            	 verifier = URLDecoder.decode(v[1], "UTF-8");
	            	 break;
	             }
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mListener.onComplete(verifier);
	}
    
    private class TwitterWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
        	Debug.i("Redirecting URL " + url);
        	
        	if (url.startsWith(mCallbackUrl)) {
        		processVerifier(url);
        		
        		TwitterDialog.this.dismiss();
        		
        		return true;
        	}  else if (url.startsWith("authorize")) {
        		return false;
        	}
        	
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        	Debug.i("Page error: " + description);
        	
            super.onReceivedError(view, errorCode, description, failingUrl);
      
            mListener.onError(description);
            
            TwitterDialog.this.dismiss();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
        	Debug.i("Loading URL: " + url);
            super.onPageStarted(view, url, favicon);
            mSpinner.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            String title = mWebView.getTitle();
            if (title != null && title.length() > 0) {
                mTitle.setText(title);
            }
            mSpinner.dismiss();
        }

    }
    
    public interface TwDialogListener {
		public void onComplete(String verifier);		
		public void onError(String value);
	}
}