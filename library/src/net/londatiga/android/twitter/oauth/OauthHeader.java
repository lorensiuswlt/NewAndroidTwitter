package net.londatiga.android.twitter.oauth;

import net.londatiga.android.twitter.util.URIUtil;

/**
 * Oauth authentication header creator.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 *
 */
public class OauthHeader {
	
	public static String buildRequestTokenHeader(String callbackUrl, String consumerKey, String nonce, String signature, 
			String signatureMethod, String timestamp, String version) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Oauth ");
		sb.append("oauth_callback=");
		sb.append("\"");
		sb.append(URIUtil.encode(callbackUrl));
		sb.append("\", ");
		
		sb.append("oauth_consumer_key=");
		sb.append("\"");
		sb.append(URIUtil.encode(consumerKey));
		sb.append("\", ");
		
		sb.append("oauth_nonce=");
		sb.append("\"");
		sb.append(URIUtil.encode(nonce));
		sb.append("\", ");
		
		sb.append("oauth_signature=");
		sb.append("\"");
		sb.append(URIUtil.encode(signature));
		sb.append("\", ");
		
		sb.append("oauth_signature_method=");
		sb.append("\"");
		sb.append(URIUtil.encode(signatureMethod));
		sb.append("\", ");
		
		sb.append("oauth_timestamp=");
		sb.append("\"");
		sb.append(URIUtil.encode(timestamp));
		sb.append("\", ");
		
		sb.append("oauth_version=");
		sb.append("\"");
		sb.append(URIUtil.encode(version));
		sb.append("\"");
		
		return sb.toString();
	}
	
	public static String buildRequestHeader(String consumerKey, String nonce, String signature, String signatureMethod, 
			String timestamp, String token, String verifier, String version) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Oauth ");
		
		sb.append("oauth_consumer_key=");
		sb.append("\"");
		sb.append(URIUtil.encode(consumerKey));
		sb.append("\", ");
		
		sb.append("oauth_nonce=");
		sb.append("\"");
		sb.append(URIUtil.encode(nonce));
		sb.append("\", ");
		
		sb.append("oauth_signature=");
		sb.append("\"");
		sb.append(URIUtil.encode(signature));
		sb.append("\", ");
		
		sb.append("oauth_signature_method=");
		sb.append("\"");
		sb.append(URIUtil.encode(signatureMethod));
		sb.append("\", ");
		
		sb.append("oauth_timestamp=");
		sb.append("\"");
		sb.append(URIUtil.encode(timestamp));
		sb.append("\", ");
		
		sb.append("oauth_token=");
		sb.append("\"");
		sb.append(URIUtil.encode(token));
		sb.append("\", ");
		
		sb.append("oauth_verifier=");
		sb.append("\"");
		sb.append(URIUtil.encode(verifier));
		sb.append("\", ");
		
		sb.append("oauth_version=");
		sb.append("\"");
		sb.append(URIUtil.encode(version));
		sb.append("\"");
		
		return sb.toString();
	}
	
	public static String buildRequestHeader(String consumerKey, String nonce, String signature, String signatureMethod,
			String timestamp, String token,  String version) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Oauth ");
		
		sb.append("oauth_consumer_key=");
		sb.append("\"");
		sb.append(URIUtil.encode(consumerKey));
		sb.append("\", ");
		
		sb.append("oauth_nonce=");
		sb.append("\"");
		sb.append(URIUtil.encode(nonce));
		sb.append("\", ");
		
		sb.append("oauth_signature=");
		sb.append("\"");
		sb.append(URIUtil.encode(signature));
		sb.append("\", ");
		
		sb.append("oauth_signature_method=");
		sb.append("\"");
		sb.append(URIUtil.encode(signatureMethod));
		sb.append("\", ");
		
		sb.append("oauth_timestamp=");
		sb.append("\"");
		sb.append(URIUtil.encode(timestamp));
		sb.append("\", ");
		
		sb.append("oauth_token=");
		sb.append("\"");
		sb.append(URIUtil.encode(token));
		sb.append("\", ");
		
		sb.append("oauth_version=");
		sb.append("\"");
		sb.append(URIUtil.encode(version));
		sb.append("\"");
		
		return sb.toString();
	}
}