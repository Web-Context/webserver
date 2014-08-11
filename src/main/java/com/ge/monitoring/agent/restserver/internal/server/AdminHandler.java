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
 * This is the internal ADMIN handler to perform some administrative task on
 * server like stopping it.
 * 
 * @author Frédéric Delorme<frederic.delorme@serphydose.com>
 * 
 */
public class AdminHandler extends RestHandler {

	public AdminHandler(RestServer server) {
		super(server);
	}

	/**
	 * Ask to stop the server.
	 */
	@Override
	public HttpStatus get(HttpRequest request, RestResponse response)
			throws IOException {
		try {
			if (request.getParameter("command", String.class, "no").equals(
					"stop")) {
				server.stop();
				return HttpStatus.OK;
			}
			if (request.getParameter("command", String.class, "no").equals(
					"info")) {
				response.add("info", server.getInfo());
				return HttpStatus.OK;
			}
		} catch (InstantiationException e) {
			return HttpStatus.INTERNAL_ERROR;
		} catch (IllegalAccessException e) {
			return HttpStatus.INTERNAL_ERROR;
		}
		return HttpStatus.BAD_REQUEST;
	}

	/**
	 * Administrative operation call does not modify server information.
	 */
	@Override
	protected void statistics(HttpStatus errCode, HttpRequest request) {
	}

	/**
	 * return some basic internal informations.
	 */
	@Override
	public HttpStatus post(HttpRequest request, RestResponse response)
			throws IOException {
		response.add("serverinfo", server.getInfo());
		return HttpStatus.OK;
	}
}
