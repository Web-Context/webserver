/**
 * 
 */
package com.webcontext.framework.appserver.model;

import java.util.Map;

/**
 * A default entity manager.
 * 
 * @author Frédéric Delorme<frederic.delorme@web-context.com>
 *
 */
public class MDBEntity {

	public Map<String,String> _id;

	public MDBEntity() {
		// TODO Auto-generated constructor stub
	}

	public MDBEntity(Map<String,String> _id) {
		super();
		this._id = _id;
	}

	/**
	 * @return the _id
	 */
	public Map<String,String> get_id() {
		return _id;
	}

	/**
	 * @param _id
	 *            the _id to set
	 */
	public void set_id(Map<String,String> _id) {
		this._id = _id;
	}

}
