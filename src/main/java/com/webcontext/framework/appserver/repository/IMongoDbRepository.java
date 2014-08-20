package com.webcontext.framework.appserver.repository;

import java.util.List;

import com.mongodb.WriteResult;
import com.webcontext.framework.appserver.repository.exception.NullMongoDBConnection;

/**
 * 
 * 
 * @author Frédéric Delorme<frederic.delorme@web-context.com>
 * @param <T>
 */
public interface IMongoDbRepository<T> {

	/**
	 * Set the database connection for this repository.
	 * 
	 * @param conn
	 */
	public void setConnection(MongoDBConnection conn);

	/**
	 * Retrieve data from the defined collection.
	 * 
	 * @return
	 * @throws NullMongoDBConnection
	 */
	public abstract List<T> find() throws NullMongoDBConnection;

	/**
	 * Retrieve data from a collection with particular criteria.
	 * 
	 * @param filter
	 * @return
	 * @throws NullMongoDBConnection
	 */
	public abstract List<T> find(String filter) throws NullMongoDBConnection;

	/**
	 * Retrieve data from a collection with particular filter and manage
	 * pagination.
	 * 
	 * @param filter
	 * @param offset
	 * @param pageSize
	 * @return
	 * @throws NullMongoDBConnection
	 */
	public abstract List<T> find(String filter, int offset, int pageSize)
			throws NullMongoDBConnection;

	/**
	 * return the number of record in a collection.
	 * 
	 * @return
	 * @throws NullMongoDBConnection
	 */
	public long count() throws NullMongoDBConnection;

	/**
	 * Save the <code>item</code> T and add to the <code>collection</code>
	 * 
	 * @param item
	 * @return _id for the create object into the collection.
	 * @throws NullMongoDBConnection
	 */
	public abstract WriteResult save(T item) throws NullMongoDBConnection;

	/**
	 * 
	 * @param item
	 * @throws NullMongoDBConnection
	 */
	public abstract void remove(T item) throws NullMongoDBConnection;

}