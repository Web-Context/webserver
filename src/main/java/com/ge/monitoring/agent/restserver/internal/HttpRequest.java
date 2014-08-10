/**
 * 
 */
package com.ge.monitoring.agent.restserver.internal;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

/**
 * Http Request object. contains all headers, parameters.
 * 
 * @author Frédéric Delorme<frederic.delorme@serphydose.com>
 * 
 */
@SuppressWarnings("restriction")
public class HttpRequest {

	private Map<String, Set<String>> parameters;
	private Headers headers;
	private HttpExchange httpExchange;

	public HttpRequest(HttpExchange httpExchange,
			Map<String, Set<String>> parameters) {
		this.parameters = parameters;
		this.setHttpExchange(httpExchange);
		setHeaders(httpExchange.getResponseHeaders());
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
		this.headers = headers;
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

	@SuppressWarnings("unchecked")
	public Set<String> getParameter(String name, String defaultValue) {

		if (parameters.containsKey(name)) {
			return parameters.get(name);
		} else {
			Set<String> defaultValues = new CopyOnWriteArraySet<>();
			defaultValues.add(defaultValue);
			return defaultValues;
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
	public Object getParameter(String name, Class<?> castClass)
			throws InstantiationException, IllegalAccessException {
		Object value = null;
		if (parameters.containsKey(name)) {
			value = castClass.cast(parameters.get(name));
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
