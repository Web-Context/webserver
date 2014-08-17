/**
 * 
 */
package com.webcontext.apps.grs.rest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.webcontext.apps.grs.framework.repository.MongoDBConnection;
import com.webcontext.apps.grs.framework.repository.exception.NullMongoDBConnection;
import com.webcontext.apps.grs.framework.repository.exception.RepositoryDoesNotExistsException;
import com.webcontext.apps.grs.framework.server.web.server.bootstrap.Bootstrap;
import com.webcontext.apps.grs.framework.server.web.server.bootstrap.IBootstrap;
import com.webcontext.apps.grs.models.Game;
import com.webcontext.apps.grs.repository.GameRepository;
import com.webcontext.apps.grs.service.DataManager;

/**
 * Boostraping the server by adding some basic data to MongoDB.
 * 
 * @author Frédéric Delorme<frederic.delorme@web-context.com>
 * 
 */
@Bootstrap
public class ServerBootstrap implements IBootstrap {

	private final static Logger LOGGER = Logger
			.getLogger(ServerBootstrap.class);

	public void initialized() {
		MongoDBConnection connection = new MongoDBConnection();
		GameRepository gr = new GameRepository();
		gr.setConnection(connection);

		try {
			if (DataManager.countEntities(Game.class) == 0) {
				try {
					List<Game> list = gr.loadObjectFromJSONFile("games.json");
					for (Game game : list) {
						gr.save(game);
					}
				} catch (FileNotFoundException e) {
					LOGGER.error("unable to read file", e);
				} catch (NullMongoDBConnection e) {
					LOGGER.error("unable to integrate MongoDB file.", e);
				} catch (IOException e) {
					LOGGER.error("Error during file access", e);
				}
			}
		} catch (RepositoryDoesNotExistsException e) {
			LOGGER.error("Unable to generate data", e);
		}
	}

}
