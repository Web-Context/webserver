/**
 * 
 */
package com.webcontext.test.framework.ut;

import org.apache.log4j.Logger;

import com.webcontext.framework.appserver.services.mongodb.MongoDBServer;
import com.webcontext.framework.appserver.services.web.server.GenericServer;
import com.webcontext.framework.appserver.utils.ArgsParser;

/**
 * This is basically a Test model of the WebServer. This class purpose is to
 * provide a test instance.
 * 
 * @author Frédéric Delorme<frederic.delorme@web-context.com>
 * 
 */
public class TestServer {

	private static Logger LOGGER = Logger.getLogger(TestServer.class);

	private GenericServer appServer = null;
	private MongoDBServer dbServer = null;

	/**
	 * Start the TestServer with <code>args</code>.
	 * 
	 * @param args
	 * @see ArgsParser.ArgType
	 */
	public void start(String[] args) {

		try {

			if (new ArgsParser(args).getBooleanArg(
					ArgsParser.ArgType.DATABASE_EMBEDDED.getKeyword(),
					ArgsParser.ArgType.DATABASE_EMBEDDED.getDefaultValue())) {
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

		} catch (Exception e) {
			LOGGER.error("Unable to start the internal TestServer component.",
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

	/**
	 * Stop both servers:
	 * <ul>
	 * <li>GenericServer</li>
	 * <li>MongoDBServer</li>
	 * </ul>
	 */
	public void stop() {
		appServer.stop();
		if (dbServer != null) {
			dbServer.stop();
		}
	}

	/*------------ Getters & Setters ----------------------------------------*/

	/**
	 * @return the appServer
	 */
	public GenericServer getAppServer() {
		return appServer;
	}

	/**
	 * @return the dbServer
	 */
	public MongoDBServer getDbServer() {
		return dbServer;
	}
}
