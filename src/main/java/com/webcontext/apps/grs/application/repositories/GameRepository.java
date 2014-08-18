/**
 * 
 */
package com.webcontext.apps.grs.application.repositories;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import com.webcontext.apps.grs.application.models.Game;
import com.webcontext.apps.grs.framework.repository.MongoDbRepository;

/**
 * A simple implementation of a Game repository using a MongoDB games collection
 * 
 * @author Frédéric Delorme<frederic.delorme@web-context.com>
 * 
 */
public class GameRepository extends MongoDbRepository<Game> {

	/**
	 * Default constructor for default connection.
	 */
	public GameRepository() {
		super();
		collectionName = "games";
	}

	@Override
	public Game deserialize(BasicDBObject item) {
		Game game = new Game();
		game = gson.fromJson(item.toString(), Game.class);
		return game;
	}

	@Override
	public BasicDBObject serialize(Game item) {
		BasicDBObject object = new BasicDBObject();

		object = (BasicDBObject) JSON.parse(gson.toJson(item).toString());
		return object;
	}

}
