/**
 * 
 */
package com.webcontext.apps.grs.framework.model;

/**
 * A default entity manager.
 * 
 * @author Frédéric Delorme<frederic.delorme@web-context.com>
 *
 */
public class MDBEntity {

	public String _id;

	public MDBEntity() {
		// TODO Auto-generated constructor stub
	}

	public MDBEntity(String _id) {
		super();
		this._id = _id;
	}

	/**
	 * @return the _id
	 */
	public String get_id() {
		return _id;
	}

	/**
	 * @param _id
	 *            the _id to set
	 */
	public void set_id(String _id) {
		this._id = _id;
	}

}
