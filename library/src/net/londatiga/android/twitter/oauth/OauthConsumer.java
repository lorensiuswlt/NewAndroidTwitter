package net.londatiga.android.twitter.oauth;

/**
 * Oauth consumer.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 *
 */
public class OauthConsumer {
	private String consumerKey;
	private String consumerSecret;
	private String callbackUrl;
	
	public OauthConsumer(String consumerKey, String consumerSecret, String callbackUrl) {
		this.consumerKey 	= consumerKey;
		this.consumerSecret	= consumerSecret;
		this.callbackUrl	= callbackUrl;
	}
	
	public String getConsumerKey() {
		return consumerKey;
	}
	
	public String getConsumerSecret() {
		return consumerSecret;
	}
	
	public String getCallbackUrl() {
		return callbackUrl;
	}
}