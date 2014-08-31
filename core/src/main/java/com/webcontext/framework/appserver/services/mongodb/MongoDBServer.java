/**
 * 
 */
package com.webcontext.framework.appserver.services.mongodb;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.webcontext.framework.appserver.utils.ArgsParser;

import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ArtifactStoreBuilder;
import de.flapdoodle.embed.mongo.config.DownloadConfigBuilder;
import de.flapdoodle.embed.mongo.config.IMongoCmdOptions;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongoCmdOptionsBuilder;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.RuntimeConfigBuilder;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.extract.ITempNaming;
import de.flapdoodle.embed.process.extract.UUIDTempNaming;
import de.flapdoodle.embed.process.io.directories.FixedPath;
import de.flapdoodle.embed.process.io.directories.IDirectory;
import de.flapdoodle.embed.process.runtime.Network;

/**
 * 
 * Instantiate a Stand-Alone MongoDB TestServer to serve the application in
 * dev-mode.
 * 
 * @author Frédéric Delorme<frederic.delorme@web-context.com>
 * 
 */
public class MongoDBServer {

	private static final Logger LOGGER = Logger.getLogger(MongoDBServer.class);

	private static MongoDBServer instance = null;

	private MongodExecutable mongodExecutable = null;

	private MongodProcess mongod = null;

	/**
	 * Internal constructor.
	 */
	private MongoDBServer() {
		super();
	}

	/**
	 * Internal constructor.
	 * 
	 * @param args
	 */
	private MongoDBServer(String[] args) {
		ArgsParser ap = new ArgsParser(args);
		int port = ap.getIntArg(ArgsParser.ArgType.DATABASE_PORT.getKeyword(),
				ArgsParser.ArgType.DATABASE_PORT.getDefaultValue());
		initialize(port, false, null);
	}

	/**
	 * Build and run MongoDB server on the IP port <code>mongoServerPort</code>.
	 * 
	 * @param mongoServerPort
	 *            the IP port where to run the MongoDBService.
	 */
	private void initialize(int mongoServerPort, boolean verbose,
			String dataPath) {
		LOGGER.info(String.format("Start MongoDB internal server on port %d",
				mongoServerPort));
		try {

			/**
			 * Set a path for data storage.
			 */
			IDirectory artifactStorePath = new FixedPath((dataPath != null
					&& !dataPath.equals("") ? dataPath
					: System.getProperty("user.home") + File.separator
							+ ".embeddedMongodb"));

			ITempNaming executableNaming = new UUIDTempNaming();
			Command command = Command.MongoD;
			IRuntimeConfig runtimeConfig = new RuntimeConfigBuilder()
					.defaults(command)
					.artifactStore(
							new ArtifactStoreBuilder()
									.defaults(command)
									.download(
											new DownloadConfigBuilder()
													.defaultsForCommand(command)
													.artifactStorePath(
															artifactStorePath))
									.executableNaming(executableNaming))
					.build();

			/**
			 * Retrieve the last stable Production version on Internet (if
			 * accessible).
			 */
			IMongoCmdOptions mongoCmdOptions = new MongoCmdOptionsBuilder()
			// Use small storage space
					.useSmallFiles(true)
					// set verbose mode at execution (debug purpose).
					.verbose(verbose)
					// Build command Options
					.build();

			IMongodConfig mongodConfig = new MongodConfigBuilder()
					.version(Version.Main.PRODUCTION)
					.net(new Net(mongoServerPort, Network.localhostIsIPv6()))
					.cmdOptions(mongoCmdOptions).build();
			/**
			 * Configure and install the MongoDB server.
			 */

			MongodStarter starter = MongodStarter.getInstance(runtimeConfig);
			mongodExecutable = starter.prepare(mongodConfig);
			LOGGER.info(String.format("MongoDB configuration:",
					mongodConfig.toString()));

		} catch (UnknownHostException e) {
			LOGGER.error(
					String.format("Unable to retrieve Localhost IP address."),
					e);
		} catch (IOException e) {
			LOGGER.error(
					String.format("Error during Mongo TestServer instance execution."),
					e);
		}
	}

	/**
	 * Retrieve the singleton instance. If not already exists, set default
	 * execution IP port for the server to 21017, deactivate the verbose mode
	 * and set data path to a default path (see
	 * MongoDBServer(int,boolean,String) for details.
	 * 
	 * @see MongoDBServer#MongoDBServer(int, boolean, String)
	 * 
	 * @return return an instance of the MongoDBServer.
	 */
	public static MongoDBServer getInstance() {
		return getInstance(27017, false, null);
	}

	/**
	 * Retrieve the singleton instance. If not already exists, set default
	 * execution IP port for the server to <code>mongoServerPort</code>,
	 * deactivate the verbose mode and set data path to a default path (see
	 * MongoDBServer(int,boolean,String) for details.
	 * 
	 * @param mongoServerPort
	 *            the default execution port for the MongoDBServer instance to
	 *            create.
	 * @return a new or already existing instance.
	 */
	public static MongoDBServer getInstance(int mongoServerPort) {
		return getInstance(mongoServerPort, false, null);
	}

	/**
	 * This is the main Instance builder for MongoDBServer. Retrieve the or
	 * create a singleton instance. If not already exists, set default execution
	 * IP port for the server to <code>mongoServerPort</code>, set
	 * <code>verbose</code> mode and set <code>dataPath</code> (see
	 * MongoDBServer(int,boolean,String) for details.
	 * 
	 * @param port
	 *            IP port for the server.
	 * @param verboseMode
	 *            activate the verbose execution mode of MongoDB server (debug
	 *            purpose.
	 * @param dataPath
	 *            String as path to a folder for data storage.
	 * @return a brand new instance or an already existing MongoDBServer.
	 */
	public static MongoDBServer getInstance(int port, boolean verboseMode,
			String dataPath) {
		if (instance == null) {
			instance = new MongoDBServer();
			instance.initialize(port, verboseMode, dataPath);
		}
		return instance;
	}

	/**
	 * Initialize a new MongoBDServer based on standard java command line
	 * arguments.
	 * 
	 * @param args
	 *            Arguments list build with <code>ArgsParser.ArgType</code>
	 *            arguments.
	 * @see ArgsParser.ArgType
	 * @return
	 */
	public static MongoDBServer getInstance(String[] args) {
		if (instance == null) {
			instance = new MongoDBServer(args);
		}
		return instance;
	}

	/**
	 * Ask MongoDb server instance to stop.
	 */
	public void stop() {
		if (this.mongodExecutable != null) {
			this.mongodExecutable.stop();
		}
	}

	/**
	 * Start MongoDBServer.
	 * 
	 * @throws IOException
	 */
	public void start() throws IOException {
		/**
		 * Start the MongoDB TestServer.
		 */
		this.mongod = this.mongodExecutable.start();
		LOGGER.info(String.format("MongoDB deamon :%s", (this.mongod != null
				&& this.mongod.isProcessRunning() ? "running" : "not started")));
	}

	public void waitUntilStarted() {
		try {
			while (!mongod.isProcessRunning()) {
				Thread.sleep(200);

			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
