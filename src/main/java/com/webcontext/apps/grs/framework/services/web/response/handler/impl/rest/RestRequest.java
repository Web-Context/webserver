/**
 * 
 */
package com.webcontext.apps.grs.framework.services.web.response.handler.impl.rest;

import java.util.Map;
import java.util.Set;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.webcontext.apps.grs.framework.services.web.response.io.HttpRequest;

/**
 * @author frederic
 * 
 */
@SuppressWarnings("restriction")
public class RestRequest extends HttpRequest {

	/**
	 * Serialize response object to JSON object.
	 */
	protected Gson gson = new GsonBuilder().serializeNulls()
			.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
			.setDateFormat("yyyy-MM-dd HH:mm:ss").setPrettyPrinting()
			.setVersion(1.0).create();

	public RestRequest(HttpExchange httpExchange,
			Map<String, Set<String>> parameters) {
		super(httpExchange, parameters);
		// TODO Auto-generated constructor stub
	}

	public Object getJsonParameter(String name, Class<?> className,
			Object defaultValue) {
		String jsonValue = (String) getParameter(name, "").toArray()[0];

		Object value = null;
		if (!jsonValue.equals("")) {
			value = className.cast(gson.fromJson(jsonValue, className));
		} else {
			value = defaultValue;

		}
		return value;
	}

}
