/**
 * 
 */
package com.webcontext.apps.grs.framework.restserver.server;

import java.io.IOException;

import com.webcontext.apps.grs.framework.restserver.http.HttpRequest;
import com.webcontext.apps.grs.framework.restserver.rest.RestResponse;
import com.webcontext.apps.grs.framework.restserver.rest.handler.RestHandler;
import com.webcontext.apps.grs.framework.restserver.server.RestServer.HttpStatus;

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
				response.addObject("info", server.getInfo());
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
		response.addObject("serverinfo", server.getInfo());
		return HttpStatus.OK;
	}
}
