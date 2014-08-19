/**
 * 
 */
package com.webcontext.apps.grs.application.rest;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.webcontext.framework.appserver.services.web.response.handler.impl.rest.RestHandler;
import com.webcontext.framework.appserver.services.web.response.handler.impl.rest.RestResponse;
import com.webcontext.framework.appserver.services.web.response.io.HttpRequest;
import com.webcontext.framework.appserver.services.web.server.GenericServer;
import com.webcontext.framework.appserver.services.web.server.GenericServer.HttpStatus;

/**
 * @author frederic
 * 
 */
public class PlatformRestHandler extends RestHandler {

	private static final Logger LOGGER = Logger
			.getLogger(PlatformRestHandler.class);

	/**
	 * @param server
	 */
	public PlatformRestHandler(GenericServer server) {
		super(server);
	}

	@Override
	public HttpStatus get(HttpRequest request, RestResponse response)
			throws IOException {
	
		return super.get(request, response);
	}
	
	
}
