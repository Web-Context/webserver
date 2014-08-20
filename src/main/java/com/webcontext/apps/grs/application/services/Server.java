/**
 * 
 */
package com.webcontext.apps.grs.application.services;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.webcontext.apps.grs.application.models.Game;
import com.webcontext.apps.grs.application.repositories.GameRepository;
import com.webcontext.apps.grs.application.rest.GamesRestHandler;
import com.webcontext.framework.appserver.services.mongodb.MongoDBServer;
import com.webcontext.framework.appserver.services.persistence.DataManager;
import com.webcontext.framework.appserver.services.web.server.GenericServer;
import com.webcontext.framework.appserver.utils.ArgumentParser;

/**
 * This is the start class for our server.
 * 
 * @author Frédéric Delorme<frederic.delorme@web-context.com>
 * 
 */
public class Server {

	private static Logger LOGGER = Logger.getLogger(Server.class);

	private static GenericServer appServer = null;
	private static MongoDBServer dbServer = null;

	public static void main(String[] args) {

		try {

			if (new ArgumentParser(args).getBooleanArg(
					ArgumentParser.ServerArguments.DATABASE_EMBEDDED
							.getKeyword(),
					ArgumentParser.ServerArguments.DATABASE_EMBEDDED
							.getDefaultValue())) {
				/**
				 * Initialize and start the MongoDBserver.
				 */
				dbServer = new MongoDBServer(args);
				dbServer.start();
				dbServer.waitUntilStarted();
			}

			// initialize server.
			appServer = new GenericServer(args);

			// Add a new repository.
			DataManager.getInstance()
					.register(Game.class, GameRepository.class);

			// add a new Handler to the Rest Server.
			appServer.addRestContext("/rest/games", new GamesRestHandler(
					appServer));

			// and start server.
			appServer.start();

		} catch (IOException e) {
			LOGGER.error(
					"Unable to start the internal Rest HTTP Server component.",
					e);
		} catch (InstantiationException e) {
			LOGGER.error(
					"Unable to start the internal Rest HTTP Server component.",
					e);
		} catch (IllegalAccessException e) {
			LOGGER.error(
					"Unable to start the internal Rest HTTP Server component.",
					e);
		} catch (InterruptedException e) {
			LOGGER.error(
					"Unable to start the internal Rest HTTP Server component.",
					e);
		} finally {
			if (appServer != null) {
				appServer.stop();
			}
			if (dbServer != null) {
				dbServer.stop();
			}
		}
		LOGGER.info("End of processing request in Server");

		// Exit from server.
		System.exit(0);
	}
}
