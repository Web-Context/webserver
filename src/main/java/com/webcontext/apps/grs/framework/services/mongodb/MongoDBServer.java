/**
 * 
 */
package com.webcontext.apps.grs.framework.services.mongodb;

import java.io.IOException;
import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

/**
 * 
 * Instantiate a standalone MongoDB Server to serve the application in devmode.
 * 
 * @author frederic
 * 
 */
public class MongoDBServer {

	public MongoDBServer() {
		MongodStarter starter = MongodStarter.getDefaultInstance();

		int port = 27017;
		MongodExecutable mongodExecutable = null;
		try {
			IMongodConfig mongodConfig = new MongodConfigBuilder()
					.version(Version.Main.PRODUCTION)
					.net(new Net(port, Network.localhostIsIPv6())).build();

			mongodExecutable = starter.prepare(mongodConfig);
			MongodProcess mongod = mongodExecutable.start();

			if (mongod.isProcessRunning()) {
				MongoClient mongo = new MongoClient("localhost", port);
				DB db = mongo.getDB("restgames");
				// create collection
				if (!db.collectionExists("games")) {
					// create collection.
					DBCollection col = db.createCollection("games",
							new BasicDBObject());
					// create index
					String index= "{\"title\":1}";
					BasicDBObject gamesIndex = (BasicDBObject) JSON.parse(index);
					db.getCollection("games").createIndex(gamesIndex);
				}
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (mongodExecutable != null)
				mongodExecutable.stop();
		}
	}

	public static void main(String[] args) {
		MongoDBServer mgserver = new MongoDBServer();
	}
	
}
