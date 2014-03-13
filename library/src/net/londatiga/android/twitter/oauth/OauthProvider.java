package net.londatiga.android.twitter.oauth;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import net.londatiga.android.twitter.http.HttpParams;
import net.londatiga.android.twitter.http.HttpValues;
import net.londatiga.android.twitter.util.Debug;
import net.londatiga.android.twitter.util.StringUtil;

/**
 * Oauth provider.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 *
 */
public class OauthProvider {
	private OauthConsumer mOauthConsumer;
	private OauthToken mOauthToken;
	private OauthAccessToken mAccessToken;
	
	private String mRequestTokenUrl;
	private String mAccessTokenUrl;
	private String mAuthorizationUrl;
	
	private String mScreenName = "";
	private String mUserId = "";
	
	public OauthProvider(OauthConsumer oauthConsumer, String requestTokenUrl, String authorizationUrl, String accessTokenUrl) {
		mOauthConsumer		= oauthConsumer;
		mRequestTokenUrl	= requestTokenUrl;
		mAuthorizationUrl	= authorizationUrl;
		mAccessTokenUrl		= accessTokenUrl;		
	}
	
	public String getAuthorizationUrl() throws Exception {
		String url = "";
		
		HttpParams httpParams		= new HttpParams();
		
		OauthSignature reqSignature	= new OauthSignature();
		
		String nonce				= OauthUtil.createNonce();
		String timestamp			= OauthUtil.getTimeStamp();
		
		httpParams.put("oauth_callback", 			new HttpValues(mOauthConsumer.getCallbackUrl()));
		httpParams.put("oauth_consumer_key", 		new HttpValues(mOauthConsumer.getConsumerKey()));
		httpParams.put("oauth_nonce", 				new HttpValues(nonce));
		httpParams.put("oauth_signature_method", 	new HttpValues(OauthUtil.SIGNATURE_METHOD));
		httpParams.put("oauth_timestamp", 			new HttpValues(timestamp));
		httpParams.put("oauth_version", 			new HttpValues(OauthUtil.OAUTH_VERSION));
		
		InputStream stream = null;
		
		try {
			String sigBase		= reqSignature.createSignatureBase("POST", mRequestTokenUrl, httpParams.getQueryString());
			String signature	= reqSignature.createRequestSignature(sigBase, mOauthConsumer.getConsumerSecret(), "");
			
			String authHeader	= OauthHeader.buildRequestTokenHeader(
									mOauthConsumer.getCallbackUrl(), 
									mOauthConsumer.getConsumerKey(), 
									nonce,
									signature, 
									OauthUtil.SIGNATURE_METHOD, 
									timestamp,
									OauthUtil.OAUTH_VERSION);
					
			Debug.i("Signature base " + sigBase);
			Debug.i("Signature " + signature);
			
			Debug.i("POST " + mRequestTokenUrl);
			Debug.i("Authorization " + authHeader);
			
			HttpClient httpClient 	= new DefaultHttpClient();
			HttpPost httpPost 		= new HttpPost(mRequestTokenUrl); 
			
			List<NameValuePair> params = new ArrayList<NameValuePair>(1);
			
			params.add(new BasicNameValuePair("oauth_callback", mOauthConsumer.getCallbackUrl()));
				
			httpPost.addHeader("Authorization", "OAuth " + authHeader);			
			httpPost.setEntity(new UrlEncodedFormEntity(params));
		        
			HttpResponse httpResponse 	= httpClient.execute(httpPost);
			
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity 		= httpResponse.getEntity();
					
				if (httpEntity == null) {
					throw new Exception("Return value is empty");
				}
					
				stream  			= httpEntity.getContent();			
				
				String response		= StringUtil.streamToString(stream);
				
				Debug.i("Response " + response);
				
				processRequestToken(response);
				
				if (mOauthToken == null) {
					throw new Exception("Failed to get request token");
				} else {
					url  = mAuthorizationUrl + "?oauth_token=" + mOauthToken.getToken(); 
				}
				
				stream.close();
			} else {
				throw new Exception(httpResponse.getStatusLine().getReasonPhrase());
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
		
		return url;
	}
	
	public OauthAccessToken retreiveAccessToken(String oauthVerifier) throws Exception {
		if (mOauthToken == null) {
			throw new Exception("Request token is empty, please call getAuthorizationUrl before calling this method");
		}
		
		HttpParams httpParams		= new HttpParams();
		
		OauthSignature reqSignature	= new OauthSignature();
		
		String nonce				= OauthUtil.createNonce();
		String timestamp			= OauthUtil.getTimeStamp();
		
		httpParams.put("oauth_verifier", 			new HttpValues(oauthVerifier));
		httpParams.put("oauth_consumer_key", 		new HttpValues(mOauthConsumer.getConsumerKey()));
		httpParams.put("oauth_nonce", 				new HttpValues(nonce));
		httpParams.put("oauth_signature_method", 	new HttpValues(OauthUtil.SIGNATURE_METHOD));
		httpParams.put("oauth_timestamp", 			new HttpValues(timestamp));
		httpParams.put("oauth_token", 				new HttpValues(mOauthToken.getToken()));
		httpParams.put("oauth_version", 			new HttpValues(OauthUtil.OAUTH_VERSION));
		
		InputStream stream = null;
		
		try {
			String sigBase		= reqSignature.createSignatureBase("POST", mAccessTokenUrl, httpParams.getQueryString());
			String signature	= reqSignature.createRequestSignature(sigBase, mOauthConsumer.getConsumerSecret(), mOauthToken.getSecret());
			
			String authHeader	= OauthHeader.buildRequestHeader( 
									mOauthConsumer.getConsumerKey(), 
									nonce,
									signature, 
									OauthUtil.SIGNATURE_METHOD, 
									timestamp,
									mOauthToken.getToken(),
									oauthVerifier,
									OauthUtil.OAUTH_VERSION);
					
			Debug.i("Signature base " + sigBase);
			Debug.i("Signature " + signature);
			
			Debug.i("POST " + mAccessTokenUrl);
			Debug.i("Authorization " + authHeader);
			
			HttpClient httpClient 	= new DefaultHttpClient();
			HttpPost httpPost 		= new HttpPost(mAccessTokenUrl); 
			
			List<NameValuePair> params = new ArrayList<NameValuePair>(1);
			
			params.add(new BasicNameValuePair("oauth_verifier", oauthVerifier));
				
			httpPost.addHeader("Authorization", "OAuth " + authHeader);			
			httpPost.setEntity(new UrlEncodedFormEntity(params));
		        
			HttpResponse httpResponse 	= httpClient.execute(httpPost);
			
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity 		= httpResponse.getEntity();
					
				if (httpEntity == null) {
					throw new Exception("Return value is empty");
				}
					
				stream  		= httpEntity.getContent();			
				
				String response	= StringUtil.streamToString(stream);
				
				Debug.i("Response " + response);
				
				processAccessToken(response);
				
				if (mAccessToken == null) {
					throw new Exception("Failed to get access token");
				}
			} else {
				throw new Exception(httpResponse.getStatusLine().getReasonPhrase());
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
		
		return mAccessToken;
	}
	
	public OauthToken getToken() {
		return mOauthToken;
	}
	
	public String getScreenName() {
		return mScreenName;
	}
	
	public String getUserId() {
		return mUserId;
	}
	
	private void processRequestToken(String response) {		
		if (response.contains("&")) {			
			String arrs[] 	= response.split("&");
			int length 		= arrs.length;
			
			String token	= "";
			String secret	= "";
			boolean confr	= true;
			
			for (int i = 0; i < length; i++) {
				String[] temp = arrs[i].split("=");
				
				if (temp[0].equals(OauthUtil.OAUTH_TOKEN)) {
					token = temp[1];
				}
				
				if (temp[0].equals(OauthUtil.OAUTH_TOKEN_SECRET)) {
					secret = temp[1];
				}
				
				if (temp[0].equals(OauthUtil.OAUTH_CALLBACK_CONFIRMED)) {
					confr = (temp[1].equals("true")) ? true : false;
				}				
			}
			
			if (!token.equals("") && !secret.equals("")) {
				mOauthToken = new OauthToken(token, secret, confr);
			}
		}
	}

	private void processAccessToken(String response) {		
		if (response.contains("&")) {			
			String arrs[] 	= response.split("&");
			int length 		= arrs.length;
			
			String token	= "";
			String secret	= "";
			
			for (int i = 0; i < length; i++) {
				String[] temp = arrs[i].split("=");
				
				if (temp[0].equals(OauthUtil.OAUTH_TOKEN)) {
					token = temp[1];
				}
				
				if (temp[0].equals(OauthUtil.OAUTH_TOKEN_SECRET)) {
					secret = temp[1];
				}
				
				if (temp[0].equals(OauthUtil.USER_ID)) {
					mUserId = temp[1];
				}
				
				if (temp[0].equals(OauthUtil.SCREEN_NAME)) {
					mScreenName = temp[1];
				}
			}
			
			if (!token.equals("") && !secret.equals("")) {
				mAccessToken = new OauthAccessToken(token, secret);
			}
		}
	}
}