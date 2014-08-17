/**
 * 
 */
package com.webcontext.apps.grs.rest;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.webcontext.apps.grs.framework.server.web.server.GenericServer;
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

	private static GenericServer server;

	public static void main(String[] args) {

		int port = 0;
		String stopKey = "";
		try {
			// initialize server.
			port = GenericServer.getIntArg(args, "port", 8888);
			stopKey = GenericServer.getStringArg(args, "StopKey", "STOP");
			server = new GenericServer(port, stopKey);

			// Add a new repository.
			DataManager.getInstance()
					.register(Game.class, GameRepository.class);

			// add a new Handler to the Rest Server.
			server.addRestContext("/rest/games", new GamesRestHandler(server));

			// and start server.
			server.start();

		} catch (IOException | InterruptedException | InstantiationException
				| IllegalAccessException e) {
			LOGGER.error("Unable to start the internal Rest HTTP Server component on port "
					+ port + ". Reason : " + e.getLocalizedMessage());
		}
		LOGGER.info("End of processing request in Server on port " + port);

		// Exit from server.
		System.exit(0);
	}
}
