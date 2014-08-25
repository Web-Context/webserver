/**
 * 
 */
package com.webcontext.framework.appserver.services.web.response.handler.impl.rest;

import java.io.OutputStream;

import org.apache.log4j.Logger;

import com.sun.net.httpserver.Headers;
import com.webcontext.framework.appserver.services.web.response.handler.ResponseHandler;
import com.webcontext.framework.appserver.services.web.server.GenericServer;
import com.webcontext.framework.appserver.services.web.server.GenericServer.HttpMethod;

/**
 * The RestHandler class is a specific Handler to perform processing of HTTP
 * request, depending on used <code>{@link HttpMethod}</code>.
 * 
 * it will call the right method according to the HTTP method:
 * <ul>
 * <li><code>get(...)</code> : perform processing of Http <code>GET</code>
 * request</li>
 * <li><code>post(...)</code> : perform processing of Http <code>POST</code>
 * request</li>
 * <li><code>put(...)</code> : perform processing of Http <code>PUT</code>
 * request</li>
 * <li><code>delete(...)</code> : perform processing of Http <code>DELETE</code>
 * request</li>
 * <li><code>options(...)</code> : perform processing of Http
 * <code>OPTIONS</code> request</li>
 * <li><code>head(...)</code> : perform processing of Http <code>HEAD</code>
 * request</li>
 * </ul>
 * 
 * @author Fréderic Delorme<frederic.delorme@web-context.com>
 * 
 */
@SuppressWarnings("restriction")
public class RestHandler extends ResponseHandler<RestResponse> {

	private static final Logger LOGGER = Logger.getLogger(RestHandler.class);

	private static final String apiKey = "123456789ABCDEF";

	/**
	 * Initialize RestHandler with the managing server.
	 * 
	 * @param server
	 */
	public RestHandler(GenericServer server) {
		super(server);

	}

	@Override
	protected boolean authorized(Headers request) {
		if ((request.containsKey("Api-key") && request.getFirst("Api-key")
				.equals(apiKey)) || apiKey.equals("")) {
			return true;
		}
		return false;
	}

	@Override
	protected String processResponse(RestResponse response) {

		// Build JSON response object.
		String strResponse = response.process();
		LOGGER.debug("Serialize response object to a JSON structure:"
				+ strResponse);
		return strResponse;
	}

	@Override
	public RestResponse createResponse(OutputStream outputStream) {
		return new RestResponse(outputStream);
	}

}
