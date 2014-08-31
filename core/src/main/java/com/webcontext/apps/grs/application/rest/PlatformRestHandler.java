/**
 * 
 */
package com.webcontext.apps.grs.application.rest;

import java.io.IOException;

import com.webcontext.framework.appserver.services.web.response.handler.ContextHandler;
import com.webcontext.framework.appserver.services.web.response.handler.impl.rest.RestHandler;
import com.webcontext.framework.appserver.services.web.response.handler.impl.rest.RestResponse;
import com.webcontext.framework.appserver.services.web.response.io.HttpRequest;
import com.webcontext.framework.appserver.services.web.server.GenericServer;
import com.webcontext.framework.appserver.services.web.server.GenericServer.HttpStatus;

/**
 * This specific context handler serves <code>Platform</code> JSON data on the
 * <code>"rest/platform"</code> URI path.
 * 
 * @author frederic
 * 
 */
@ContextHandler(path = "/rest/platform")
public class PlatformRestHandler extends RestHandler {

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
