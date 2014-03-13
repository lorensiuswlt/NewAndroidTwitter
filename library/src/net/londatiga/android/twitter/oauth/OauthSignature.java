package net.londatiga.android.twitter.oauth;

import java.security.GeneralSecurityException;
import java.io.UnsupportedEncodingException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import net.londatiga.android.twitter.util.Base64;
import net.londatiga.android.twitter.util.Debug;
import net.londatiga.android.twitter.util.URIUtil;

/**
 * Oauth signature.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 *
 */
public class OauthSignature {
	public static final String MAC_NAME = "HmacSHA1";

	public String createSignatureBase(String method, String url, String queryString) {
		StringBuffer sb = new StringBuffer();
		
		sb.append(method);
		sb.append("&");
		sb.append(URIUtil.encode(url));
		sb.append("&");
		sb.append(URIUtil.encode(queryString));
		
		return sb.toString();
	}

	public String createRequestSignature(String signatureBase, String consumerSecret, String tokenSecret) 
		throws Exception {
		
		try {
            String keyString 	= URIUtil.encode(consumerSecret) + '&' + URIUtil.encode(tokenSecret);
            
            Debug.i("key " + keyString);
            
            byte[] keyBytes 	= keyString.getBytes("UTF-8");

            SecretKey key 		= new SecretKeySpec(keyBytes, MAC_NAME);
            Mac mac 			= Mac.getInstance(MAC_NAME);
            
            mac.init(key);

            byte[] text 		= signatureBase.getBytes("UTF-8");

            return Base64.encodeBytes(mac.doFinal(text)).trim();
        } catch (GeneralSecurityException e) {
            throw e;
        } catch (UnsupportedEncodingException e) {
            throw e;
        }
	}
}