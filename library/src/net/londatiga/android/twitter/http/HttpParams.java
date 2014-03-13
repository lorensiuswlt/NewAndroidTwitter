package net.londatiga.android.twitter.http;

import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Iterator;

import net.londatiga.android.twitter.util.URIUtil;

/**
 * Http parameters.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 *
 */
public class HttpParams {
	private TreeMap<String, HttpValues> mParams;
	
	public HttpParams() {
		mParams = new TreeMap<String, HttpValues>();
	}
	
	public void put(String key, HttpValues values) {
		if (!mParams.containsKey(key)) {
			mParams.put(key, values);
		}
	}

	public HttpValues get(String key) {
		return (mParams.containsKey(key)) ? mParams.get(key) : null;
	}

	public boolean containsKey(String key) {
		return mParams.containsKey(key);
	}
	
	public String getQueryString() {
		if (mParams.size() == 0) {
			return "";
		}
		
		StringBuffer querySb = new StringBuffer();
		int size = mParams.size();
		int i = 0;
		
		for (String key : mParams.keySet()) {
			HttpValues values = mParams.get(key);
			
			if (values != null) {
				if (!values.isEmpty()) {
					TreeSet<String> vals = values.getAll();
					Iterator<String> iterator = vals.iterator();
					
					while (iterator.hasNext()) { 
						querySb.append(key + "=" + URIUtil.encode((String) iterator.next()));
						
						if (iterator.hasNext()) {
							querySb.append("&");
						}
					}
				} else {
					querySb.append(key + "=");
				}
			} else {
				querySb.append(key + "=");
			}
			
			if (i != size-1) {
				querySb.append("&");
			}
			
			i++;
		}
		
		return querySb.toString();
	}
}