/**
 * 
 */
package com.webcontext.apps.grs.framework.restserver.rest.handler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.webcontext.apps.grs.framework.restserver.http.HttpRequest;
import com.webcontext.apps.grs.framework.restserver.server.RestServer;
import com.webcontext.apps.grs.framework.restserver.server.RestServer.HttpStatus;

/**
 * @author 212391884
 *
 */
public class WebHandler extends ResponseHandler<WebResponse> {

	private static final Logger LOGGER = Logger.getLogger(WebHandler.class);

	/**
	 * @param server
	 */
	public WebHandler(RestServer server) {
		super(server);
	}

	@Override
	public HttpStatus get(HttpRequest request, WebResponse response)
			throws IOException {
		String resourcePath = request.getHttpExchange().getRequestURI()
				.toString();
		resourcePath = (resourcePath.replace("/web", "").equals("/")?"/index.html":resourcePath.replace("/web", ""));
		resourcePath=this.getClass().getResource("/").getPath().toString()+resourcePath;
		LOGGER.debug("request the resource " + resourcePath);
		String content = "";
		String line;
		try {
			BufferedReader br = new BufferedReader(new FileReader(resourcePath));

			while ((line = br.readLine()) != null) {
				response.add(line);
			}
			br.close();
		} catch (Exception e) {
			LOGGER.error("Unable to read file " + resourcePath);
		}

		return HttpStatus.OK;
	}

	@Override
	public WebResponse createResponse() {
		return new WebResponse();
	}

	@Override
	protected String processResponse(WebResponse response) {
		return response.process();
	}

}
