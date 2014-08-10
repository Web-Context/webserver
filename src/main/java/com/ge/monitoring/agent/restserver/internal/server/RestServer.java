/**
 * 
 */
package com.ge.monitoring.agent.restserver.internal.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.log4j.Logger;

import com.ge.monitoring.agent.restserver.MeasuresRestHandler;
import com.ge.monitoring.agent.restserver.internal.rest.RestHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Internal HTTP server on a specific port (default is 8888).
 * 
 * @author Frédéric Delorme<frederic.delorme@serphydose.com>
 *
 */

@SuppressWarnings("restriction")
public class RestServer {
	private static final Logger LOGGER = Logger.getLogger(RestServer.class);

	/**
	 * Internal HTTP method
	 * 
	 * @author Frédéric Delorme<frederic.delorme@serphydose.com>
	 *
	 */
	public enum HttpMethod {
		GET, PUT, POST, DELETE, OPTIONS, HEAD, TRACE, CONNECT
	}

	/**
	 * list of HTTP implemented ERROR code.
	 * 
	 * @author Frédéric Delorme<frederic.delorme@serphydose.com>
	 *
	 */
	public enum HttpError {
		OK(200), CREATED(201), ACCEPTED(202), MOVE_PERMANENTLY(301), MOVED_TEMPORARILY(
				302), BAD_REQUEST(400), UNAUTHORIZED(401), PAYMENT_REQUIRED(402), FORBIDDEN(
				403), NOT_FOUNT(404), I_AM_A_TEAPOT(418), INTERNAL_ERROR(500);

		private int code;

		private HttpError(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}

	}

	/**
	 * Sun SE Http Server
	 */
	private HttpServer server = null;
	/**
	 * Http port used by the server at startup.
	 */
	private int port = 8888;

	/**
	 * Default constructor initializing HTTP Rest server on the default port (8
	 * 888).
	 * 
	 * @throws IOException
	 */
	public RestServer() throws IOException {
		initServer(this.port);
	}

	/**
	 * Initialize the Rest HTTP Server on the specified <code>port</code> à
	 * construction time.
	 * 
	 * @param port
	 * @throws IOException
	 */
	public RestServer(int port) throws IOException {
		this.port = port;
		initServer(port);
	}

	/**
	 * Initialize the Rest HTTP Server.
	 * 
	 * @param port
	 * @throws IOException
	 */
	private void initServer(int port) throws IOException {
		server = HttpServer.create(new InetSocketAddress(port), 0);
		server.setExecutor(null);
		LOGGER.info("Server has just been insitialized on port " + port);
	}

	/**
	 * Start the Rest HTTP server after initialization.
	 */
	public void start() {
		if (server != null) {
			server.start();
			LOGGER.info("Server Start");
		}
	}

	/**
	 * Stop the Rest HTTP Server.
	 */
	public void stop() {
		if (server != null) {
			server.stop(0);
			LOGGER.info("Server Stopped");
		}
	}

	public void addContext(String restPath, RestHandler restHandler) {
		server.createContext(restPath, restHandler);
	}

	public static void main(String[] args) {

		RestServer server;
		int port = 0;
		try {
			port = getIntArg(args, "port", 8888);
			server = new RestServer(port);
			server.addContext("/rest/instruments", new MeasuresRestHandler());
			server.start();
		} catch (IOException e) {
			LOGGER.error("Unable to start the internal Rest HTTP Server component on port "
					+ port + ". Reason : " + e.getLocalizedMessage());
		}
	}

	/**
	 * retrieve an <code>int</code> parameter name <code>argName</code> from the
	 * <code>args</code> list. if value is not set , return the
	 * <code>defaultValue</code>.
	 * 
	 * @param args
	 *            list of arguments from the Java main method.
	 * @param argName
	 *            argument name to search in the list
	 * @param defaultValue
	 *            the default value if <code>argName</code> value is not found
	 *            in the <code>args</code> list.
	 * @return value of the argument, or the fall back default value
	 *         <code>defaultValue</code>.
	 */
	private static int getIntArg(String[] args, String argName, int defaultValue) {
		for (String arg : args)
			if (arg.startsWith(argName)) {
				String[] argValue = arg.split("=");
				return Integer.parseInt(argValue[1]);
			}
		return defaultValue;
	}

}
