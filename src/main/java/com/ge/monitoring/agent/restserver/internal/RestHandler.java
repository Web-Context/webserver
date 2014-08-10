/**
 * 
 */
package com.ge.monitoring.agent.restserver.internal;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.ge.monitoring.agent.restserver.internal.RestServer.HttpError;
import com.ge.monitoring.agent.restserver.internal.RestServer.HttpMethod;
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

		Map<String, Set<String>> parameters = extractParameters(httpExchange);
		HttpRequest request = new HttpRequest(httpExchange, parameters);

		if (httpExchange.getRequestMethod().equals(HttpMethod.GET.name())) {
			errCode = get(request, response);
		} else if (httpExchange.getRequestMethod().equals(HttpMethod.POST)) {
			errCode = post(request, response);
		} else if (httpExchange.getRequestMethod().equals(HttpMethod.PUT)) {
			errCode = put(request, response);
		} else if (httpExchange.getRequestMethod().equals(HttpMethod.DELETE)) {
			errCode = delete(request, response);
		} else if (httpExchange.getRequestMethod().equals(HttpMethod.OPTIONS)) {
			errCode = options(request, response);
		} else if (httpExchange.getRequestMethod().equals(HttpMethod.HEAD)) {
			errCode = head(request, response);
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
	private Map<String, Set<String>> extractParameters(HttpExchange httpExchange) {
		Map<String, Set<String>> httpParameters = new HashMap<String, Set<String>>();
		String query;
		if ((query = httpExchange.getRequestURI().getQuery()) != null) {

			String[] parameters = query.split("&");
			for (String parameter : parameters) {
				if (parameter.contains("=")) {
					String[] values = parameter.split("=");
					if (!httpParameters.containsKey(values[0])) {
						Set<String> listValues = new CopyOnWriteArraySet<String>();
						listValues.add(values[1]);
						httpParameters.put(values[0], listValues);
					} else {
						httpParameters.get(values[0]).add(values[1]);
					}
				} else {
					httpParameters.put(parameter, null);
				}
			}

		}
		return httpParameters;
	}

	/**
	 * Need to override this method to perform HTTP GET request processing.
	 * 
	 * @param he
	 *            The {@link HttpRequest} object containing basic object from
	 *            the request.
	 * @param response
	 *            the RestResponse to perform response write.
	 * @throws IOException
	 */
	public HttpError get(HttpRequest request, RestResponse response)
			throws IOException {
		return HttpError.OK;

	}

	/**
	 * Need to override this method to perform HTTP POST request processing.
	 * 
	 * @param he
	 *            The {@link HttpRequest} object containing basic object from
	 *            the request.
	 * @param response
	 *            the RestResponse to perform response write.
	 * @throws IOException
	 */
	public HttpError post(HttpRequest request, RestResponse response)
			throws IOException {
		return HttpError.OK;

	}

	/**
	 * Need to override this method to perform HTTP PUT request processing.
	 * 
	 * @param he
	 *            The {@link HttpRequest} object containing basic object from
	 *            the request.
	 * @param response
	 *            the RestResponse to perform response write.
	 * @throws IOException
	 */
	public HttpError put(HttpRequest request, RestResponse response)
			throws IOException {
		return HttpError.OK;

	}

	/**
	 * Need to override this method to perform HTTP DELETE request processing.
	 * 
	 * @param he
	 *            The {@link HttpRequest} object containing basic object from
	 *            the request.
	 * @param response
	 *            the RestResponse to perform response write.
	 * @throws IOException
	 */
	public HttpError delete(HttpRequest request, RestResponse response)
			throws IOException {
		return HttpError.OK;

	}

	/**
	 * Need to override this method to perform HTTP HEAD request processing.
	 * 
	 * @param he
	 *            The {@link HttpRequest} object containing basic object from
	 *            the request.
	 * @param response
	 *            the RestResponse to perform response write.
	 * @throws IOException
	 */
	private HttpError head(HttpRequest request, RestResponse response) {
		return HttpError.OK;
	}

	/**
	 * Need to override this method to perform HTTP OPTIONS request processing.
	 * 
	 * @param he
	 *            The {@link HttpRequest} object containing basic object from
	 *            the request.
	 * @param response
	 *            the RestResponse to perform response write.
	 * @throws IOException
	 */
	private HttpError options(HttpRequest request, RestResponse response) {
		return HttpError.OK;
	}
}
