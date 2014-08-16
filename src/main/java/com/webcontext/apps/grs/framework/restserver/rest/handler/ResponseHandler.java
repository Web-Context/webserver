/**
 * 
 */
package com.webcontext.apps.grs.framework.restserver.rest.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.log4j.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.webcontext.apps.grs.framework.restserver.http.HttpRequest;
import com.webcontext.apps.grs.framework.restserver.http.IHttpResponse;
import com.webcontext.apps.grs.framework.restserver.server.RestServer;
import com.webcontext.apps.grs.framework.restserver.server.RestServer.HttpMethod;
import com.webcontext.apps.grs.framework.restserver.server.RestServer.HttpStatus;

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
 * @author Frédéric Delorme<frederic.delorme@web-context.com>
 * 
 */
@SuppressWarnings("restriction")
public abstract class ResponseHandler<T extends IHttpResponse> implements
		HttpHandler {

	private static final Logger LOGGER = Logger
			.getLogger(ResponseHandler.class);

	/**
	 * Linked RestServer serving this RestHandler.
	 */
	protected RestServer server;

	/**
	 * Initialize RestHandler with the managing server.
	 * 
	 * @param server
	 */
	public ResponseHandler(RestServer server) {
		this.server = server;
	}

	public abstract T createResponse(OutputStream outputStream);

	/**
	 * Process request. only GET, POST, pUT, DELETE, OPTIONS and HEAD are
	 * implemented in this HTTP Request handler. Based on
	 * <code>getRequestMethod()</code>, the right processing method is called.
	 */
	public void handle(HttpExchange httpExchange) {

		T response = createResponse(httpExchange.getResponseBody());
		HttpStatus errCode = HttpStatus.NOT_FOUNT;
		// prepare and send response.
		try {
			Map<String, Set<String>> parameters = extractParameters(httpExchange);
			HttpRequest request = new HttpRequest(httpExchange, parameters);
			statistics(errCode, request);
			httpExchange.setAttribute("Content-Type", response.getEncodage());

			if (httpExchange.getRequestMethod().equals(HttpMethod.GET.name())) {
				errCode = get(request, response);
			} else if (httpExchange.getRequestMethod().equals(
					HttpMethod.POST.name())) {
				errCode = post(request, response);
			} else if (httpExchange.getRequestMethod().equals(
					HttpMethod.PUT.name())) {
				errCode = put(request, response);
			} else if (httpExchange.getRequestMethod().equals(
					HttpMethod.DELETE.name())) {
				errCode = delete(request, response);
			} else if (httpExchange.getRequestMethod().equals(
					HttpMethod.OPTIONS.name())) {
				errCode = options(request, response);
			} else if (httpExchange.getRequestMethod().equals(
					HttpMethod.HEAD.name())) {
				errCode = head(request, response);
			}
			String strResponse = processResponse(response);

			httpExchange.sendResponseHeaders(errCode.getCode(),
					strResponse.getBytes().length);
			OutputStream osResp = httpExchange.getResponseBody();
			osResp.write(strResponse.getBytes());
			osResp.close();
		} catch (IOException e) {
			LOGGER.error("Error during retrieving data.", e);
		}
	}

	/**
	 * @param response
	 * @return
	 */
	protected abstract String processResponse(T response);

	protected void statistics(HttpStatus errCode, HttpRequest request) {
		// some statistics on request processing.
		server.getInfo().setLastRequest(new Date());
		server.getInfo().setRequestCounter(
				server.getInfo().getRequestCounter() + 1);
		server.getInfo().setLastURI(request.getHttpExchange().getRequestURI());

		if (errCode.equals(HttpStatus.OK)) {
			server.getInfo().setSuccessfulRequestCounter(
					server.getInfo().getSuccessfulRequestCounter() + 1);

		} else {
			server.getInfo().setErrorRequestCounter(
					server.getInfo().getErrorRequestCounter() + 1);
			server.getInfo().setLastErrorURI(
					request.getHttpExchange().getRequestURI());
		}
	}

	/**
	 * Extract the in line parameters to a map.
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
	 *            the HttpResponse to perform response write.
	 * @throws IOException
	 */
	public HttpStatus get(HttpRequest request, T response) throws IOException {
		return HttpStatus.OK;

	}

	/**
	 * Need to override this method to perform HTTP POST request processing.
	 * 
	 * @param he
	 *            The {@link HttpRequest} object containing basic object from
	 *            the request.
	 * @param response
	 *            the HttpResponse to perform response write.
	 * @throws IOException
	 */
	public HttpStatus post(HttpRequest request, T response) throws IOException {
		return HttpStatus.OK;

	}

	/**
	 * Need to override this method to perform HTTP PUT request processing.
	 * 
	 * @param he
	 *            The {@link HttpRequest} object containing basic object from
	 *            the request.
	 * @param response
	 *            the HttpResponse to perform response write.
	 * @throws IOException
	 */
	public HttpStatus put(HttpRequest request, T response) throws IOException {
		return HttpStatus.OK;

	}

	/**
	 * Need to override this method to perform HTTP DELETE request processing.
	 * 
	 * @param he
	 *            The {@link HttpRequest} object containing basic object from
	 *            the request.
	 * @param response
	 *            the HttpResponse to perform response write.
	 * @throws IOException
	 */
	public HttpStatus delete(HttpRequest request, T response)
			throws IOException {
		return HttpStatus.OK;

	}

	/**
	 * Need to override this method to perform HTTP HEAD request processing.
	 * 
	 * @param he
	 *            The {@link HttpRequest} object containing basic object from
	 *            the request.
	 * @param response
	 *            the HttpResponse to perform response write.
	 * @throws IOException
	 */
	private HttpStatus head(HttpRequest request, T response) {
		return HttpStatus.OK;
	}

	/**
	 * Need to override this method to perform HTTP OPTIONS request processing.
	 * 
	 * @param he
	 *            The {@link HttpRequest} object containing basic object from
	 *            the request.
	 * @param response
	 *            the HttpResponse to perform response write.
	 * @throws IOException
	 */
	private HttpStatus options(HttpRequest request, T response) {
		return HttpStatus.OK;
	}
}
