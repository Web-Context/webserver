/**
 * 
 */
package com.webcontext.apps.grs.application.services;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.webcontext.framework.appserver.services.mongodb.MongoDBServer;
import com.webcontext.framework.appserver.services.web.server.GenericServer;
import com.webcontext.framework.appserver.utils.ArgsParser;

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

			if (new ArgsParser(args).getBooleanArg(
					ArgsParser.ArgType.DATABASE_EMBEDDED
							.getKeyword(),
					ArgsParser.ArgType.DATABASE_EMBEDDED
							.getDefaultValue())) {
				/**
				 * Initialize and start the MongoDBserver.
				 */
				dbServer = MongoDBServer.getInstance(args);
				dbServer.start();
				dbServer.waitUntilStarted();
			}

			// initialize server.
			appServer = new GenericServer(args);

			// and start server.
			appServer.start();

		} catch (IOException e) {
			LOGGER.error(
					"Unable to start the internal Rest HTTP TestServer component.",
					e);
		} catch (InstantiationException e) {
			LOGGER.error(
					"Unable to start the internal Rest HTTP TestServer component.",
					e);
		} catch (IllegalAccessException e) {
			LOGGER.error(
					"Unable to start the internal Rest HTTP TestServer component.",
					e);
		} catch (InterruptedException e) {
			LOGGER.error(
					"Unable to start the internal Rest HTTP TestServer component.",
					e);
		} catch (Exception e) {
			LOGGER.error(
					"Unable to start the internal Rest HTTP TestServer component.",
					e);
		} finally {
			if (appServer != null) {
				appServer.stop();
			}
			if (dbServer != null) {
				dbServer.stop();
			}
		}
		LOGGER.info("End of processing request in TestServer");

		// Exit from server.
		System.exit(0);
	}
}
