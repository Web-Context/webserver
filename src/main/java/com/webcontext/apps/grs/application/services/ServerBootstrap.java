/**
 * 
 */
package com.webcontext.apps.grs.application.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.webcontext.apps.grs.application.models.Game;
import com.webcontext.apps.grs.application.models.Platform;
import com.webcontext.apps.grs.application.repositories.GameRepository;
import com.webcontext.apps.grs.application.repositories.PlatformRepository;
import com.webcontext.framework.appserver.repository.MongoDBConnection;
import com.webcontext.framework.appserver.repository.exception.NullMongoDBConnection;
import com.webcontext.framework.appserver.repository.exception.RepositoryDoesNotExistsException;
import com.webcontext.framework.appserver.services.persistence.DataManager;
import com.webcontext.framework.appserver.services.web.server.bootstrap.Bootstrap;
import com.webcontext.framework.appserver.services.web.server.bootstrap.IBootstrap;

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
		PlatformRepository pr = new PlatformRepository();
		pr.setConnection(connection);

		try {
			if (DataManager.countEntities(Game.class) == 0) {
				List<Game> games = gr.loadObjectFromJSONFile("games.json");
				for (Game game : games) {
					gr.save((Game) game);
				}
			}
			if (DataManager.countEntities(Platform.class) == 0) {
				List<Platform> platforms = pr
						.loadObjectFromJSONFile("platforms.json");
				for (Platform platform : platforms) {
					pr.save((Platform) platform);
				}
			}
		} catch (FileNotFoundException e) {
			LOGGER.error("unable to read file", e);
		} catch (NullMongoDBConnection e) {
			LOGGER.error("unable to integrate MongoDB file.", e);
		} catch (IOException e) {
			LOGGER.error("Error during file access", e);
		} catch (RepositoryDoesNotExistsException e) {
			LOGGER.error("Unable to generate data", e);
		}
	}

}
