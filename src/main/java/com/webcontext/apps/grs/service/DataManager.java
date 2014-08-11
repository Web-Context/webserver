/**
 * 
 */
package com.webcontext.apps.grs.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.webcontext.apps.grs.framework.model.MDBEntity;
import com.webcontext.apps.grs.framework.repository.IMongoDbRepository;
import com.webcontext.apps.grs.framework.repository.MongoDBConnection;

/**
 * Data manager is a MongoDB Repository Service, providing easy access to data.
 * 
 * @author Frédéric Delorme<frederic.delorme@web-context.com>
 *
 */
public class DataManager {

	/**
	 * internal instance.
	 */
	private static DataManager _instance;

	/**
	 * Map of MongoDB repositories.
	 */
	private Map<Class<? extends MDBEntity>, IMongoDbRepository> repositories = new HashMap<Class<? extends MDBEntity>, IMongoDbRepository>();

	/**
	 * Database Connection.
	 */
	private MongoDBConnection connection;

	/**
	 * Default constructor.
	 */
	private DataManager() {
		connection = new MongoDBConnection();
	}

	/**
	 * return an instance of the DataManager
	 * 
	 * @return
	 */
	public static DataManager getInstance() {
		if (_instance == null) {
			_instance = new DataManager();
		}
		return _instance;

	}

	/**
	 * Register a new MongoDB <code>repository</code> for <code>entity</code>
	 * into the DataManager.
	 * 
	 * @param entity
	 * @param repository
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("rawtypes")
	public void register(Class<? extends MDBEntity> entity,
			Class<? extends IMongoDbRepository> repository)
			throws InstantiationException, IllegalAccessException {
		IMongoDbRepository repo = repository.newInstance();
		repo.setConnection(connection);
		repositories.put(entity, repo);
	}

	/**
	 * Retrieve all data from manager with <code>title</code>,
	 * <code>platform</code> and limit number of return items to
	 * <code>pageSize</code>.
	 * 
	 * @param platform
	 *            API version to request
	 * @param title
	 *            title to search for
	 * @param offset
	 *            data page offset
	 * @param pageSize
	 *            data page size
	 * @return
	 */
	public List<MDBEntity> find(Class<? extends MDBEntity> entity, String filter) {

		IMongoDbRepository repository = repositories.get(entity);

		List<MDBEntity> measures = repository.find(filter);
		return measures;
	}

	/**
	 * static helper to access easily the find method.
	 * 
	 * @param version
	 * @param title
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	public static List<MDBEntity> findAll(Class<? extends MDBEntity> entity,
			String filter) {
		return DataManager.getInstance().find(entity, filter);

	}

}
