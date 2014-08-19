package com.webcontext.framework.appserver.services.web.response.handler.impl.rest;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.webcontext.framework.appserver.services.web.response.io.HttpResponse;

/**
 * Response builder object to perform JSON serialization of object on Rest
 * answer.
 * 
 * @author Frédéric Delorme<frederic.delorme@web-context.com>
 * 
 */
public class RestResponse extends HttpResponse<Map<String, Object>> {

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
	public RestResponse(OutputStream outputStream) {
		super(outputStream);
		data = new HashMap<String, Object>();
		this.mimeType = "application/json";
		this.encodage = "UTF-8";
	}

	/**
	 * return all data in response object.
	 * 
	 * @return
	 */
	public Map<String, Object> getData() {
		return data;
	}

	/**
	 * Convert all objects in data list to one JSon Object to be send ton Http
	 * Response.
	 * 
	 * @return
	 */
	public String process() {
		return gson.toJson(data);
	}

	@Override
	public void add(Map<String, Object> data) {
		this.data.putAll(data);

	}

	public void addObject(String key, Object value) {
		this.data.put(key, value);
	}
}
