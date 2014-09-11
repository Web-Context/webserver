/**
 * 
 */
package com.webcontext.apps.gameslibrary.application.rest;

import java.util.List;

import org.apache.log4j.Logger;

import com.sun.net.httpserver.Headers;
import com.webcontext.apps.gameslibrary.application.models.Game;
import com.webcontext.apps.gameslibrary.application.models.Platform;
import com.webcontext.apps.gameslibrary.application.rest.GamesRestHandler;
import com.webcontext.framework.appserver.model.MDBEntity;
import com.webcontext.framework.appserver.services.persistence.DataManager;
import com.webcontext.framework.appserver.services.web.response.handler.ContextHandler;
import com.webcontext.framework.appserver.services.web.response.handler.impl.rest.RestHandler;
import com.webcontext.framework.appserver.services.web.response.handler.impl.rest.RestResponse;
import com.webcontext.framework.appserver.services.web.response.io.HttpRequest;
import com.webcontext.framework.appserver.services.web.server.GenericServer;
import com.webcontext.framework.appserver.services.web.server.GenericServer.HttpStatus;

/**
 * This is a sample implementation of a RestHandler on this GenericServer. It
 * provides JSON document on some basic request over a video games database. It
 * serves JSON on the "rest/games" URI path.
 * 
 * @author Frédéric Delorme<frederic.delorme@web-context.com>
 * 
 */
@SuppressWarnings("restriction")
@ContextHandler(path = "/rest/games")
public class GamesRestHandler extends RestHandler {
	private static final Logger LOGGER = Logger
			.getLogger(GamesRestHandler.class);

	/**
	 * Default constructor initializing the parent TestServer attribute.
	 * 
	 * @param server
	 */
	public GamesRestHandler(GenericServer server) {
		super(server);
	}

	@Override
	protected boolean authorized(Headers request) {
		return true;
	}

	/**
	 * Perform GET HTTP request processing. Generate a specific JSON random
	 * valued message.
	 */
	@Override
	public HttpStatus get(HttpRequest request, RestResponse response) {
		String title = null, platform = null;
		Integer pageSize = 0, offset = 0;

		try {
			title = (String) request.getParameter("title", String.class, "");

			platform = (String) request.getParameter("platform", String.class,
					"");

			pageSize = (Integer) request.getParameter("pageSize",
					Integer.class, "10");
			offset = (Integer) request.getParameter("offset", Integer.class,
					"0");

			LOGGER.debug(String.format(
					"Parameters title=%s, version=%s, pageSize=%d", title,
					platform, pageSize));

			if (pageSize > 0 && title != null && platform != null) {

				String filter = buildfilter(title, platform);

				List<Class<? extends MDBEntity>> games = DataManager
						.getRepository(Game.class).find(filter, offset,
								pageSize);
				response.addObject("games", games);

				List<Class<? extends MDBEntity>> platforms = DataManager
						.getRepository(Platform.class).find();
				response.addObject("platforms", platforms);

				LOGGER.debug(String.format(
						"retrieve %d games and %d platforms", games.size(),
						platforms.size()));
				return HttpStatus.OK;
			} else {
				response.addObject("error", "Unable to retrieve data");
				return HttpStatus.NOT_FOUND;
			}
		} catch (InstantiationException e) {
			LOGGER.error("Unable to retrieve data", e);
			return HttpStatus.INTERNAL_ERROR;
		} catch (IllegalAccessException e) {
			LOGGER.error("Unable to retrieve data", e);
			return HttpStatus.INTERNAL_ERROR;
		} catch (Exception e) {
			LOGGER.error("Unable to retrieve data", e);
			return HttpStatus.INTERNAL_ERROR;
		}
	}

	/**
	 * Build the sub parameter's filter. Filter is built on <code>title</code>
	 * and <code>platform</code>, only if each of those is not null or an empty
	 * string.
	 * 
	 * @param title
	 *            Game title to search for.
	 * @param platform
	 *            game platform to search in.
	 * @return
	 */
	private String buildfilter(String title, String platform) {
		StringBuilder sb = new StringBuilder("{");
		if (title != null && !title.equals("")) {
			sb.append("\"title\": {$in : [\"").append(title).append("\"]}");
		}
		if (platform != null && !platform.equals("")) {
			if (!sb.toString().equals("{")) {
				sb.append(",");
			}
			sb.append("\"platform\": {$in : [\"").append(platform)
					.append("\"]}");

		}
		sb.append("}");
		return sb.toString();
	}

}
