/**
 * 
 */
package com.webcontext.apps.grs.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import com.webcontext.apps.grs.framework.repository.MongoDbRepository;
import com.webcontext.apps.grs.models.Game;

/**
 * @author 212391884
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
		game = gson.fromJson(JSON.serialize(item), Game.class);
		return game;
	}

	@Override
	public BasicDBObject serialize(Game item) {
		BasicDBObject object = new BasicDBObject();

		object = (BasicDBObject) JSON.parse(gson.toJson(item).toString());
		return object;
	}

}
