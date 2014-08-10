/**
 * 
 */
package com.ge.monitoring.agent.restserver.internal.server;

import java.io.IOException;

import com.ge.monitoring.agent.restserver.internal.http.HttpRequest;
import com.ge.monitoring.agent.restserver.internal.rest.RestHandler;
import com.ge.monitoring.agent.restserver.internal.rest.RestResponse;
import com.ge.monitoring.agent.restserver.internal.server.RestServer.HttpStatus;

/**
 * @author frederic
 * 
 */
public class AdminHandler extends RestHandler {

	public AdminHandler(RestServer server) {
		super(server);
	}

	@Override
	public HttpStatus get(HttpRequest request, RestResponse response)
			throws IOException {
		if(request.getParameter("stop")!=null){
			server.stop();
		}
		return HttpStatus.ACCEPTED;
	}
}
