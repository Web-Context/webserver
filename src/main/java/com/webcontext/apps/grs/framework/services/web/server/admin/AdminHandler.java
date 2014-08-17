/**
 * 
 */
package com.webcontext.apps.grs.framework.services.web.server.admin;

import java.io.IOException;

import com.webcontext.apps.grs.framework.services.web.response.handler.impl.rest.RestHandler;
import com.webcontext.apps.grs.framework.services.web.response.handler.impl.rest.RestResponse;
import com.webcontext.apps.grs.framework.services.web.response.io.HttpRequest;
import com.webcontext.apps.grs.framework.services.web.server.GenericServer;
import com.webcontext.apps.grs.framework.services.web.server.GenericServer.HttpStatus;

/**
 * This is the internal ADMIN handler to perform some administrative task on
 * server like stopping it.
 * 
 * @author Frédéric Delorme<frederic.delorme@web-context.com>
 * 
 */
public class AdminHandler extends RestHandler {

	public AdminHandler(GenericServer server) {
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
