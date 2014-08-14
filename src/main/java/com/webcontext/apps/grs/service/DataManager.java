/**
 * 
 */
package com.webcontext.apps.grs.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.webcontext.apps.grs.framework.model.MDBEntity;
import com.webcontext.apps.grs.framework.repository.IMongoDbRepository;
import com.webcontext.apps.grs.framework.repository.MongoDBConnection;
import com.webcontext.apps.grs.framework.repository.exception.NullMongoDBConnection;

/**
 * Data manager is a MongoDB Repository Service, providing easy access to data.
 * 
 * @author Frédéric Delorme<frederic.delorme@web-context.com>
 *
 */
public class DataManager {

	private static final Logger LOGGER = Logger.getLogger(DataManager.class);

	/**
	 * internal instance.
	 */
	private static DataManager _instance;

	/**
	 * Map of MongoDB repositories.
	 */
	@SuppressWarnings("rawtypes")
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
		IMongoDbRepository<?> repo = repository.newInstance();
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
	@SuppressWarnings("unchecked")
	public List<MDBEntity> find(Class<? extends MDBEntity> entity,
			String filter, int offset, int pageSize) {

		IMongoDbRepository<MDBEntity> repository = repositories.get(entity);

		List<MDBEntity> list = null;
		try {
			list = repository.find(filter, offset, pageSize);
		} catch (NullMongoDBConnection e) {
			LOGGER.error(
					"Unalble to retrieve data for " + entity.getCanonicalName(),
					e);
		}
		return list;
	}

	/**
	 * Retrieve an entity on the specific collection (already declared in the
	 * repository implementation)
	 * 
	 * @param entity
	 *            the entity to retrieve
	 * @param id
	 *            the unique identifier for the searched entity.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MDBEntity> findById(Class<? extends MDBEntity> entity, String id) {
		IMongoDbRepository<MDBEntity> repository = repositories.get(entity);

		List<MDBEntity> entities = null;
		try {
			entities = repository.find(String.format("{ \"id\":\"%s\"}", id));
		} catch (NullMongoDBConnection e) {
			LOGGER.error(
					"Unalble to retrieve data for " + entity.getCanonicalName(),
					e);
		}
		return entities;

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
		return DataManager.getInstance().find(entity, filter, 0, 0);

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
			String filter, int offset, int pageSize) {
		return DataManager.getInstance().find(entity, filter, offset, pageSize);

	}

	/**
	 * retrieve the number of entries on the corresponding collection.
	 * 
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public long count(Class<? extends MDBEntity> entity) {
		IMongoDbRepository<MDBEntity> repository = repositories.get(entity);
		long count = -1;
		try {
			count = repository.count();
		} catch (NullMongoDBConnection e) {
			LOGGER.error(
					"Unalble to retrieve data for " + entity.getCanonicalName(),
					e);
		}
		return count;
	}

	/**
	 * static helper to retrieve number of entities in a collection.
	 * 
	 * @param entity
	 * @return
	 */
	public static long countEntities(Class<? extends MDBEntity> entity) {
		return DataManager.getInstance().count(entity);
	}

	/**
	 * Save an entity to Collection.
	 * 
	 * @param entity
	 *            to be saved.
	 */
	@SuppressWarnings("unchecked")
	public void save(MDBEntity entity) {
		IMongoDbRepository<MDBEntity> repository = repositories.get(entity
				.getClass());
		try {
			repository.save(entity);
		} catch (NullMongoDBConnection e) {
			LOGGER.error("Unalble to retrieve data for "
					+ entity.getClass().getCanonicalName(), e);
		}
	}

}
