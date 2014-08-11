/**
 * 
 */
package com.ge.monitoring.agent.restserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ge.monitoring.agent.models.MockupData;
import com.ge.monitoring.agent.restserver.internal.http.HttpRequest;
import com.ge.monitoring.agent.restserver.internal.rest.RestHandler;
import com.ge.monitoring.agent.restserver.internal.rest.RestResponse;
import com.ge.monitoring.agent.restserver.internal.server.RestServer;
import com.ge.monitoring.agent.restserver.internal.server.RestServer.HttpStatus;

/**
 * This is a sample implementation of this RestServer. It provides JSON document
 * on some basic request.
 * 
 * @author Frédéric Delorme<frederic.delorme@serphydose.com>
 * 
 */
public class MeasuresRestHandler extends RestHandler {
	private static final Logger LOGGER = Logger
			.getLogger(MeasuresRestHandler.class);

	public MeasuresRestHandler(RestServer server) {
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

		String title = null, version = null;
		int nb = 0;
		try {
			title = (String) request.getParameter("title", String.class,
					"no-title");

			version = (String) request.getParameter("version", "1.0").toArray()[0];
			nb = Integer.parseInt((String) request.getParameter("nb", "0")
					.toArray()[0]);

			LOGGER.debug(String.format(
					"Parameters title=%s, version=%s, nb=%d", title, version,
					nb));

			if (nb != 0 && title != null && version != null) {
				List<MockupData> measures = new ArrayList<MockupData>();
				for (int i = 0; i < nb; i++) {
					measures.add(new MockupData(title, version, (int) (Math
							.random() * 10000)));
				}
				response.add("measures", measures);
				LOGGER.debug(String.format("created nb=%d objects", nb));
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

	public static void main(String[] args) {

		RestServer server;
		int port = 0;
		String stopKey = "";
		try {
			port = RestServer.getIntArg(args, "port", 8888);
			stopKey = RestServer.getStringArg(args, "StopKey", "STOP");
			server = new RestServer(port, stopKey);
			server.addContext("/rest/instruments", new MeasuresRestHandler(
					server));
			server.start();
		} catch (IOException | InterruptedException e) {
			LOGGER.error("Unable to start the internal Rest HTTP Server component on port "
					+ port + ". Reason : " + e.getLocalizedMessage());
		}
		LOGGER.info("End of processing request in Server on port " + port);
		System.exit(0);
	}
}
