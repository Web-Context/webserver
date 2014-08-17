package com.webcontext.apps.grs.framework.services.web.response.io;

/**
 * This is the interface manipulated by the Handler to build HTTP Response.
 * 
 * @author Frédéric Delorme<frederic.delorme@web-context.com>
 * 
 */
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