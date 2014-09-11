/**
 * 
 */
package com.webcontext.framework.appserver.services.web.response.io;

import java.io.OutputStream;

/**
 * @author Frédéric Delorme<frederic.delorme@web-context.com>
 * 
 */
public abstract class HttpResponse<T> implements IHttpResponse {

	protected String mimeType;
	protected String encodage;
	private OutputStream outputStream;

	/**
	 * @return the outputStream
	 */
	public OutputStream getOutputStream() {
		return outputStream;
	}

	protected T data;

	/**
	 * 
	 */
	public HttpResponse() {
		this.mimeType = "text/html";
	}

	/**
	 * 
	 */
	public HttpResponse(OutputStream outputStream) {
		this();
		this.outputStream = outputStream;
	}

	public void add(String mimeType, T data) {
		this.mimeType = mimeType;
		this.data = data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.webcontext.framework.appserver.services.web.http.IHttpResponse#getEncodage
	 * ()
	 */
	@Override
	public String getEncodage() {
		return encodage;
	}

	/**
	 * @param encodage
	 *            the encodage to set
	 */
	public void setEncodage(String encodage) {
		this.encodage = encodage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.webcontext.framework.appserver.services.web.http.IHttpResponse#getMimeType
	 * ()
	 */
	@Override
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

	public abstract void add(T data);

	/**
	 * Convert all objects in data list to one JSon Object to be send ton Http
	 * Response.
	 * 
	 * @return
	 */
	public abstract String process();
}
