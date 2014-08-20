/**
 * 
 */
package com.webcontext.apps.grs.application.repositories;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import com.webcontext.apps.grs.application.models.Platform;
import com.webcontext.framework.appserver.repository.MongoDbRepository;
import com.webcontext.framework.appserver.utils.FileIO;

/**
 * @author frederic
 * 
 */
public class PlatformRepository extends MongoDbRepository<Platform> {

	public PlatformRepository() {
		super("platforms");
	}

	@Override
	public Platform deserialize(BasicDBObject item) {
		Platform platform = new Platform();
		platform = gson.fromJson(item.toString(), Platform.class);
		return platform;
	}

	@Override
	public BasicDBObject serialize(Platform item) {
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
	public List<Platform> loadObjectFromJSONFile(String filePath)
			throws IOException {
		filePath = this.getClass().getResource("/").getPath().toString()
				+ File.separator + filePath;

		String json = FileIO.loadAsString(filePath);
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
				.create();

		TypeToken<List<Platform>> token = new TypeToken<List<Platform>>() {
		};
		List<Platform> list = gson.fromJson(json, token.getType());

		return list;
	}

}
