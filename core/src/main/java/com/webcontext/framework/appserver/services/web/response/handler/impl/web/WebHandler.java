/**
 * 
 */
package com.webcontext.framework.appserver.services.web.response.handler.impl.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.webcontext.framework.appserver.services.web.response.handler.ContextHandler;
import com.webcontext.framework.appserver.services.web.response.handler.ResponseHandler;
import com.webcontext.framework.appserver.services.web.response.io.HttpRequest;
import com.webcontext.framework.appserver.services.web.server.GenericServer;
import com.webcontext.framework.appserver.services.web.server.GenericServer.HttpStatus;
import com.webcontext.framework.appserver.utils.FileIO;

/**
 * Implementation for a minimalistic Web TestServer, adding <code>HTML</code>,
 * <code>CSS</code>, <code>Javascript</code> and image resources serving
 * capabilities to the GenericServer.
 * 
 * @author Fréderic Delorme<frederic.delorme@web-context.com>
 * 
 */
@ContextHandler(path = "/web")
public class WebHandler extends ResponseHandler<WebResponse> {

	/*
	 * Logger ...
	 */
	private static final Logger LOGGER = Logger.getLogger(WebHandler.class);

	/**
	 * MIME types for Web handling files !
	 */
	private Map<String, String> mimeTypes = new HashMap<>();

	/**
	 * Create the basic MIME Types table.
	 * 
	 * @param server
	 * @throws IOException
	 */
	public WebHandler(GenericServer server) throws IOException {
		super(server);
		loadMimeTypesFromFiles();
	}

	/**
	 * load MIME types from file.
	 * 
	 * @throws IOException
	 */
	public void loadMimeTypesFromFiles() throws IOException {
		Properties props = new Properties();
		props.load(this.getClass().getResourceAsStream(
				"/config/web-mimes.properties"));
		for (Entry<Object, Object> prop : props.entrySet()) {
			mimeTypes.put((String) prop.getKey(), (String) prop.getValue());
		}
	}

	/**
	 * Serve a simple page on an HTTP GET method.
	 */
	@SuppressWarnings("restriction")
	@Override
	public HttpStatus get(HttpRequest request, WebResponse response)
			throws IOException {

		String resourcePath = request.getHttpExchange().getRequestURI()
				.toString(),
				fullPath="";

		resourcePath = (resourcePath.replace("/web", "").equals("/") ? "index.html"
				: resourcePath.replace("/web", ""));

		LOGGER.info(String.format("Web Resource %s from path = [%s]", resourcePath, this.getClass().getResource("/").getFile()));

		fullPath = this.getClass().getResource("/").getPath().toString()
				+ resourcePath.substring(0);

		// extract resource extension
		String mimeType = extractMimeType(fullPath);

		if (mimeType != null) {
			response.setMimeType(mimeType);
		}

		// Send file content to response stream.
		
		File resourceFile = new File(fullPath);
		if (resourceFile.exists()) {
			try {
				String content = FileIO.fastRead(resourcePath);
				if (content != null && !content.equals("")) {
					response.add(content);
					LOGGER.info(String
							.format("Send resource [%s] of size %s with mime type [%s].",
									resourcePath, formatSize(resourceFile),
									mimeType));
					return HttpStatus.OK;
				} else {
					LOGGER.error(String
							.format("error accessing resource [%s]",resourcePath));
					return HttpStatus.INTERNAL_ERROR;
				}
			} catch (FileNotFoundException fnfe) {
				LOGGER.error(String
						.format("File [%s] not found",resourcePath),fnfe);
				return HttpStatus.NOT_FOUND;
			}
		} else {
			LOGGER.warn(String
					.format("File resource [%s] not found !",resourcePath));
			return HttpStatus.NOT_FOUND;
		}
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
		String mimeType = null;
		if (mimeTypes.containsKey(extension)) {
			mimeType = mimeTypes.get(extension);
		} else {
			mimeType = FileIO.getContentType(resourcePath);
			if ((mimeType == null || mimeType.equals(""))
					&& mimeTypes.containsKey("default")) {
				mimeType = mimeTypes.get("default");
			} else {
				mimeType = "application/actet-stream";
			}
		}
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
	 * Process response for this kind of ResponseHandler. It is basically
	 * calling the <code>process()</code> method from the
	 * <code>WebResponse</code> object.
	 */
	@Override
	protected String processResponse(WebResponse response) {
		return response.process();
	}

}
