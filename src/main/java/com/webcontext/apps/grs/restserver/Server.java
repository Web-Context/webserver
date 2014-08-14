/**
 * 
 */
package com.webcontext.apps.grs.restserver;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.webcontext.apps.grs.framework.restserver.rest.handler.WebHandler;
import com.webcontext.apps.grs.framework.restserver.server.RestServer;
import com.webcontext.apps.grs.models.Game;
import com.webcontext.apps.grs.repository.GameRepository;
import com.webcontext.apps.grs.service.DataManager;

/**
 * This is the start class for our server.
 * 
 * @author Frédéric Delorme<frederic.delorme@web-context.com>
 *
 */
public class Server {

	private static Logger LOGGER = Logger.getLogger(Server.class);

	private static RestServer server;

	public static void main(String[] args) {

		int port = 0;
		String stopKey = "";
		try {
			port = RestServer.getIntArg(args, "port", 8888);
			stopKey = RestServer.getStringArg(args, "StopKey", "STOP");
			server = new RestServer(port, stopKey);
			DataManager.getInstance()
					.register(Game.class, GameRepository.class);

			server.addRestContext("/rest/games", new GamesRestHandler(server));

			server.start();
		} catch (IOException | InterruptedException | InstantiationException
				| IllegalAccessException e) {
			LOGGER.error("Unable to start the internal Rest HTTP Server component on port "
					+ port + ". Reason : " + e.getLocalizedMessage());
		}
		LOGGER.info("End of processing request in Server on port " + port);
		System.exit(0);
	}
}
