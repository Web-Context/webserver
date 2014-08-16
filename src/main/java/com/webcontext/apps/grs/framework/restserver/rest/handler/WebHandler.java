/**
 * 
 */
package com.webcontext.apps.grs.framework.restserver.rest.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.webcontext.apps.grs.framework.restserver.http.HttpRequest;
import com.webcontext.apps.grs.framework.restserver.rest.response.WebResponse;
import com.webcontext.apps.grs.framework.restserver.server.RestServer;
import com.webcontext.apps.grs.framework.restserver.server.RestServer.HttpStatus;

/**
 * IMplemnattion for a minimalistic Web Server, adding <code>HTML</code>,
 * <code>CSS</code>, <code>Javascript</code> and image resources serving
 * capabilities to the RestServer.
 * 
 * @author Fréderic Delorme<frederic.delorme@web-context.com>
 * 
 */
public class WebHandler extends ResponseHandler<WebResponse> {

	private static final Logger LOGGER = Logger.getLogger(WebHandler.class);

	private Map<String, String> mimeTypes = new HashMap<>();

	/**
	 * CReate the basic Mime Types table.
	 * 
	 * @param server
	 */
	public WebHandler(RestServer server) {
		super(server);
		mimeTypes.put("css", "text/css");
		mimeTypes.put("html", "text/html");
		mimeTypes.put("js", "application/javascript");
		mimeTypes.put("png", "image/png");
		mimeTypes.put("jpg,jpeg", "image/jpeg");
	}

	/**
	 * serve a simple page on an HTTP GET method.
	 */
	@SuppressWarnings("restriction")
	@Override
	public HttpStatus get(HttpRequest request, WebResponse response)
			throws IOException {
		String resourcePath = request.getHttpExchange().getRequestURI()
				.toString();
		resourcePath = (resourcePath.replace("/web", "").equals("/") ? "index.html"
				: resourcePath.replace("/web", ""));
		resourcePath = this.getClass().getResource("/").getPath().toString()
				+ resourcePath.substring(0);
		// extract resource extension
		String mimeType = extractMimeType(resourcePath);
		if (mimeType != null) {
			response.setMimeType(mimeType);
		}
		// Send file content to response stream.
		String line;
		File resourceFile = new File(resourcePath);
		try {
			BufferedReader br = new BufferedReader(new FileReader(resourcePath));

			while ((line = br.readLine()) != null) {
				response.add(line);
			}
			br.close();
		} catch (Exception e) {
			LOGGER.error("Unable to read resource for URI "
					+ request.getHttpExchange().getRequestURI().toString());
		}
		LOGGER.info(String.format(
				"Send resource [%s] of size %s with mime type [%s].",
				resourcePath, formatSize(resourceFile), mimeType));

		return HttpStatus.OK;
	}

	/**
	 * Compute File size.
	 * 
	 * @param resourceFile
	 * @return
	 */
	private String formatSize(File resourceFile) {
		float value = resourceFile.length();
		if (value < 1024) {
			return "" + value + " octets";
		} else if (value > 1024 * 1024) {
			return "" + ((float) value / (1024 * 1024)) + " Mo";
		} else {
			return "" + ((float) value / 1024) + " Ko";
		}
	}

	/**
	 * Extract MIME type for a resources, based on the <code>mimestypes</code>
	 * table.
	 * 
	 * @param resourcePath
	 * @return
	 */
	private String extractMimeType(String resourcePath) {
		String extension = resourcePath
				.substring(resourcePath.lastIndexOf(".") + 1);
		String mimeType = mimeTypes.get(extension);
		return mimeType;
	}

	/**
	 * Response Factory for this Handler implementation.
	 */
	@Override
	public WebResponse createResponse(OutputStream outputStream) {
		return new WebResponse(outputStream);
	}

	/**
	 * Process response for this kind of ResponseHandler. It is basicaly call
	 * the <code>process()</code> method from the <code>WebResponse</code>
	 * object.
	 */
	@Override
	protected String processResponse(WebResponse response) {
		return response.process();
	}

}
