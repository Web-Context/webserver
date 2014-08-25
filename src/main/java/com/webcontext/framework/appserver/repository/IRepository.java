/**
 * 
 */
package com.webcontext.framework.appserver.repository;

/**
 * @author 212391884
 *
 */
public interface IRepository<T> {

	/**
	 * Set the database connection for this repository.
	 * 
	 * @param conn
	 */
	public void setConnection(MongoDBConnection conn);	
}
