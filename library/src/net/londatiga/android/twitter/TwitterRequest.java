package net.londatiga.android.twitter;

import java.net.URL;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.AsyncTask;

import net.londatiga.android.twitter.oauth.OauthAccessToken;
import net.londatiga.android.twitter.oauth.OauthConsumer;

import net.londatiga.android.twitter.http.HttpConn;
import net.londatiga.android.twitter.util.Cons;

/**
 * Twitter API request.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 *
 */
public class TwitterRequest {
	private OauthConsumer mConsumer;
	private OauthAccessToken mAccessToken;
	
	public TwitterRequest(OauthConsumer consumer, OauthAccessToken accessToken) {
		mConsumer		= consumer;
		mAccessToken	= accessToken;
	}
	
	public void createRequest(String method, String url,  List<NameValuePair> params, RequestListener listener) {
		new RequestTask(method, url, params, listener).execute();
	}
	
	public void verifyCredentials(VerifyCredentialListener listener) {
		new VerifyCredentialTask(listener).execute();
	}
	
	private class RequestTask extends AsyncTask<URL, Integer, Long> {
		String method, url, response = "";
		
		List<NameValuePair> params;
		
		RequestListener listener;
		
		public RequestTask(String method, String url,  List<NameValuePair> params, RequestListener listener) {
			this.method		= method;
			this.url		= url;
			this.params		= params;
			this.listener 	= listener;
		}
		
		protected void onCancelled() {
		}
		
    	protected void onPreExecute() {
    	}
    
        protected Long doInBackground(URL... urls) {         
            long result = 0;
            
            try {
            	 HttpConn conn 	= new HttpConn(mConsumer, mAccessToken);
            	 
            	 if (method.equals(HttpConn.REQUEST_GET)) {
            		 response	= conn.connectGet(url, params);
            	 } else {
            		 response	= conn.connectPost(url, params);
            	 }
            } catch (Exception e) { 
            	e.printStackTrace();
            }
            
            return result;
        }

        protected void onProgressUpdate(Integer... progress) {              	
        }

        protected void onPostExecute(Long result) {
        	if (!response.equals("")) {        		
        		listener.onSuccess(response);        		
        	} else {
        		listener.onError("Failed to process api request");
        	}
        }                
    }
	
	private class VerifyCredentialTask extends AsyncTask<URL, Integer, Long> {
		String response = "";
		
		VerifyCredentialListener listener;
		
		public VerifyCredentialTask(VerifyCredentialListener listener) {
			this.listener = listener;
		}
		
		protected void onCancelled() {
		}
		
    	protected void onPreExecute() {
    	}
    
        protected Long doInBackground(URL... urls) {         
            long result = 0;
            
            try {
            	 HttpConn conn 	= new HttpConn(mConsumer, mAccessToken);
            	 
            	 response		= conn.connectGet(Cons.API_VERIFY_CREDENTIAL_URL, null);
            } catch (Exception e) { 
            	e.printStackTrace();
            }
            
            return result;
        }

        protected void onProgressUpdate(Integer... progress) {              	
        }

        protected void onPostExecute(Long result) {
        	if (!response.equals("")) {
        		try {
        			JSONObject jsonObj 		= (JSONObject) new JSONTokener(response).nextValue();
        		
        			TwitterUser user		= new TwitterUser();
        			
        			user.userId				= jsonObj.getString("id");
        			user.screenName			= jsonObj.getString("screen_name");
        			user.name				= jsonObj.getString("name");
        			user.description		= jsonObj.getString("description");
        			user.location			= jsonObj.getString("location");
        			user.profileImageUrl	= jsonObj.getString("profile_image_url");
        			user.website			= jsonObj.getString("url");
        			user.followerCount		= jsonObj.getInt("followers_count");
        			
        			listener.onSuccess(user);
        		} catch (Exception e) {
        			listener.onError("Failed to get credentials");
        		}
        	} else {
        		listener.onError("Failed to get credentials");
        	}
        }                
    }
	
	public interface RequestListener {
		public abstract void onSuccess(String response);
		public abstract void onError(String error);
	}
	
	public interface VerifyCredentialListener {
		public abstract void onSuccess(TwitterUser user);
		public abstract void onError(String error);
	}
}