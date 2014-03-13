package net.londatiga.android.twitter.oauth;

/**
 * Oauth access token.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 *
 */
public class OauthAccessToken {
	private String token;
	private String secret;
	
	public OauthAccessToken(String token, String secret) {
		this.token	= token;
		this.secret	= secret;
	}
	
	public String getToken() {
		return token;
	}
	
	public String getSecret() {
		return secret;
	}
}