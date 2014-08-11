/**
 * 
 */
package com.webcontext.apps.grs.restserver;

import java.util.List;

import org.apache.log4j.Logger;

import com.webcontext.apps.grs.framework.model.MDBEntity;
import com.webcontext.apps.grs.framework.restserver.http.HttpRequest;
import com.webcontext.apps.grs.framework.restserver.rest.RestHandler;
import com.webcontext.apps.grs.framework.restserver.rest.RestResponse;
import com.webcontext.apps.grs.framework.restserver.server.RestServer;
import com.webcontext.apps.grs.framework.restserver.server.RestServer.HttpStatus;
import com.webcontext.apps.grs.models.Game;
import com.webcontext.apps.grs.service.DataManager;

/**
 * This is a sample implementation of this RestServer. It provides JSON document
 * on some basic request over a videogames database.
 * 
 * @author Frédéric Delorme<frederic.delorme@serphydose.com>
 * 
 */
public class GamesRestHandler extends RestHandler {
	private static final Logger LOGGER = Logger
			.getLogger(GamesRestHandler.class);

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
		int nb = 0;
		try {
			title = (String) request.getParameter("title", String.class,
					"no-title");

			platform = (String) request.getParameter("platform", String.class,
					"1.0");

			nb = (Integer) request.getParameter("nb", Integer.class, "10");

			LOGGER.debug(String.format(
					"Parameters title=%s, version=%s, nb=%d", title, platform,
					nb));

			if (nb != 0 && title != null && platform != null) {
				String filter = String
						.format("{\"platform\":\"%s\", \"title\":\"%s\", \"parameters\":{\"offset\":\"%d\",\"pageSize\":\"%d\"}}",
								platform, title, 0, nb);
				List<MDBEntity> games = DataManager.findAll(Game.class, filter);
				response.add("games", games);
				LOGGER.debug(String.format("retrieve nb=%d objects",
						games.size()));
				return HttpStatus.OK;
			} else {
				response.add("error", "Unable to retrieve data");
				return HttpStatus.NOT_FOUNT;
			}
		} catch (InstantiationException e) {
			return HttpStatus.INTERNAL_ERROR;
		} catch (IllegalAccessException e) {
			return HttpStatus.INTERNAL_ERROR;
		}
	}

}
