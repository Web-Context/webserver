/**
 * 
 */
package com.webcontext.apps.grs.framework.restserver.http;

/**
 * @author 212391884
 *
 */
public abstract class HttpResponse<T> {

	protected String mimeType;

	/**
	 * @return the mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * @param mimeType
	 *            the mimeType to set
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	protected T data;

	/**
	 * 
	 */
	public HttpResponse() {
		this.mimeType = "text/html";
	}

	public void add(String mimeType, T data) {
		this.mimeType = mimeType;
		this.data = data;
	}

	public abstract void add(T data);

	/**
	 * Convert all objects in data list to one JSon Object to be send ton Http
	 * Response.
	 * 
	 * @return
	 */
	public abstract String process();
}
