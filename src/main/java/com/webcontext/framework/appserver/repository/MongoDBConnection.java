/**
 * 
 */
package com.webcontext.framework.appserver.repository;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import com.mongodb.DB;
import com.mongodb.MongoClient;

/**
 * MongoDB connection. Manage a specific connection to one or more MongoDB
 * database.
 * 
 * @author Frédéric Delorme<frederic.delorme@web-context.com>
 * 
 */
public class MongoDBConnection {

	/**
	 * Authentication flag. True if authenticated is activated and
	 * <code>username/password</code> are authenticated.
	 */
	private static boolean authenticated = true;

	/**
	 * MongoDB client.
	 */
	private static MongoClient mongo;
	/**
	 * MongoDB Database connection.
	 */
	private static DB db;
	/**
	 * Properties file setting database access configuration.
	 */
	private static Properties properties;

	/**
	 * Default constructor. Create a default connection.
	 */
	public MongoDBConnection() {

		this("/config/data.xml");

	}

	/**
	 * initialize MongoClient with the <code>dataConfiguration</code> XML file.
	 */
	public MongoDBConnection(String dataConfiguration) {
		if (properties == null) {
			properties = new Properties();
		}
		if (mongo == null) {
			try {
				@SuppressWarnings("unused")
				String path = this.getClass().getResource("/").toString();
				properties.loadFromXML(this.getClass().getResourceAsStream(
						dataConfiguration));
				mongo = new MongoClient();
				db = mongo.getDB(properties.get("database").toString());
				// if authentication is required,
				if (properties.containsKey("username")
						&& properties.containsKey("password")) {
					authenticated = db.authenticate(
							properties.getProperty("username").toString(),
							properties.getProperty("password").toCharArray());
				}
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidPropertiesFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	/**
	 * @return the authenticated
	 */
	public static boolean isAuthenticated() {
		return authenticated;
	}

	/**
	 * @param authenticated
	 *            the authenticated to set
	 */
	public static void setAuthenticated(boolean authenticated) {
		MongoDBConnection.authenticated = authenticated;
	}

	/**
	 * @return the mongo
	 */
	public static MongoClient getMongo() {
		return mongo;
	}

	/**
	 * @param mongo
	 *            the mongo to set
	 */
	public static void setMongo(MongoClient mongo) {
		MongoDBConnection.mongo = mongo;
	}

	/**
	 * @return the db
	 */
	public DB getDb() {
		return db;
	}

	/**
	 * @param db
	 *            the db to set
	 */
	public void setDb(DB pdb) {
		db = pdb;
	}

	/**
	 * @return the properties
	 */
	public static Properties getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public static void setProperties(Properties properties) {
		MongoDBConnection.properties = properties;
	}

}
