package net.londatiga.android.twitter.http;

import java.util.TreeSet;

/**
 * Http parameter values.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 *
 */
public class HttpValues {
	private TreeSet<String> mValues;
	
	public HttpValues() {
		mValues = new TreeSet<String>();
	}
	
	public HttpValues(String value) {
		mValues = new TreeSet<String>();
		
		mValues.add(value);
	}
	
	public void add(String value) {
		mValues.add(value);
	}

	public void remove(String value) {
		mValues.remove(value);
	}

	public void clear() {
		mValues.clear();
	}
	
	public boolean isEmpty() {
		return mValues.isEmpty();
	}
	
	public TreeSet<String> getAll() {
		return mValues;
	}
 }