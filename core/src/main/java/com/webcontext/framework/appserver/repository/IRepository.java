/**
 * 
 */
package com.webcontext.framework.appserver.repository;

import java.util.List;

import com.mongodb.WriteResult;
import com.webcontext.framework.appserver.repository.exception.NullMongoDBConnection;
import com.webcontext.framework.appserver.repository.exception.RepositoryException;

/**
 * This repository interface declare all basic specific method to access some T
 * entity.
 * 
 * @author Frédéric Delorme<frederic.delorme@webcontext.com>
 * 
 */
public interface IRepository<T> {

	/**
	 * Set the database connection for this repository.
	 * 
	 * @param conn
	 */
	public void setConnection(MongoDBConnection conn)
			throws RepositoryException;

	/**
	 * Retrieve data from the defined collection.
	 * 
	 * @return
	 * @throws NullMongoDBConnection
	 */
	public abstract List<T> find() throws RepositoryException;

	/**
	 * Retrieve data from a collection with particular criteria.
	 * 
	 * @param filter
	 * @return
	 * @throws NullMongoDBConnection
	 */
	public abstract List<T> find(String filter) throws RepositoryException;

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
			throws RepositoryException;

	/**
	 * return the number of record in a collection.
	 * 
	 * @return
	 * @throws NullMongoDBConnection
	 */
	public long count() throws RepositoryException;

	/**
	 * Save the <code>item</code> T and add to the <code>collection</code>
	 * 
	 * @param item
	 * @return _id for the create object into the collection.
	 * @throws NullMongoDBConnection
	 */
	public abstract WriteResult save(T item) throws RepositoryException;

	/**
	 * 
	 * @param item
	 * @throws NullMongoDBConnection
	 */
	public abstract void remove(T item) throws RepositoryException;

}
