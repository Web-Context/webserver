package com.ge.monitoring.agent.restserver.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private Map<String, Object> data;

	/**
	 * Default constructor. Initialize the Data list.
	 */
	public RestResponse() {
		data = new HashMap<String, Object>();
	}

	/**
	 * return all data in response object.
	 * 
	 * @return
	 */
	public Map<String,Object> getData() {
		return data;
	}

	/**
	 * add a new Object to the data list.
	 * 
	 * @param data
	 */
	public void add(String key,Object value) {
		this.data.put(key, value);
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
