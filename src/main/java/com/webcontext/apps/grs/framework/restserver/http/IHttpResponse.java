package com.webcontext.apps.grs.framework.restserver.http;

public interface IHttpResponse {

	/**
	 * @return the encodage
	 */
	public abstract String getEncodage();

	/**
	 * @return the mimeType
	 */
	public abstract String getMimeType();

}