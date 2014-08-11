package com.webcontext.apps.grs.framework.repository;

import java.util.List;

import com.mongodb.WriteResult;

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
	 */
	public abstract List<T> find();

	/**
	 * Retrieve data from a collection with particular criteria.
	 * 
	 * @param filter
	 * @return
	 */
	public abstract List<T> find(String filter);

	/**
	 * Save the <code>item</code> T and add to the <code>collection</code>
	 * 
	 * @param item
	 * @return _id for the create object into the collection.
	 */
	public abstract WriteResult save(T item);

	/**
	 * 
	 * @param item
	 */
	public abstract void remove(T item);

}