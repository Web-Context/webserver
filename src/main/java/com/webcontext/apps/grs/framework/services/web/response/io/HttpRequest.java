package com.webcontext.apps.grs.framework.services.web.response.io;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

/**
 * Http Request object. contains all HTTP headers and HTTP request parameters.
 * 
 * @author Frédéric Delorme<frederic.delorme@web-context.com>
 * 
 */
@SuppressWarnings("restriction")
public class HttpRequest {

	private Map<String, Set<String>> parameters;
	private Headers headers = new Headers();
	private HttpExchange httpExchange;

	public HttpRequest(HttpExchange httpExchange,
			Map<String, Set<String>> parameters) {
		this.parameters = parameters;
		this.setHttpExchange(httpExchange);
		this.headers.putAll(httpExchange.getResponseHeaders());
	}

	/**
	 * @return the httpExchange
	 */
	public HttpExchange getHttpExchange() {
		return httpExchange;
	}

	/**
	 * @param httpExchange
	 *            the httpExchange to set
	 */
	public void setHttpExchange(HttpExchange httpExchange) {
		this.httpExchange = httpExchange;
	}

	/**
	 * @return the headers
	 */
	public Headers getHeaders() {
		return headers;
	}

	/**
	 * @param headers
	 *            the headers to set
	 */
	public void setHeaders(Headers headers) {
		this.headers.putAll(headers);
	}

	/**
	 * @return the parameters
	 */
	public Map<String, Set<String>> getParameters() {
		return parameters;
	}

	/**
	 * Return the parameter <code>name</code>, return empty string if parameter
	 * does not exists.
	 * 
	 * @param name
	 *            is the name of the parameter to retrieve on the URL.
	 * @return a String containing the value of the parameter
	 */
	public Set<String> getParameter(String name) {
		return getParameter(name, "");
	}

	/**
	 * Retrieve the <code>name</code> parameter on the {@link HttpRequest}. if
	 * parameter is not set, return the <code>defaultValue</code>.
	 * 
	 * @param name
	 *            Name of the parameter to retrieve from the URL
	 * @param defaultValue
	 *            the default value if parameter does not exists on URL. is
	 *            null, no default value will be set.
	 * @return
	 */
	public Set<String> getParameter(String name, String defaultValue) {

		if (parameters.containsKey(name)) {
			return parameters.get(name);
		} else if (defaultValue != null) {
			Set<String> defaultValues = new CopyOnWriteArraySet<>();
			defaultValues.add(defaultValue);
			return defaultValues;
		} else {
			return null;
		}
	}

	/**
	 * return the object cast to its castClass.
	 * 
	 * @param name
	 * @param castClass
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public Object getParameter(String name, Class<?> castClass,
			String defaultValue) throws InstantiationException,
			IllegalAccessException {
		Object value = null;
		switch (castClass.getName()) {
		case "java.lang.Integer":
			value = new Integer((String) getParameter(name, defaultValue)
					.toArray()[0]);
			break;
		case "java.lang.Float":
			value = new Float((String) getParameter(name, defaultValue)
					.toArray()[0]);
			break;

		case "java.lang.Boolean":
			value = new Boolean((String) getParameter(name, defaultValue)
					.toArray()[0]);
			break;

		case "java.lang.Double":
			value = new Double((String) getParameter(name, defaultValue)
					.toArray()[0]);
			break;

		case "java.lang.String":
			value = castClass
					.cast(getParameter(name, defaultValue).toArray()[0]);
			break;

		}

		return value;
	}

	/**
	 * @param parameters
	 *            the parameters to set
	 */
	public void setParameters(Map<String, Set<String>> parameters) {
		this.parameters = parameters;
	}

}
