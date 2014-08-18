/**
 * 
 */
package com.webcontext.apps.grs.framework.services.mongodb;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.webcontext.apps.grs.framework.utils.ArgumentParser;

import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ArtifactStoreBuilder;
import de.flapdoodle.embed.mongo.config.DownloadConfigBuilder;
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
 * Instantiate a Stand-Alone MongoDB Server to serve the application in
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
	 * Build and run MongoDB server on the IP port <code>mongoServerPort</code>.
	 * 
	 * @param mongoServerPort
	 *            the IP port where to run the MongoDBService.
	 */
	private MongoDBServer(int mongoServerPort) {
		LOGGER.info(String.format("Start MongoDB internal server on port %d",
				mongoServerPort));
		try {

			/**
			 * Set a path for data storage.
			 */
			IDirectory artifactStorePath = new FixedPath(
					System.getProperty("user.home") + File.separator
							+ ".embeddedMongodb");
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
			IMongodConfig mongodConfig = new MongodConfigBuilder()
					.version(Version.Main.PRODUCTION)
					.net(new Net(mongoServerPort, Network.localhostIsIPv6()))
					.cmdOptions(
							new MongoCmdOptionsBuilder().useSmallFiles(true)
							// .verbose(true)
									.build()).build();
			/**
			 * Configure and install the MongoDB server.
			 */

			MongodStarter starter = MongodStarter.getInstance(runtimeConfig);
			mongodExecutable = starter.prepare(mongodConfig);
			LOGGER.info(String.format("MongoDB configuraiton:",
					mongodConfig.toString()));

		} catch (UnknownHostException e) {
			LOGGER.error(
					String.format("Unable to retrieve Localhost IP address."),
					e);
		} catch (IOException e) {
			LOGGER.error(String
					.format("Error during Mongo Server instance execution."), e);
		}
	}

	public MongoDBServer(String[] args) {
		this(new ArgumentParser(args).getIntArg("dbport", 27017));

	}

	/**
	 * Retrieve the singleton instance
	 * 
	 * @return
	 */
	public static MongoDBServer getInstance() {
		if (instance == null) {
			instance = new MongoDBServer(27017);
		}
		return instance;
	}

	/**
	 * Retrieve the singleton instance
	 * 
	 * @return
	 */
	public static MongoDBServer getInstance(int mongoServerPort) {
		if (instance == null) {
			instance = new MongoDBServer(mongoServerPort);
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
		 * Start the MongoDB Server.
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

	/**
	 * Start the MongoDBServer in a stand alone mode on port 27017.
	 * 
	 * @param args
	 *            list of java arguments .
	 */
	public static void main(String[] args) {
		// extract specific arg from java list args.
		ArgumentParser ap = new ArgumentParser(args);
		MongoDBServer mgserver = new MongoDBServer(ap.getIntArg("port", 27017));
		try {
			if (mgserver != null && mgserver.mongod == null) {
				mgserver.start();
				Thread.sleep(10 * 1000);
				mgserver.stop();
			}
		} catch (IOException | InterruptedException e) {
			LOGGER.error("unalbe to start server", e);
		}
	}

}
