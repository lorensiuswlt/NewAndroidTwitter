package net.londatiga.android.twitter.oauth;

import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * Oauth utils.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 *
 */
public class OauthUtil {
	public static final String SIGNATURE_METHOD = "HMAC-SHA1";
	public static final String OAUTH_VERSION = "1.0";
	public static final String OAUTH_VERIFIER = "oauth_verifier";
	public static final String OAUTH_CALLBACK_CONFIRMED = "oauth_callback_confirmed";
	public static final String OAUTH_TOKEN = "oauth_token";
	public static final String OAUTH_TOKEN_SECRET = "oauth_token_secret";
	public static final String USER_ID = "user_id";
	public static final String SCREEN_NAME = "screen_name";
	
	public static final String OAUTH_REQUEST_URL = "https://api.twitter.com/oauth/request_token";	
	public static final String OAUTH_AUTHENTICATION_URL = "https://api.twitter.com/oauth/authenticate";
	public static final String OAUTH_AUTHORIZATION_URL = "https://api.twitter.com/oauth/authorize";
	public static final String OAUTH_ACCESSTOKEN_URL = "https://api.twitter.com/oauth/access_token";
	
	public static String createNonce() {
		String nonce = "";
		
		try {
			SecureRandom prng 	= SecureRandom.getInstance("SHA1PRNG");
			String randomNum	= String.valueOf(prng.nextInt());

			MessageDigest sha 	= MessageDigest.getInstance("SHA-1");
			byte[] result 		=  sha.digest( randomNum.getBytes() );
			nonce 				= OauthUtil.hexEncode(result);
		} catch (Exception e) {			
		}
		
	     return nonce;
	}
	
	public static String hexEncode( byte[] aInput){
	    StringBuilder result = new StringBuilder();
	    
	    char[] digits = {'0', '1', '2', '3', '4','5','6','7','8','9','a','b','c','d','e','f'};
	    
	    for ( int idx = 0; idx < aInput.length; ++idx) {
	      byte b = aInput[idx];
	      
	      result.append( digits[ (b&0xf0) >> 4 ] );
	      result.append( digits[ b&0x0f] );
	    }
	    
	    return result.toString();
	}
	
	public static String getTimeStamp() {
		return String.valueOf(System.currentTimeMillis()/1000);
	}
}