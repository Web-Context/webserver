/**
 * 
 */
package com.webcontext.apps.grs.application.models;

import com.webcontext.framework.appserver.model.MDBEntity;

/**
 * @author frederic
 *
 */
public class Platform extends MDBEntity {

	private String code=null;
	private String name=null;
	private String description=null;

	public Platform() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param code
	 * @param name
	 * @param description
	 */
	public Platform(String code, String name, String description) {
		super();
		this.code = code;
		this.name = name;
		this.description = description;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Platform [code=" + code + ", name=" + name + ", description="
				+ description + "]";
	}

}
