package com.ge.monitoring.agent.restserver;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Response builder object to perform JSON serialization of object on Rest
 * answer.
 * 
 * @author Frédéric Delorme<frederic.delorme@serphydose.com>
 *
 */
public class RestResponse {

	/**
	 * Serialize response object to JSON object.
	 */
	protected Gson gson = new GsonBuilder().serializeNulls()
			.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
			.setPrettyPrinting().setVersion(1.0).create();

	private List<Object> data;

	/**
	 * Default constructor. Initialize the Data list.
	 */
	public RestResponse() {
		data = new ArrayList<Object>();
	}

	/**
	 * return all data in response object.
	 * 
	 * @return
	 */
	public List<Object> getData() {
		return data;
	}

	/**
	 * add a new Object to the data list.
	 * 
	 * @param data
	 */
	public void addData(Object data) {
		this.data.add(data);
	}

	/**
	 * Convert all objects in data list to one JSon Object to be send ton Http
	 * Response.
	 * 
	 * @return
	 */
	public String toJson() {
		return gson.toJson(data);
	}
}
