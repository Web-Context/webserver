/**
 * 
 */
package com.webcontext.apps.grs.restserver;

import java.util.List;

import org.apache.log4j.Logger;

import com.webcontext.apps.grs.framework.model.MDBEntity;
import com.webcontext.apps.grs.framework.restserver.http.HttpRequest;
import com.webcontext.apps.grs.framework.restserver.rest.RestResponse;
import com.webcontext.apps.grs.framework.restserver.rest.handler.RestHandler;
import com.webcontext.apps.grs.framework.restserver.server.RestServer;
import com.webcontext.apps.grs.framework.restserver.server.RestServer.HttpStatus;
import com.webcontext.apps.grs.models.Game;
import com.webcontext.apps.grs.service.DataManager;

/**
 * This is a sample implementation of a RestHandler on this RestServer. It
 * provides JSON document on some basic request over a videogames database.
 * 
 * @author Frédéric Delorme<frederic.delorme@serphydose.com>
 * 
 */
public class GamesRestHandler extends RestHandler {
	private static final Logger LOGGER = Logger
			.getLogger(GamesRestHandler.class);

	/**
	 * Default constructor initializing the parent Server attribute.
	 * 
	 * @param server
	 */
	public GamesRestHandler(RestServer server) {
		super(server);
	}

	/**
	 * Perform GET HTTP request processing. Generate a specific JSON random
	 * valued message.
	 */
	@Override
	public HttpStatus get(HttpRequest request, RestResponse response) {

		// String title = (String) request.getParameter("title",
		// "no-title").toArray()[0];

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
				String filter = String
				// {\"title\": {$in : [\"%s\"]},
				// \"parameters\":{\"offset\":\"%d\",\"pageSize\":\"%d\"}}
						.format("{" + subfilter(title, platform) + "}");
				List<MDBEntity> games = DataManager.findAll(Game.class, filter,
						offset, pageSize);
				response.addObject("games", games);
				LOGGER.debug(String.format("retrieve nb=%d objects",
						games.size()));
				return HttpStatus.OK;
			} else {
				response.addObject("error", "Unable to retrieve data");
				return HttpStatus.NOT_FOUNT;
			}
		} catch (InstantiationException e) {
			return HttpStatus.INTERNAL_ERROR;
		} catch (IllegalAccessException e) {
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
	private String subfilter(String title, String platform) {
		StringBuilder sb = new StringBuilder();
		if (title != null && !title.equals("")) {
			sb.append("\"title\": {$in : [\"").append(title).append("\"]}");
		}
		if (platform != null && !platform.equals("")) {
			sb.append(",\"platform\": {$in : [\"").append(platform)
					.append("\"]}");

		}
		return sb.toString();
	}

}
