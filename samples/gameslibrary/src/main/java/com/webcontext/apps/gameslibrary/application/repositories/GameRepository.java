/**
 * 
 */
package com.webcontext.apps.gameslibrary.application.repositories;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import com.webcontext.apps.gameslibrary.application.models.Game;
import com.webcontext.framework.appserver.repository.IRepository;
import com.webcontext.framework.appserver.repository.MongoDbRepository;
import com.webcontext.framework.appserver.services.persistence.Repository;
import com.webcontext.framework.appserver.utils.FileIO;

/**
 * A simple implementation of a Game repository using a MongoDB games collection
 * 
 * @author Frédéric Delorme<frederic.delorme@web-context.com>
 * 
 */
@Repository(entity = Game.class)
public class GameRepository extends MongoDbRepository<Game> implements
		IRepository<Game> {

	/**
	 * Default constructor for default connection.
	 */
	public GameRepository() {
		super("games");
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

	/**
	 * Read the T object list from a JSON file.
	 * 
	 * @param filePath
	 *            the JSON file to be read and parsed to produce a
	 *            <code>List<T></code> objects.
	 * @return return a list of T object as a <code>list<T></code>.
	 * @throws IOException
	 */
	public List<Game> loadObjectFromJSONFile(String filePath)
			throws IOException {
		filePath = this.getClass().getResource("/").getPath().toString()
				+ File.separator + filePath;

		String json = FileIO.fastRead(filePath);
		TypeToken<List<Game>> token = new TypeToken<List<Game>>() {
		};
		List<Game> list = gson.fromJson(json, token.getType());

		return list;
	}

}
