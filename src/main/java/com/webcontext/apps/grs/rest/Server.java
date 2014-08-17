/**
 * 
 */
package com.webcontext.apps.grs.rest;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.webcontext.apps.grs.framework.services.web.server.GenericServer;
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

		try {
			// initialize server.
			server = new GenericServer(args);

			// Add a new repository.
			DataManager.getInstance()
					.register(Game.class, GameRepository.class);

			// add a new Handler to the Rest Server.
			server.addRestContext("/rest/games", new GamesRestHandler(server));

			// and start server.
			server.start();

		} catch (IOException | InterruptedException | InstantiationException
				| IllegalAccessException e) {
			LOGGER.error("Unable to start the internal Rest HTTP Server component. Reason : " + e.getLocalizedMessage());
		}
		LOGGER.info("End of processing request in Server");

		// Exit from server.
		System.exit(0);
	}
}
