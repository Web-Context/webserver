/**
 * 
 */
package com.webcontext.apps.grs.application.models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.webcontext.framework.appserver.model.MDBEntity;

/**
 * @author frederic
 *
 */
public class Platform extends MDBEntity {

	private String code = "";
	private String name = "";
	private String manufacturer = "";
	private String developer = "";
	private String accesskey = "";
	private Date availabilityDate = new Date();
	private Map<String, String> pictures = new HashMap<>();

	public Platform() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param code
	 * @param name
	 * @param manufacturer
	 * @param developer
	 * @param accesskey
	 * @param availabilityDate
	 * @param pictures
	 */
	public Platform(String code, String name, String manufacturer,
			String developer, String accesskey, Date availabilityDate,
			Map<String, String> pictures) {
		super();
		this.code = code;
		this.name = name;
		this.manufacturer = manufacturer;
		this.developer = developer;
		this.accesskey = accesskey;
		this.availabilityDate = availabilityDate;
		this.pictures = pictures;
	}

	/**
	 * @return the manufacturer
	 */
	public String getManufacturer() {
		return manufacturer;
	}

	/**
	 * @param manufacturer
	 *            the manufacturer to set
	 */
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	/**
	 * @return the developer
	 */
	public String getDeveloper() {
		return developer;
	}

	/**
	 * @param developer
	 *            the developer to set
	 */
	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	/**
	 * @return the accesskey
	 */
	public String getAccesskey() {
		return accesskey;
	}

	/**
	 * @param accesskey
	 *            the accesskey to set
	 */
	public void setAccesskey(String accesskey) {
		this.accesskey = accesskey;
	}

	/**
	 * @return the availabilityDate
	 */
	public Date getAvailabilityDate() {
		return availabilityDate;
	}

	/**
	 * @param availabilityDate
	 *            the availabilityDate to set
	 */
	public void setAvailabilityDate(Date availabilityDate) {
		this.availabilityDate = availabilityDate;
	}

	/**
	 * @return the pictures
	 */
	public Map<String, String> getPictures() {
		return pictures;
	}

	/**
	 * @param pictures
	 *            the pictures to set
	 */
	public void setPictures(Map<String, String> pictures) {
		this.pictures = pictures;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
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
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Platform [code=").append(code).append(", name=")
				.append(name).append(", manufacturer=").append(manufacturer)
				.append(", developer=").append(developer)
				.append(", accesskey=").append(accesskey)
				.append(", availabilityDate=").append(availabilityDate)
				.append(", pictures=").append(pictures).append("]");
		return builder.toString();
	}

}
