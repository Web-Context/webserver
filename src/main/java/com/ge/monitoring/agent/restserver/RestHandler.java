/**
 * 
 */
package com.ge.monitoring.agent.restserver;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.ge.monitoring.agent.restserver.RestServer.HttpError;
import com.ge.monitoring.agent.restserver.RestServer.HttpMethod;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * The RestHandler class is a specific Handler to perform processing of HTTP
 * request, depending on used <code>{@link HttpMethod}</code>.
 * 
 * it will call the right method according to the HTTP method:
 * <ul>
 * <li><code>get(...)</code> : perform processing of Http <code>GET</code>
 * request</li>
 * <li><code>post(...)</code> : perform processing of Http <code>POST</code>
 * request</li>
 * <li><code>put(...)</code> : perform processing of Http <code>PUT</code>
 * request</li>
 * <li><code>delete(...)</code> : perform processing of Http <code>DELETE</code>
 * request</li>
 * <li><code>options(...)</code> : perform processing of Http
 * <code>OPTIONS</code> request</li>
 * <li><code>head(...)</code> : perform processing of Http <code>HEAD</code>
 * request</li>
 * </ul>
 * 
 * @author Frédéric Delorme<frederic.delorme@serphydose.com>
 *
 */
@SuppressWarnings("restriction")
public class RestHandler implements HttpHandler {

	public void handle(HttpExchange httpExchange) throws IOException {

		RestResponse response = new RestResponse();
		HttpError errCode = HttpError.OK;

		Map<String, String> parameters = extractParameters(httpExchange);

		if (httpExchange.getRequestMethod().equals(HttpMethod.GET.name())) {
			errCode = get(httpExchange, response);
		} else if (httpExchange.getRequestMethod().equals(HttpMethod.POST)) {
			errCode = post(httpExchange, response);
		} else if (httpExchange.getRequestMethod().equals(HttpMethod.PUT)) {
			errCode = put(httpExchange, response);
		} else if (httpExchange.getRequestMethod().equals(HttpMethod.DELETE)) {
			errCode = delete(httpExchange, response);
		} else if (httpExchange.getRequestMethod().equals(HttpMethod.OPTIONS)) {
			errCode = options(httpExchange, response);
		} else if (httpExchange.getRequestMethod().equals(HttpMethod.HEAD)) {
			errCode = head(httpExchange, response);
		}

		String strResponse = response.toJson();
		httpExchange.sendResponseHeaders(errCode.getCode(),
				strResponse.length());
		OutputStream osResp = httpExchange.getResponseBody();
		osResp.write(strResponse.getBytes());
		osResp.close();
	}

	/**
	 * Extract inline parameters to a map.
	 * 
	 * @param httpExchange
	 * @return
	 */
	private Map<String, String> extractParameters(HttpExchange httpExchange) {
		String query = httpExchange.getRequestURI().getQuery();
		Map<String, String> httpParameters = new HashMap<String, String>();

		String[] parameters = query.split("&");
		for (String parameter : parameters) {
			if (parameter.contains("=")) {
				String[] values = parameter.split("=");
				httpParameters.put(values[0], values[1]);
			} else {
				httpParameters.put(parameter, null);
			}
		}

		return httpParameters;
	}

	/**
	 * Need to override this method to perform HTTP GET request processing.
	 * 
	 * @param he
	 *            The {@link HttpExchange} object containing basic object from
	 *            the request.
	 * @param response
	 *            the RestResponse to perform response write.
	 * @throws IOException
	 */
	public HttpError get(HttpExchange he, RestResponse response)
			throws IOException {
		return HttpError.OK;

	}

	/**
	 * Need to override this method to perform HTTP POST request processing.
	 * 
	 * @param he
	 *            The {@link HttpExchange} object containing basic object from
	 *            the request.
	 * @param response
	 *            the RestResponse to perform response write.
	 * @throws IOException
	 */
	public HttpError post(HttpExchange he, RestResponse response)
			throws IOException {
		return HttpError.OK;

	}

	/**
	 * Need to override this method to perform HTTP PUT request processing.
	 * 
	 * @param he
	 *            The {@link HttpExchange} object containing basic object from
	 *            the request.
	 * @param response
	 *            the RestResponse to perform response write.
	 * @throws IOException
	 */
	public HttpError put(HttpExchange he, RestResponse response)
			throws IOException {
		return HttpError.OK;

	}

	/**
	 * Need to override this method to perform HTTP DELETE request processing.
	 * 
	 * @param he
	 *            The {@link HttpExchange} object containing basic object from
	 *            the request.
	 * @param response
	 *            the RestResponse to perform response write.
	 * @throws IOException
	 */
	public HttpError delete(HttpExchange he, RestResponse response)
			throws IOException {
		return HttpError.OK;

	}

	/**
	 * Need to override this method to perform HTTP HEAD request processing.
	 * 
	 * @param he
	 *            The {@link HttpExchange} object containing basic object from
	 *            the request.
	 * @param response
	 *            the RestResponse to perform response write.
	 * @throws IOException
	 */
	private HttpError head(HttpExchange t, RestResponse response) {
		return HttpError.OK;
	}

	/**
	 * Need to override this method to perform HTTP OPTIONS request processing.
	 * 
	 * @param he
	 *            The {@link HttpExchange} object containing basic object from
	 *            the request.
	 * @param response
	 *            the RestResponse to perform response write.
	 * @throws IOException
	 */
	private HttpError options(HttpExchange t, RestResponse response) {
		return HttpError.OK;
	}
}
