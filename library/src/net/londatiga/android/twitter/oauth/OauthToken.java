package net.londatiga.android.twitter.oauth;

/**
 * Oauth request token.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 *
 */
public class OauthToken {
	private String token;
	private String secret;
	private boolean callbackConfirmed;
	
	public OauthToken(String token, String secret, boolean callbackConfirmed) {
		this.token				= token;
		this.secret				= secret;
		this.callbackConfirmed	= callbackConfirmed; 
	}
	
	public String getToken() {
		return token;
	}
	
	public String getSecret() {
		return secret;
	}
	
	public boolean callbackConfirmed() {
		return callbackConfirmed;
	}
}